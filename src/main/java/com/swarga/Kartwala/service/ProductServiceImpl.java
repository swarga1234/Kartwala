package com.swarga.Kartwala.service;

import com.swarga.Kartwala.exception.APIException;
import com.swarga.Kartwala.exception.ResourceNotFoundException;
import com.swarga.Kartwala.model.Category;
import com.swarga.Kartwala.model.Product;
import com.swarga.Kartwala.payload.ProductDTO;
import com.swarga.Kartwala.payload.ProductResponse;
import com.swarga.Kartwala.repository.CategoryRepository;
import com.swarga.Kartwala.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public ProductDTO addProduct(Long categoryId, ProductDTO productDTO) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow( () -> new ResourceNotFoundException("Category", "categoryId", categoryId));
        Product product = modelMapper.map(productDTO, Product.class);
        product.setImage("default.png");
        product.setCategory(category);
        product.setSpecialPrice(product.getPrice()- (product.getDiscount()/100)*product.getPrice());
        Product savedProduct = productRepository.save(product);
        return modelMapper.map(savedProduct, ProductDTO.class);
    }

    @Override
    public ProductResponse getAllProducts(Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Sort sortByandOrder = sortOrder.equalsIgnoreCase("desc")?Sort.by(sortBy).descending()
                :Sort.by(sortBy).ascending();
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByandOrder);
        Page<Product> productPage = productRepository.findAll(pageDetails);
        List<Product> products = productPage.getContent();
        if( products==null || products.isEmpty()){
            throw new APIException("No Products available!!");
        }
        List<ProductDTO> productDTOS = products.stream()
                .map( product -> modelMapper.map(product, ProductDTO.class) )
                .toList();
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        productResponse.setTotalPages(productPage.getTotalPages());
        productResponse.setPageNumber(productPage.getNumber());
        productResponse.setPageSize(productPage.getSize());
        productResponse.setTotalElements(productPage.getTotalElements());
        productResponse.setLastPage(productPage.isLast());
        return productResponse;
    }

    @Override
    public ProductResponse getProductsByCategory(Long categoryId, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
        Category existingCategory = categoryRepository.findById(categoryId)
                .orElseThrow( () -> new ResourceNotFoundException("Category", "categoryId", categoryId));
        Sort sortByandOrder = sortOrder.equalsIgnoreCase("desc")?
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageDetails = PageRequest.of(pageNumber, pageSize, sortByandOrder);
        Page<Product> productPage = productRepository.findByCategory(existingCategory, pageDetails);
        List<Product> products = productPage.getContent();
        if(products==null || products.isEmpty()){
            throw new APIException("No Products available for category: "+existingCategory.getCategoryName());
        }
        List<ProductDTO> productDTOS =products.stream()
                .map( product -> {
                    return modelMapper.map(product, ProductDTO.class);
                }).toList();
        ProductResponse productResponse = new ProductResponse();
        productResponse.setTotalElements(productPage.getTotalElements());
        productResponse.setLastPage(productPage.isLast());
        productResponse.setTotalPages(productPage.getTotalPages());
        productResponse.setPageNumber(productPage.getNumber());
        productResponse.setPageSize(productPage.getSize());
        productResponse.setContent(productDTOS);
        return productResponse;
    }

    @Override
    public ProductResponse searchProductsByKeyword(String keyword, Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

        Sort sortByandOrder= sortOrder.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageDetails = PageRequest.of(pageNumber,pageSize,sortByandOrder);
        Page<Product> productPage = productRepository.findByProductNameLikeIgnoreCase("%"+keyword+"%", pageDetails);
        List<Product> products = productPage.getContent();
        if(products==null || products.isEmpty()){
            throw new APIException("No matching Products with keyword: "+keyword+" found!!");
        }
        List<ProductDTO> productDTOS = products.stream().map( product -> {
            return modelMapper.map(product, ProductDTO.class);
        }).toList();

        ProductResponse productResponse = new ProductResponse();
        productResponse.setTotalElements(productPage.getTotalElements());
        productResponse.setLastPage(productPage.isLast());
        productResponse.setTotalPages(productPage.getTotalPages());
        productResponse.setPageNumber(productPage.getNumber());
        productResponse.setPageSize(productPage.getSize());
        productResponse.setContent(productDTOS);
        return productResponse;
    }

    @Override
    public ProductDTO updateProduct(Long productId, ProductDTO productDTO) {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow( () -> new ResourceNotFoundException("Product", "productId", productId));
        //Product product = modelMapper.map(productDTO, Product.class);
        existingProduct.setProductName(productDTO.getProductName());
        existingProduct.setDescription(productDTO.getDescription());
        existingProduct.setQuantity(productDTO.getQuantity());
        existingProduct.setDiscount(productDTO.getDiscount());
        existingProduct.setPrice(productDTO.getPrice());
        double specialPrice = productDTO.getPrice() - (productDTO.getDiscount()/100)* productDTO.getPrice();
        existingProduct.setSpecialPrice(specialPrice);
        Product updatedProduct = productRepository.save(existingProduct);
        return modelMapper.map(updatedProduct, ProductDTO.class);
    }

    @Override
    public ProductDTO deleteProduct(Long productId) {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow( () -> new ResourceNotFoundException("Product", "productId", productId));
        productRepository.delete(existingProduct);
        return modelMapper.map(existingProduct,ProductDTO.class);
    }

}

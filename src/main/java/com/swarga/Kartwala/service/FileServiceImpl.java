package com.swarga.Kartwala.service;

import com.swarga.Kartwala.exception.APIException;
import com.swarga.Kartwala.exception.FileNotUploadedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService{

    @Override
    public String uploadImage(String path, MultipartFile productImage) {
        String filename = productImage.getOriginalFilename();
        if(filename==null || filename.isEmpty()){
            throw new APIException("Image to be uploaded is empty or null!!");
        }
        //Generate a unique UUID filename
        String randomId = UUID.randomUUID().toString();
        filename = randomId.concat(filename.substring(filename.lastIndexOf(".")));
        File folder = new File(path);
        if(!folder.exists()){
            folder.mkdir();
        }
        String filePath = path+File.separator+filename;
        try {
            Files.copy(productImage.getInputStream(), Paths.get(filePath));
        } catch (IOException e) {
            throw new FileNotUploadedException("Unable to upload the image!!",e);
        }
        return filename;
    }
}

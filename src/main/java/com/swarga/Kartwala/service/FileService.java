package com.swarga.Kartwala.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    String uploadImage(String path, MultipartFile productImage);
}

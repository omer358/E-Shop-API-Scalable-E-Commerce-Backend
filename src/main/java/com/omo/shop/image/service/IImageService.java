package com.omo.shop.image.service;

import com.omo.shop.image.dto.ImageDto;
import com.omo.shop.image.model.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IImageService {
    ImageDto getImageById(Long id);

    Image getImageEntityById(Long id);

    void deleteImageById(Long id);

    List<ImageDto> saveImages(List<MultipartFile> file, Long productId);

    void updateImage(MultipartFile file, Long imageId);

    byte[] decodeBase64ToBytes(String base64String);
}
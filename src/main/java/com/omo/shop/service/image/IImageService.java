package com.omo.shop.service.image;

import com.omo.shop.dto.ImageDto;
import com.omo.shop.models.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IImageService {
    ImageDto getImageById(Long id);

    Image getImageEntityById(Long id);

    void deleteImageById(Long id);

    List<ImageDto> saveImage(List<MultipartFile> file, Long productId);

    void updateImage(MultipartFile file, Long imageId);

    byte[] decodeBase64ToBytes(String base64String);
}
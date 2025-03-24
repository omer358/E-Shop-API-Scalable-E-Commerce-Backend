package com.omo.shop.service.image;

import com.omo.shop.dto.ImageDto;
import com.omo.shop.models.Image;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.sql.Blob;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class ImageMapper {

    private final ModelMapper modelMapper;

    public ImageDto toDto(Image image) {
        ImageDto imageDto = modelMapper.map(image, ImageDto.class);
        imageDto.setImageBase64(convertBlobToBase64(image.getImage())); // Convert Blob to Base64
        return imageDto;
    }

    public Image toEntity(ImageDto imageDto) {
        Image image = modelMapper.map(imageDto, Image.class);
        image.setImage(null); // ⚠️ Blob cannot be directly mapped, needs manual handling
        return image;
    }

    private String convertBlobToBase64(Blob blob) {
        try (InputStream inputStream = blob.getBinaryStream()) {
            byte[] bytes = inputStream.readAllBytes();
            return Base64.getEncoder().encodeToString(bytes);
        } catch (Exception e) {
            return null; // Handle exception
        }
    }
}

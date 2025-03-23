package com.omo.shop.dto;

import com.omo.shop.models.Image;
import lombok.Data;

import java.sql.Blob;
import java.util.Base64;

@Data
public class ImageDto {
    private Long imageId;
    private String imageName;
    private String fileType;
    private String base64Image;
    private String downloadUrl;

    public static ImageDto fromEntity(Image image) {
        ImageDto dto = new ImageDto();
        dto.setImageId(image.getId());
        dto.setImageName(image.getFileName());
        dto.setFileType(image.getFileType());
        dto.setDownloadUrl(image.getDownloadUrl());
        dto.setBase64Image(convertToBase64(image.getImage()));
        return dto;
    }

    public static Image fromDto(ImageDto dto) {
        Image image = new Image();
        image.setId(dto.getImageId());
        image.setFileName(dto.getImageName());
        image.setFileType(dto.getFileType());
        image.setDownloadUrl(dto.getDownloadUrl());
        // Base64 conversion to Blob is omitted for now.
        return image;
    }

    private static String convertToBase64(Blob blob) {
        if (blob == null) return null;
        try {
            byte[] bytes = blob.getBytes(1, (int) blob.length());
            return Base64.getEncoder().encodeToString(bytes);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}

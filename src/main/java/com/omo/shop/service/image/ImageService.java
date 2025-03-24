package com.omo.shop.service.image;

import com.omo.shop.dto.ImageDto;
import com.omo.shop.dto.ProductDto;
import com.omo.shop.exceptions.ResourceNotFoundException;
import com.omo.shop.models.Image;
import com.omo.shop.repository.ImageRepository;
import com.omo.shop.service.product.IProductService;
import com.omo.shop.service.product.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService implements IImageService {
    private final ImageRepository imageRepository;
    private final IProductService productService;
    private final ProductMapper productMapper;
    private final ImageMapper imageMapper;

    @Override
    public ImageDto getImageById(Long id) {
        Image image = imageRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("No image found with id: " + id)
                );
        return imageMapper.toDto(image);
    }

    @Override
    public void deleteImageById(Long id) {
        imageRepository.deleteById(id);
    }

    @Override
    public List<ImageDto> saveImage(List<MultipartFile> files, Long productId) {
        ProductDto productDto = productService.getProductById(productId);
        List<ImageDto> savedImageDto = new ArrayList<>();
        for (MultipartFile file : files) {
            try {
                Image image = new Image();
                image.setFileName(file.getOriginalFilename());
                image.setFileType(file.getContentType());
                image.setImage((new SerialBlob(file.getBytes())));
                image.setProduct(productMapper.toEntity(
                        productDto)
                );
                String buildDownloadurl = "/api/v1/images/image/download";
                String downloadUrl = buildDownloadurl + image.getId();
                image.setDownloadUrl(downloadUrl);
                Image savedImage = imageRepository.save(image);
                savedImage.setDownloadUrl(buildDownloadurl + savedImage.getId());
                imageRepository.save(savedImage);
                ImageDto imageDto = new ImageDto();
                imageDto.setId(savedImage.getId());
                imageDto.setFileName(savedImage.getFileName());
                imageDto.setDownloadUrl(savedImage.getDownloadUrl());
                savedImageDto.add(imageDto);
            } catch (IOException | SQLException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
        return savedImageDto;
    }

    @Override
    public void updateImage(MultipartFile file, Long imageId) {
        Image image = imageMapper.toEntity(getImageById(imageId));
        try {
            image.setFileName(file.getOriginalFilename());
            image.setFileType(file.getContentType());
            image.setImage(new SerialBlob((file.getBytes())));
            imageRepository.save(image);
        } catch (IOException | SQLException e) {
            throw new RuntimeException();
        }
    }

    @Override
    public byte[] decodeBase64ToBytes(String base64String) {
        return Base64.getDecoder().decode(base64String);
    }

}

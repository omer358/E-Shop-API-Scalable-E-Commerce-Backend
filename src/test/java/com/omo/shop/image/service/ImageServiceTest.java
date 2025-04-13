package com.omo.shop.image.service;


import com.omo.shop.common.exceptions.ResourceNotFoundException;
import com.omo.shop.image.dto.ImageDto;
import com.omo.shop.image.mapper.ImageMapper;
import com.omo.shop.image.model.Image;
import com.omo.shop.image.repository.ImageRepository;
import com.omo.shop.product.dto.ProductDto;
import com.omo.shop.product.mapper.ProductMapper;
import com.omo.shop.product.service.IProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import static com.omo.shop.common.constants.ExceptionMessages.IMAGE_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ImageServiceTest {

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private IProductService productService;

    @Mock
    private ProductMapper productMapper;

    @Mock
    private ImageMapper imageMapper;

    @InjectMocks
    private ImageService imageService;

    private Image image;
    private ImageDto imageDto;
    private ProductDto productDto;

    @BeforeEach
    void setUp() throws SQLException {

        productDto = ProductDto.builder().id(1L).build();

        image = Image.builder()
                .id(1L)
                .fileName("test.png")
                .fileType("image/png")
                .downloadUrl("/api/v1/images/image/download/1")
                .image(new SerialBlob("image-bytes".getBytes()))
                .build();

        imageDto = ImageDto.builder()
                .id(1L)
                .fileName("test.png")
                .fileType("image/png")
                .downloadUrl("/api/v1/images/image/download/1")
                .build();
    }

    @Test
    @DisplayName("Should return image DTO when image exists")
    void getImageById_shouldReturnImageDto_whenImageExists() {
        when(imageRepository.findById(1L)).thenReturn(Optional.of(image));
        when(imageMapper.toDto(image)).thenReturn(imageDto);

        ImageDto result = imageService.getImageById(1L);

        assertEquals(imageDto.getId(), result.getId());
        verify(imageRepository).findById(1L);
        verify(imageMapper).toDto(image);
    }

    @Test
    @DisplayName("Should throw exception when image not found")
    void getImageById_shouldThrowException_whenNotFound() {
        when(imageRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> imageService.getImageById(1L));

        assertEquals(IMAGE_NOT_FOUND, exception.getMessage());
    }

    @Test
    @DisplayName("Should delete image by ID")
    void deleteImageById_shouldDeleteImage() {
        imageService.deleteImageById(1L);
        verify(imageRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Should save images and return image DTOs")
    void saveImages_shouldSaveAndReturnImageDtos() throws IOException, SQLException {
        MockMultipartFile mockFile = new MockMultipartFile("image", "test.png", "image/png", "image-bytes".getBytes());

        when(productService.getProductById(1L)).thenReturn(productDto);
        when(productMapper.toEntity(productDto)).thenReturn(image.getProduct());
        when(imageRepository.save(any(Image.class))).thenReturn(image);
        when(imageMapper.toDto(any(Image.class))).thenReturn(imageDto);

        List<ImageDto> result = imageService.saveImages(List.of(mockFile), 1L);

        assertEquals(1, result.size());
        assertEquals("test.png", result.get(0).getFileName());
        verify(imageRepository, atLeastOnce()).save(any(Image.class));
    }

    @Test
    @DisplayName("Should update image successfully")
    void updateImage_shouldUpdateImageSuccessfully() throws IOException, SQLException {
        MockMultipartFile file = new MockMultipartFile("file", "updated.png", "image/png", "new-image".getBytes());

        when(imageMapper.toEntity(any())).thenReturn(image);
        when(imageRepository.save(image)).thenReturn(image);
        when(imageRepository.findById(1L)).thenReturn(Optional.of(image));

        imageService.updateImage(file, 1L);

        verify(imageRepository).save(image);
    }

    @Test
    @DisplayName("Should decode base64 string to byte array")
    void decodeBase64ToBytes_shouldDecodeCorrectly() {
        String encoded = Base64.getEncoder().encodeToString("sample".getBytes());

        byte[] result = imageService.decodeBase64ToBytes(encoded);

        assertArrayEquals("sample".getBytes(), result);
    }
}

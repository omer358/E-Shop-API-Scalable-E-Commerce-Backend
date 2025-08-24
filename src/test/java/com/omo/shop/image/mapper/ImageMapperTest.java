package com.omo.shop.image.mapper;

import com.omo.shop.image.dto.ImageDto;
import com.omo.shop.image.model.Image;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.Blob;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ImageMapperTest {

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ImageMapper imageMapper;

    private Image image;
    private ImageDto imageDto;

    @BeforeEach
    void setUp() throws SQLException {
        image = new Image();
        image.setId(1L);
        byte[] data = new byte[]{1, 2, 3};
        Blob blob = new SerialBlob(data);
        image.setImage(blob);

        imageDto = new ImageDto();
        imageDto.setId(1L);
    }

    @Test
    @DisplayName("should map Image to ImageDto")
    void toDto_WithValidImage_ShouldMapCorrectly() {
        // Arrange
        when(modelMapper.map(image, ImageDto.class)).thenReturn(imageDto);

        // Act
        ImageDto result = imageMapper.toDto(image);

        // Assert
        assertThat(result).isNotNull();
        verify(modelMapper).map(image, ImageDto.class);
    }

    @Test
    @DisplayName("should map ImageDto to Image with null image data")
    void toEntity_WithValidImageDto_ShouldMapCorrectly() {
        // Arrange
        when(modelMapper.map(imageDto, Image.class)).thenReturn(image);

        // Act
        Image result = imageMapper.toEntity(imageDto);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getImage()).isNull();
        verify(modelMapper).map(imageDto, Image.class);
    }
}
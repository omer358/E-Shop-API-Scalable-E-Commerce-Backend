package com.omo.shop.image.mapper;

import com.omo.shop.image.dto.ImageDto;
import com.omo.shop.image.model.Image;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ImageMapper {

    private final ModelMapper modelMapper;

    public ImageDto toDto(Image image) {
        return modelMapper.map(image, ImageDto.class);
    }

    public Image toEntity(ImageDto imageDto) {
        Image image = modelMapper.map(imageDto, Image.class);
        image.setImage(null);
        return image;
    }


}

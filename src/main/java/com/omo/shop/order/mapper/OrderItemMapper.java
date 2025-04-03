package com.omo.shop.order.mapper;

import com.omo.shop.order.dto.OrderItemDto;
import com.omo.shop.order.model.OrderItem;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderItemMapper {
    private final ModelMapper modelMapper;

    public OrderItemDto toDto(OrderItem order) {
        return modelMapper.map(order, OrderItemDto.class);
    }

    public OrderItem toEntity(OrderItemDto orderItemDto) {
        return modelMapper.map(orderItemDto, OrderItem.class);
    }

    public List<OrderItemDto> toDtoList(List<OrderItem> orderItems) {
        return orderItems.stream().map(this::toDto).collect(Collectors.toList());
    }
}

package com.omo.shop.order.mapper;

import com.omo.shop.order.dto.OrderDto;
import com.omo.shop.order.model.Order;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderMapper {
    private final ModelMapper modelMapper;

    public OrderDto toDto(Order order) {
        return modelMapper.map(order, OrderDto.class);
    }

    public Order toEntity(OrderDto orderDto) {
        return modelMapper.map(orderDto, Order.class);
    }

    public List<OrderDto> toDtoList(List<Order> orders) {
        return orders.stream().map(this::toDto).collect(Collectors.toList());
    }
}

package com.omo.shop.order.controller;

import com.omo.shop.common.response.ApiResponse;
import com.omo.shop.exceptions.InsufficientStockException;
import com.omo.shop.exceptions.ResourceNotFoundException;
import com.omo.shop.order.dto.OrderDto;
import com.omo.shop.order.service.IOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/orders")
public class OrderController {
    private final IOrderService orderService;

    @PostMapping("/place-order")
    public ResponseEntity<ApiResponse> createOrder(@RequestParam Long userId) {
        try {
            OrderDto orderDto = orderService.placeOrder(userId);
            return ResponseEntity.ok(new ApiResponse("Success", orderDto));
        } catch (InsufficientStockException e) {
            return ResponseEntity.status(CONFLICT)
                    .body(new ApiResponse(e.getMessage(), CONFLICT));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), NOT_FOUND));
        }
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<ApiResponse> getOrderById(@PathVariable Long orderId) {
        try {
            OrderDto order = orderService.getOrder(orderId);
            return ResponseEntity.ok(new ApiResponse("Success", order));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND)
                    .body(new ApiResponse(e.getMessage(), NOT_FOUND));
        }
    }

    @GetMapping("/{userId}/orders")
    public ResponseEntity<ApiResponse> getUserOrders(@PathVariable Long userId) {
        List<OrderDto> orderDtoList = orderService.getUserOrders(userId);
        return ResponseEntity.ok(new ApiResponse("Success", orderDtoList));
    }
}

package com.omo.shop.order.service;

import com.omo.shop.cart.model.Cart;
import com.omo.shop.cart.model.CartItem;
import com.omo.shop.cart.service.ICartService;
import com.omo.shop.common.constants.ExceptionMessages;
import com.omo.shop.common.exceptions.InsufficientStockException;
import com.omo.shop.common.exceptions.ResourceNotFoundException;
import com.omo.shop.order.dto.OrderDto;
import com.omo.shop.order.enums.OrderStatus;
import com.omo.shop.order.mapper.OrderMapper;
import com.omo.shop.order.model.Order;
import com.omo.shop.order.model.OrderItem;
import com.omo.shop.order.repository.OrderRepository;
import com.omo.shop.product.model.Product;
import com.omo.shop.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@RequiredArgsConstructor
@Service
public class OrderService implements IOrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final ICartService cartService;
    private final OrderMapper orderMapper;

    @Transactional
    @Override
    public OrderDto placeOrder(Long userId) {
        Cart cart = cartService.getCartByUserId(userId);
        Order order = createOrder(cart);
        List<OrderItem> orderItems = createOrderItems(order, cart);
        order.setOrderItems(new HashSet<>(orderItems));
        order.setTotalPrice(calculateTotalAmount(orderItems));
        Order savedOrder = orderRepository.save(order);
        cartService.clearCart(cart.getId());
        return orderMapper.toDto(savedOrder);
    }

    @Override
    public OrderDto getOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(
                        () -> new ResourceNotFoundException(ExceptionMessages.ORDER_NOT_FOUND)
                );
        return orderMapper.toDto(order);
    }

    @Override
    public List<OrderDto> getUserOrders(Long userId) {
        List<Order> orderList = orderRepository.findOrdersByUserId(userId);
        return orderMapper.toDtoList(orderList);
    }

    private Order createOrder(Cart cart) {
        return Order.builder()
                .user(cart.getUser())
                .orderStatus(OrderStatus.PENDING)
                .orderDataTime(LocalDateTime.now())
                .build();
    }

    private List<OrderItem> createOrderItems(Order order, Cart cart) {

        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem cartItem : cart.getItems()) {
            Product product = cartItem.getProduct();

            if (product.getInventory() < cartItem.getQuantity()) {
                throw new InsufficientStockException("Not enough stock for product: " + product.getName());
            }

            product.setInventory(product.getInventory() - cartItem.getQuantity());
            orderItems.add(OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantity(cartItem.getQuantity())
                    .price(cartItem.getUnitPrice())
                    .build());
        }

        productRepository.saveAll(cart.getItems().stream()
                .map(CartItem::getProduct)
                .toList());
        return orderItems;

    }

    private BigDecimal calculateTotalAmount(List<OrderItem> orderItemList) {
        return orderItemList
                .stream()
                .map(item ->
                        item.getPrice()
                                .multiply(new BigDecimal(item.getQuantity()))
                )
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}

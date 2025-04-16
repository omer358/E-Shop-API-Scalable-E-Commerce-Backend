package com.omo.shop.order.service;

import com.omo.shop.address.dto.AddressDto;
import com.omo.shop.address.model.Address;
import com.omo.shop.address.repository.AddressRepository;
import com.omo.shop.cart.model.Cart;
import com.omo.shop.cart.model.CartItem;
import com.omo.shop.cart.service.ICartService;
import com.omo.shop.common.exceptions.InsufficientStockException;
import com.omo.shop.common.exceptions.ResourceNotFoundException;
import com.omo.shop.common.exceptions.UnauthorizedAccessException;
import com.omo.shop.order.dto.OrderDto;
import com.omo.shop.order.enums.OrderStatus;
import com.omo.shop.order.mapper.OrderMapper;
import com.omo.shop.order.model.Order;
import com.omo.shop.order.model.OrderItem;
import com.omo.shop.order.repository.OrderRepository;
import com.omo.shop.product.model.Product;
import com.omo.shop.product.repository.ProductRepository;
import com.omo.shop.user.dto.UserDto;
import com.omo.shop.user.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.omo.shop.common.constants.ExceptionMessages.ORDER_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private ProductRepository productRepository;
    @Mock
    private AddressRepository addressRepository;
    @Mock
    private ICartService cartService;
    @Mock
    private OrderMapper orderMapper;
    @InjectMocks
    private OrderService orderService;

    private Product product;
    private CartItem cartItem;
    private User user;
    private UserDto userDto;
    private Cart cart;
    private Order order;
    private OrderDto orderDto;
    private Address address;
    private Address address2;
    private AddressDto addressDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        product = Product.builder()
                .id(1L)
                .name("Laptop")
                .inventory(10)
                .price(BigDecimal.valueOf(1500))
                .build();

        cartItem = CartItem.builder()
                .id(1L)
                .product(product)
                .quantity(2)
                .unitPrice(BigDecimal.valueOf(1500))
                .build();

        cart = Cart.builder()
                .id(1L)
                .items(Set.of(cartItem))
                .build();

        order = Order.builder()
                .orderId(1L)
                .user(cart.getUser())
                .orderDataTime(LocalDateTime.now())
                .orderStatus(OrderStatus.PENDING)
                .orderItems(Set.of(
                        OrderItem.builder()
                                .product(product)
                                .quantity(2)
                                .price(BigDecimal.valueOf(1500))
                                .build()
                ))
                .totalPrice(BigDecimal.valueOf(3000))
                .build();

        orderDto = OrderDto.builder()
                .orderId(1L)
                .orderStatus(OrderStatus.PENDING.toString())
                .totalPrice(BigDecimal.valueOf(3000))
                .build();

        user = User.builder()
                .id(1L)
                .firstName("Omar")
                .lastName("Dev")
                .email("omar@example.com")
                .password("encoded-pass")
                .build();

        userDto = UserDto.builder()
                .id(1L)
                .firstName("Omar")
                .lastName("Dev")
                .email("omar@example.com")
                .build();

        address = Address.builder()
                .id(1L)
                .street("Main St")
                .city("Metropolis")
                .state("Gotham")
                .country("USA")
                .zipCode("12345")
                .user(user)
                .build();

        address2 = Address.builder()
                .id(2L)
                .street("Main St")
                .city("Metropolis")
                .state("Gotham")
                .country("USA")
                .zipCode("12345")
                .user(User.builder().id(2L).build())
                .build();

        addressDto = AddressDto.builder()
                .id(1L)
                .street("Main St")
                .city("Metropolis")
                .state("Gotham")
                .country("USA")
                .zipCode("12345")
                .build();
    }

    @Test
    void placeOrder_shouldCreateOrder_whenStockIsAvailable() {
        when(cartService.getCartByUserId(1L)).thenReturn(cart);
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(addressRepository.findById(address.getId())).thenReturn(Optional.of(address));
        when(orderMapper.toDto(any(Order.class))).thenReturn(orderDto);

        OrderDto result = orderService.placeOrder(1L,1L);

        assertNotNull(result);
        assertEquals(orderDto.getTotalPrice(), result.getTotalPrice());
        verify(productRepository).saveAll(any());
        verify(cartService).clearCart(cart.getId());
    }

    @Test
    void placeOrder_shouldThrowException_whenAddressDoesHotBelongToTheUser() {
        when(cartService.getCartByUserId(1L)).thenReturn(cart);
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(addressRepository.findById(address2.getId())).thenReturn(Optional.of(address2));
        when(orderMapper.toDto(any(Order.class))).thenReturn(orderDto);

        assertThrows(UnauthorizedAccessException.class,
                ()-> orderService.placeOrder(1L,2L));

        verify(addressRepository).findById(any());
    }

    @Test
    void placeOrder_shouldThrowException_whenStockIsInsufficient() {
        product.setInventory(1); // insufficient stock

        when(cartService.getCartByUserId(1L)).thenReturn(cart);
        when(addressRepository.findById(address.getId())).thenReturn(Optional.of(address));

        assertThrows(InsufficientStockException.class,
                () -> orderService.placeOrder(1L,1L));

        verify(productRepository, never()).saveAll(any());
        verify(orderRepository, never()).save(any());
    }

    @Test
    void getOrder_shouldReturnOrderDto_whenOrderExists() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderMapper.toDto(order)).thenReturn(orderDto);

        OrderDto result = orderService.getOrder(1L);

        assertNotNull(result);
        assertEquals(orderDto.getOrderId(), result.getOrderId());
    }

    @Test
    void getOrder_shouldThrowException_whenOrderNotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> orderService.getOrder(1L));

        assertEquals(ORDER_NOT_FOUND, exception.getMessage());
    }

    @Test
    void getUserOrders_shouldReturnOrderDtoList_whenUserHasOrders() {
        List<Order> orders = List.of(order);
        List<OrderDto> orderDtos = List.of(orderDto);

        when(orderRepository.findOrdersByUserId(1L)).thenReturn(orders);
        when(orderMapper.toDtoList(orders)).thenReturn(orderDtos);

        List<OrderDto> result = orderService.getUserOrders(1L);

        assertEquals(1, result.size());
        assertEquals(orderDto.getOrderId(), result.get(0).getOrderId());
    }
}

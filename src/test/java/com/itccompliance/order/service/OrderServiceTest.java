package com.itccompliance.order.service;

import com.itccompliance.order.entity.Order;
import com.itccompliance.order.model.OrderDto;
import com.itccompliance.order.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private PdfGeneratorService pdfGeneratorService;

    @InjectMocks
    private OrderService orderService;

    private Order sampleOrder;
    private OrderDto sampleOrderDto;

    @BeforeEach
    void setUp() {
        sampleOrder = new Order(1L, "Alice", List.of("Item1", "Item2"), 199.99, "CREATED");
        sampleOrderDto = new OrderDto(1L, "Alice", List.of("Item1", "Item2"), 199.99, "CREATED");
    }

    @Test
    void createOrder_ShouldSaveAndReturnDto() {
        when(orderRepository.save(any(Order.class))).thenReturn(sampleOrder);

        OrderDto result = orderService.createOrder(sampleOrderDto);

        assertNotNull(result);
        assertEquals("Alice", result.customerName());
        verify(orderRepository).save(any(Order.class));
        verify(pdfGeneratorService).generatePdf(sampleOrder);
    }

    @Test
    void updateOrder_ShouldUpdateAndReturnDto() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(sampleOrder));
        when(orderRepository.save(any(Order.class))).thenReturn(sampleOrder);

        OrderDto updatedDto = new OrderDto(1L, "Alice Updated", List.of("Item1"), 150.0, "UPDATED");

        OrderDto result = orderService.updateOrder(1L, updatedDto);

        assertNotNull(result);
        assertEquals("Alice Updated", result.customerName());
        verify(orderRepository).findById(1L);
        verify(orderRepository).save(any(Order.class));
        verify(pdfGeneratorService).generatePdf(any(Order.class));
    }

    @Test
    void updateOrder_ShouldThrow_WhenNotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        OrderDto updatedDto = new OrderDto(1L, "Test", List.of(), 0.0, "NONE");

        assertThrows(IllegalArgumentException.class, () -> orderService.updateOrder(1L, updatedDto));
    }

    @Test
    void getOrderById_ShouldReturnDto() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(sampleOrder));

        OrderDto result = orderService.getOrderById(1L);

        assertNotNull(result);
        assertEquals("Alice", result.customerName());
    }

    @Test
    void getOrderById_ShouldThrow_WhenNotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> orderService.getOrderById(1L));
    }

    @Test
    void getAllOrders_ShouldReturnList() {
        when(orderRepository.findAll()).thenReturn(List.of(sampleOrder));

        List<OrderDto> result = orderService.getAllOrders();

        assertEquals(1, result.size());
        assertEquals("Alice", result.get(0).customerName());
    }

    @Test
    void deleteOrder_ShouldCallDeleteById() {
        doNothing().when(orderRepository).deleteById(1L);

        orderService.deleteOrder(1L);

        verify(orderRepository).deleteById(1L);
    }
}

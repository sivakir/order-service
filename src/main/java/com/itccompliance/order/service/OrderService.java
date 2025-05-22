package com.itccompliance.order.service;

import com.itccompliance.order.entity.Order;
import com.itccompliance.order.model.OrderDto;
import com.itccompliance.order.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final PdfGeneratorService pdfGeneratorService;

    public OrderService(OrderRepository orderRepository, PdfGeneratorService pdfGeneratorService){
        this.orderRepository = orderRepository;
        this.pdfGeneratorService = pdfGeneratorService;
    }

    public OrderDto createOrder(OrderDto dto) {
        logger.info("Creating new order for customer: {}", dto.customerName());
        Order saved = orderRepository.save(toEntity(dto));
        pdfGeneratorService.generatePdf(saved);
        return toDto(saved);
    }

    public OrderDto updateOrder(Long id, OrderDto dto) {
        logger.info("Updating order with ID {}", id);
        Order existing = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + id));
        existing.setCustomerName(dto.customerName());
        existing.setItems(dto.items());
        existing.setTotalPrice(dto.totalPrice());
        existing.setStatus(dto.status());
        Order updated = orderRepository.save(existing);
        pdfGeneratorService.generatePdf(updated);
        return toDto(updated);
    }

    public OrderDto getOrderById(Long id) {
        logger.info("Fetching order with ID {}", id);
        return toDto(orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + id)));
    }

    public List<OrderDto> getAllOrders() {
        logger.info("Listing all orders");
        return orderRepository.findAll().stream().map(this::toDto).toList();
    }

    public void deleteOrder(Long id) {
        logger.info("Deleting order with ID {}", id);
        orderRepository.deleteById(id);
    }

    private Order toEntity(OrderDto dto) {
        return new Order(
                dto.id(),
                dto.customerName(),
                dto.items(),
                dto.totalPrice(),
                dto.status()
        );
    }

    private OrderDto toDto(Order order) {
        return new OrderDto(
                order.getId(),
                order.getCustomerName(),
                order.getItems(),
                order.getTotalPrice(),
                order.getStatus()
        );
    }
}

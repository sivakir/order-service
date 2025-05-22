package com.itccompliance.order.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itccompliance.order.model.OrderDto;
import com.itccompliance.order.repository.OrderRepository;
import com.itccompliance.order.service.OrderService;
import com.itccompliance.order.service.PdfGeneratorService;
import com.itccompliance.order.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
@Import({OrderControllerTest.MockOrderServiceConfig.class, OrderControllerTest.TestSecurityConfig.class})

class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrderService orderService;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private OrderDto sampleOrder;

    @BeforeEach
    void setUp() {
        sampleOrder = new OrderDto(1L, "Alice", List.of("Item1", "Item2"), 199.99, "CREATED");
    }

    @Test
    @DisplayName("Create Order - Should Return OK")
    @WithMockUser(roles = "USER")
    void createOrder_ShouldReturnOk() throws Exception {
        when(orderService.createOrder(any(OrderDto.class))).thenReturn(sampleOrder);

        mockMvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleOrder)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerName").value("Alice"));
    }

    @Test
    @DisplayName("Get Order by ID - Should Return Order")
    @WithMockUser(roles = "ADMIN")
    void getOrderById_ShouldReturnOrder() throws Exception {
        when(orderService.getOrderById(1L)).thenReturn(sampleOrder);

        mockMvc.perform(get("/api/v1/orders/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.customerName").value("Alice"));
    }

    @Test
    @DisplayName("List All Orders - Should Return List")
    @WithMockUser(roles = "ADMIN")
    void listOrders_ShouldReturnList() throws Exception {
        when(orderService.getAllOrders()).thenReturn(List.of(sampleOrder));

        mockMvc.perform(get("/api/v1/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].customerName").value("Alice"));
    }

    @Test
    @DisplayName("Delete Order - Should Return No Content")
    @WithMockUser(roles = "ADMIN")
    void deleteOrder_ShouldReturnNoContent() throws Exception {
        doNothing().when(orderService).deleteOrder(1L);

        mockMvc.perform(delete("/api/v1/orders/1"))
                .andExpect(status().isNoContent());
    }

    // Local test configuration for mocking the service
    @TestConfiguration
    static class MockOrderServiceConfig {
        @Bean
        public OrderService orderService() {
            return Mockito.mock(OrderService.class);
        }
        @Bean
        public OrderRepository orderRepository() {
            return Mockito.mock(OrderRepository.class);
        }
        @Bean
        public PdfGeneratorService pdfGeneratorService() {
            return Mockito.mock(PdfGeneratorService.class);
        }
        @Bean
        public JwtUtil jwtUtil() {
            return new JwtUtil();
        }
    }
    @TestConfiguration
    static class TestSecurityConfig {

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http.csrf().disable()
                    .authorizeHttpRequests().anyRequest().permitAll();
            return http.build();
        }

        @Bean
        public UserDetailsService userDetailsService() {
            return username -> User.builder()
                    .username("testuser")
                    .password("testpass")
                    .roles("USER", "ADMIN")
                    .build();
        }
    }
}

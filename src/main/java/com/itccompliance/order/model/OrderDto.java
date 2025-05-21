package com.itccompliance.order.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record OrderDto(

        Long id,

        @NotEmpty(message = "Customer name must not be empty")
        String customerName,

        @NotNull(message = "Items list must not be null")
        List<@NotEmpty(message = "Item name must not be empty") String> items,

        @Min(value = 0, message = "Total price must be non-negative")
        double totalPrice,

        @NotEmpty(message = "Status must not be empty")
        String status

) {}

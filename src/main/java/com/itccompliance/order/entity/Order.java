package com.itccompliance.order.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.util.List;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String customerName;

    @ElementCollection
    private List<String> items;

    @Positive
    private double totalPrice;

    @NotBlank
    private String status;
    public Order(){

    }
    public Order(Long id, String customerName, List<String> items, double totalPrice, String status) {
        this.id = id;
        this.customerName = customerName;
        this.items = items;
        this.totalPrice = totalPrice;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public @NotBlank String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(@NotBlank String customerName) {
        this.customerName = customerName;
    }

    public List<String> getItems() {
        return items;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }

    @Positive
    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(@Positive double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public @NotBlank String getStatus() {
        return status;
    }

    public void setStatus(@NotBlank String status) {
        this.status = status;
    }

}

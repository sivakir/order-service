package com.itccompliance.order.repository;

import com.itccompliance.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    // No additional methods needed for basic CRUD
}

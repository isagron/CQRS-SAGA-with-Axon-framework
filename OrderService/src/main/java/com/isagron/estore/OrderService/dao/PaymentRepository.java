package com.isagron.estore.OrderService.dao;

import com.isagron.estore.OrderService.domain.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<PaymentEntity, String> {
}

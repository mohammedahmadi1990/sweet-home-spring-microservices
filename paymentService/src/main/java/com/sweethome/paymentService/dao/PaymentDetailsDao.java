package com.sweethome.paymentService.dao;

import com.sweethome.paymentService.entity.PaymentDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentDetailsDao extends JpaRepository<PaymentDetailsEntity, Integer> {
}

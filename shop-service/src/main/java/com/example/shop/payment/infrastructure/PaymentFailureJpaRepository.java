package com.example.shop.payment.infrastructure;

import com.example.shop.payment.domain.PaymentFailure;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentFailureJpaRepository extends JpaRepository<PaymentFailure, UUID> {

}

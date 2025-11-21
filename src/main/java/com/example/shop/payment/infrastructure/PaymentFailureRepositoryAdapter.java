package com.example.shop.payment.infrastructure;

import com.example.shop.payment.domain.PaymentFailure;
import com.example.shop.payment.domain.PaymentFailureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PaymentFailureRepositoryAdapter implements PaymentFailureRepository {

    private final PaymentFailureJpaRepository failureJpaRepository;


    @Override
    public PaymentFailure save(PaymentFailure failure) {
        return failureJpaRepository.save(failure);
    }
}

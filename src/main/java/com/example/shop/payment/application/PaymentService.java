package com.example.shop.payment.application;

import com.example.shop.common.ResponseEntity;
import com.example.shop.payment.application.dto.PaymentCommand;
import com.example.shop.payment.application.dto.PaymentFailureCommand;
import com.example.shop.payment.application.dto.PaymentFailureInfo;
import com.example.shop.payment.application.dto.PaymentInfo;
import com.example.shop.payment.client.TossPaymentClient;
import com.example.shop.payment.client.dto.TossPaymentResponse;
import com.example.shop.payment.domain.Payment;
import com.example.shop.payment.domain.PaymentFailure;
import com.example.shop.payment.domain.PaymentFailureRepository;
import com.example.shop.payment.domain.PaymentRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentFailureRepository paymentFailureRepository;

    private final TossPaymentClient tossPaymentClient;


    public ResponseEntity<List<PaymentInfo>> findAll(Pageable pageable) {
        Page<Payment> page = paymentRepository.findAll(pageable);
        List<PaymentInfo> payments = page.stream()
            .map(PaymentInfo::from)
            .toList();
        return new ResponseEntity<>(HttpStatus.OK.value(), payments, page.getTotalElements());
    }


    public ResponseEntity<PaymentInfo> confirm(PaymentCommand command) {
        TossPaymentResponse tossPayment = tossPaymentClient.confirm(command);
        Payment payment = Payment.create(tossPayment.paymentKey(), tossPayment.orderId(),
            tossPayment.totalAmount());

        LocalDateTime approvedAt =
            tossPayment.approvedAt() != null ? tossPayment.approvedAt().toLocalDateTime() : null;
        LocalDateTime requestedAt =
            tossPayment.requestedAt() != null ? tossPayment.requestedAt().toLocalDateTime() : null;
        payment.markConfirmed(tossPayment.method(), approvedAt, requestedAt);

        paymentRepository.save(payment);

        return new ResponseEntity<>(HttpStatus.OK.value(), PaymentInfo.from(payment), 1);
    }

    public ResponseEntity<PaymentFailureInfo> recordFailure(PaymentFailureCommand command) {

        PaymentFailure failure = PaymentFailure.from(
            command.orderId(),
            command.paymentKey(),
            command.errorCode(),
            command.errorMessage(),
            command.amount(),
            command.rawPayload()
        );
        PaymentFailure saved = paymentFailureRepository.save(failure);
        return new ResponseEntity<>(HttpStatus.OK.value(), PaymentFailureInfo.from(saved), 1);
    }
}

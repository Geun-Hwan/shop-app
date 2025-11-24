package com.example.shop.payment.client;

import com.example.shop.payment.application.dto.PaymentCommand;
import com.example.shop.payment.client.dto.TossPaymentResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class TossPaymentClient {

    private static final String CONFIRM_URL = "https://api.tosspayments.com/v1/payments/confirm";

    private final RestTemplate restTemplate;

    @Value("${payment.toss.secret-key}")
    private String secretKey;


    public TossPaymentResponse confirm(PaymentCommand command) {
        if (!StringUtils.hasText(secretKey)) {
            throw new IllegalStateException("Toss secret key is not configure");
        }

        HttpHeaders headers = createHeaders();

        Map<String, Object> body = new HashMap<>();
        body.put("paymentKey", command.paymentKey());
        body.put("orderId", command.orderId());
        body.put("amount", command.amount());

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        return restTemplate.postForObject(CONFIRM_URL, entity, TossPaymentResponse.class);
    }


    private HttpHeaders createHeaders() {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String auth = secretKey + ":";
        String encoded = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));

        headers.set(HttpHeaders.AUTHORIZATION, "Basic " + encoded);

        return headers;

    }

}

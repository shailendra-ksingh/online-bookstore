package com.bookstore.service.impl;

import com.bookstore.dto.payment.PaymentMethod;
import com.bookstore.dto.payment.PaymentResult;
import com.bookstore.service.PaymentStrategy;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Component
public class UpiPaymentStrategy implements PaymentStrategy {

    @Override
    public PaymentMethod getPaymentMethod() {
        return PaymentMethod.UPI;
    }

    @Override
    public PaymentResult process(BigDecimal amount) {
        return new PaymentResult(
                true,
                "UPI-" + UUID.randomUUID(),
                "UPI payment successful"
        );
    }
}

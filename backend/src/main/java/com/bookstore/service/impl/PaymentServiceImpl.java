package com.bookstore.service.impl;

import com.bookstore.dto.payment.PaymentResult;
import com.bookstore.service.PaymentService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Override
    public PaymentResult processPayment(BigDecimal amount) {

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            return new PaymentResult(
                    false,
                    null,
                    "Payment amount must be greater than zero"
            );
        }

        // In a real scenario, the payment gateway API
        // such as Razorpay, or another provider need to be called.
        String transactionId = UUID.randomUUID().toString();

        return new PaymentResult(
                true,
                transactionId,
                "Payment successful"
        );
    }
}
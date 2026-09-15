package com.bookstore.service.impl;

import com.bookstore.dto.payment.PaymentMethod;
import com.bookstore.dto.payment.PaymentResult;
import com.bookstore.service.PaymentService;
import com.bookstore.service.PaymentStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class PaymentServiceImpl implements PaymentService {

    private final Map<PaymentMethod, PaymentStrategy> strategies;

    public PaymentServiceImpl(List<PaymentStrategy> strategies) {
        this.strategies = new EnumMap<>(PaymentMethod.class);
        strategies.forEach(strategy ->
                this.strategies.put(strategy.getPaymentMethod(), strategy));
    }

    @Override
    public PaymentResult processPayment(
            BigDecimal amount,
            PaymentMethod method) {

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            return new PaymentResult(
                    false,
                    null,
                    "Payment amount must be greater than zero"
            );
        }

        PaymentStrategy strategy = strategies.get(method);
        if (strategy == null) {
            throw new IllegalArgumentException(
                    "Unsupported payment method: " + method
            );
        }

        log.info("Processing {} payment for amount={}", method, amount);
        return strategy.process(amount);
    }
}

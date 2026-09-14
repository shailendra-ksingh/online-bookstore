package com.bookstore.service;

import com.bookstore.dto.payment.PaymentResult;

import java.math.BigDecimal;

public interface PaymentService {

    PaymentResult processPayment(BigDecimal amount);
}

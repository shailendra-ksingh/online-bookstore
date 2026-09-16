package com.bookstore.service;

import com.bookstore.dto.payment.PaymentMethod;
import com.bookstore.dto.payment.PaymentResult;

import java.math.BigDecimal;

public interface PaymentStrategy {

    PaymentMethod getPaymentMethod();

    PaymentResult process(BigDecimal amount);
}

package com.bookstore.dto.payment;

public record PaymentResult(
        boolean successful,
        String transactionId,
        String message
) {
}
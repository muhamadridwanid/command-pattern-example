package com.cloudhebat.example.command;

import com.cloudhebat.example.dto.PaymentRequest;

public interface PaymentCommand {
    void process(PaymentRequest request);
    void refund(PaymentRequest request); // Untuk Rollback
    Class<?> getValidationGroup();       // Untuk Validasi Dinamis
}

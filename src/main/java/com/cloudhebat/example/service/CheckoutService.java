package com.cloudhebat.example.service;

import com.cloudhebat.example.dto.PaymentRequest;

public interface CheckoutService {
    void processCheckout(PaymentRequest paymentRequest);
}

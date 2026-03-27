package com.cloudhebat.example.controller;

import com.cloudhebat.example.dto.PaymentRequest;
import com.cloudhebat.example.service.CheckoutService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class PaymentController {
    private final CheckoutService checkoutService;

    @PostMapping("/checkout")
    public String checkout(@RequestBody @Valid PaymentRequest req) {
        checkoutService.processCheckout(req);
        return "Transaksi Berhasil!";
    }
}
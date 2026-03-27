package com.cloudhebat.example.service;

import com.cloudhebat.example.command.PaymentCommand;
import com.cloudhebat.example.dto.PaymentRequest;
import com.cloudhebat.example.exception.CustomValidationException;
import com.cloudhebat.example.executor.PaymentMacroExecutor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.*;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class CheckoutServiceImpl implements CheckoutService {
    private final Map<String, PaymentCommand> commandMap;
    private final SmartValidator validator;

    @Override
    public void processCheckout(PaymentRequest request) {
        // 1. Get Main Command
        PaymentCommand payment = commandMap.get(request.getMethod().toUpperCase());
        if (payment == null) throw new RuntimeException("Metode tidak ditemukan!");

        // 2. Validasi Dinamis
        BindingResult bindingResult = new BeanPropertyBindingResult(request, "paymentRequest");
        validator.validate(request, bindingResult, payment.getValidationGroup());
        if (bindingResult.hasErrors()) {
            throw new CustomValidationException(bindingResult);
        }

        // 3. Eksekusi Berantai dengan Rollback
        PaymentMacroExecutor macro = new PaymentMacroExecutor(request);
        macro.add(payment);                       // Eksekusi Bayar
        macro.add(commandMap.get("INVENTORY"));   // Eksekusi Stok

        macro.execute();
    }
}

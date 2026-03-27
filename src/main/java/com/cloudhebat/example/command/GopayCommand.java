package com.cloudhebat.example.command;

import com.cloudhebat.example.config.ValidationGroups.*;
import com.cloudhebat.example.dto.PaymentRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component("GOPAY")
public class GopayCommand implements PaymentCommand {
    @Override
    public void process(PaymentRequest request) {
        log.info("[GOPAY] Memotong saldo: {}", request.getPhoneNumber());
    }

    @Override
    public void refund(PaymentRequest request) {
        log.info("[GOPAY] Refund saldo ke Gojek API...");
    }

    @Override
    public Class<?> getValidationGroup() {
        return OnGopay.class;
    }
}

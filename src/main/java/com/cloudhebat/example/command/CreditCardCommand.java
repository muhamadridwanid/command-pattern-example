package com.cloudhebat.example.command;

import com.cloudhebat.example.dto.PaymentRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import com.cloudhebat.example.config.ValidationGroups.*;


@Slf4j
@Component("CREDIT_CARD")
public class CreditCardCommand implements PaymentCommand {
    @Override
    public void process(PaymentRequest request) {
        log.info("[CC] Mengunci limit kartu {}", request.getCardNumber());
    }

    @Override
    public void refund(PaymentRequest request) {
        log.info("[CC] Membatalkan limit (Void) kartu...");
    }

    @Override
    public Class<?> getValidationGroup() {
        return OnCreditCard.class;
    }
}

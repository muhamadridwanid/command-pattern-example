package com.cloudhebat.example.dto;

import com.cloudhebat.example.config.ValidationGroups.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PaymentRequest {
    @NotBlank(message = "Metode harus diisi")
    private String method; // Contoh: "GOPAY", "CREDIT_CARD"

    @Min(value = 1000, message = "Minimal Rp1.000")
    private double amount;

    //Khusus CC
    @NotBlank(groups = OnCreditCard.class, message = "Nomor kartu wajib")
    private String cardNumber;

    @NotBlank(groups = OnCreditCard.class, message = "CVV wajib")
    private String cvv;

    //Khusus gopay
    @NotBlank(groups = OnGopay.class, message = "Nomor HP wajib")
    private String phoneNumber;
}

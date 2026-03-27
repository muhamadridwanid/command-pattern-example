package com.cloudhebat.example.command;

import com.cloudhebat.example.dto.PaymentRequest;
import com.cloudhebat.example.config.ValidationGroups.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component("INVENTORY")
public class InventoryCommand implements PaymentCommand {
    @Override
    public void process(PaymentRequest request) {
        log.info("[STOK] Mengurangi stok di database...");
        // Simulasi error: jika stok habis, lempar exception untuk trigger rollback
        // log.error("[STOK] Stok Habis!");
        // throw new RuntimeException("Stok Habis!");
    }

    @Override
    public void refund(PaymentRequest request) {
        log.info("[STOK] Menambah kembali stok (Restock)...");
    }

    @Override
    public Class<?> getValidationGroup() {
        return OnInventory.class;
    }
}

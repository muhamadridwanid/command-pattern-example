package com.cloudhebat.example;

import com.cloudhebat.example.command.GopayCommand;
import com.cloudhebat.example.command.InventoryCommand;
import com.cloudhebat.example.command.PaymentCommand;
import com.cloudhebat.example.dto.PaymentRequest;
import com.cloudhebat.example.executor.PaymentMacroExecutor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Stack;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class CommandPatternExampleApplicationTests {

    @Test
    void testRollbackWhenInventoryFails() {
        // 1. MOCKING: Kita buat tiruan dari Command agar bisa kita kontrol hasilnya
        PaymentCommand gopay = mock(GopayCommand.class);
        PaymentCommand inventory = mock(InventoryCommand.class);

        PaymentRequest request = new PaymentRequest();
        request.setMethod("GOPAY");
        request.setAmount(50000);

        // 2. SCENARIO:
        // Gopay sukses, tapi Inventory sengaja kita buat ERROR
        doNothing().when(gopay).process(request);
        doThrow(new RuntimeException("Stok Habis!")).when(inventory).process(request);

        // 3. EXECUTION: Masukkan ke Macro Executor
        PaymentMacroExecutor macro = new PaymentMacroExecutor(request);
        macro.add(gopay);
        macro.add(inventory);

        // Pastikan exception dilempar ke atas
        assertThrows(RuntimeException.class, () -> {
            macro.execute();
        });

        // 4. VERIFICATION (The Magic Part):
        // Pastikan Gopay dijalankan (process)
        verify(gopay, times(1)).process(request);

        // Pastikan Inventory dijalankan (dan gagal)
        verify(inventory, times(1)).process(request);

        // PEMBUKTIAN ROLLBACK:
        // Karena Inventory gagal, Gopay HARUS di-refund otomatis.
        // Tapi Inventory TIDAK BOLEH di-refund karena dia belum sempat sukses (belum masuk history stack).
        verify(gopay, times(1)).refund(request);
        verify(inventory, times(0)).refund(request);

        System.out.println("Test Passed: Uang kembali karena stok gagal!");
    }
}
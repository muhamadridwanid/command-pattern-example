package com.cloudhebat.example.executor;

import com.cloudhebat.example.command.PaymentCommand;
import com.cloudhebat.example.dto.PaymentRequest;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

@Slf4j
public class PaymentMacroExecutor {
    private final List<PaymentCommand> commands = new ArrayList<>();
    private final Stack<PaymentCommand> history = new Stack<>();
    private final PaymentRequest request;

    public PaymentMacroExecutor(PaymentRequest request) {
        this.request = request;
    }

    public void add(PaymentCommand command) {
        if (command != null) commands.add(command);
    }

    public void execute() {
        try {
            for (PaymentCommand cmd : commands) {
                cmd.process(request);
                history.push(cmd);
            }
        } catch (Exception e) {
            rollback();
            throw e;
        }
    }

    private void rollback() {
        log.error("Terjadi kesalahan. Memulai proses Rollback...");
        while (!history.isEmpty()) {
            history.pop().refund(request);
        }
    }
}
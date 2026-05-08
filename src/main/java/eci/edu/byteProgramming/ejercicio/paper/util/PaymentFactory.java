package eci.edu.byteProgramming.ejercicio.paper.util;

public interface PaymentFactory {
    PaymentMethod createPaymentMethod(double amount, String customerId, String description);
}


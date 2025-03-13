package com.assignment.merabills.data.model;

import java.util.ArrayList;
import java.util.List;

public class PaymentData {

    private List<Payment> payments;

    public PaymentData() {
        payments = new ArrayList<>();
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

    public double getTotalAmount() {
        double total = 0;
        for (Payment payment : payments) {
            total += payment.getAmount();
        }
        return total;
    }

    public boolean hasPaymentType(PaymentType type) {
        for (Payment payment : payments) {
            if (payment.getType() == type) {
                return true;
            }
        }
        return false;
    }

    public List<PaymentType> getAvailablePaymentTypes() {
        List<PaymentType> availableTypes = new ArrayList<>();

        for (PaymentType type : PaymentType.values()) {
            if (!hasPaymentType(type)) {
                availableTypes.add(type);
            }
        }

        return availableTypes;
    }
}
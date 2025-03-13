package com.assignment.merabills.data.repository;

import androidx.lifecycle.LiveData;

import com.assignment.merabills.data.model.Payment;
import com.assignment.merabills.data.model.PaymentData;
import com.assignment.merabills.data.model.PaymentType;

import java.util.List;

public interface PaymentRepository {
    LiveData<PaymentData> getPaymentData();

    void addPayment(Payment payment);

    void removePayment(PaymentType type);

    boolean savePayments();

    void loadPayments();

    boolean hasPaymentType(PaymentType type);

    List<PaymentType> getAvailablePaymentTypes();
}
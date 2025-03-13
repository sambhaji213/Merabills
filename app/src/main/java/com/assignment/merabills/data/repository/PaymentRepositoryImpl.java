package com.assignment.merabills.data.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.assignment.merabills.data.model.Payment;
import com.assignment.merabills.data.model.PaymentData;
import com.assignment.merabills.data.model.PaymentType;
import com.assignment.merabills.data.storage.FileStorageHelper;

import java.util.List;

public class PaymentRepositoryImpl implements PaymentRepository {
    private final FileStorageHelper fileStorageHelper;
    private final MutableLiveData<PaymentData> paymentDataLiveData;

    public PaymentRepositoryImpl(FileStorageHelper fileStorageHelper) {
        this.fileStorageHelper = fileStorageHelper;
        this.paymentDataLiveData = new MutableLiveData<>(new PaymentData());
    }

    @Override
    public LiveData<PaymentData> getPaymentData() {
        return paymentDataLiveData;
    }

    @Override
    public void addPayment(Payment payment) {
        PaymentData currentData = paymentDataLiveData.getValue();
        if (currentData != null) {
            // First check if payment type already exists, if so remove it (to update)
            currentData.getPayments().removeIf(p -> p.getType() == payment.getType());
            // Add the new payment
            currentData.getPayments().add(payment);
            paymentDataLiveData.setValue(currentData);
        }
    }

    @Override
    public void removePayment(PaymentType type) {
        PaymentData currentData = paymentDataLiveData.getValue();
        if (currentData != null) {
            currentData.getPayments().removeIf(p -> p.getType() == type);
            paymentDataLiveData.setValue(currentData);
        }
    }

    @Override
    public boolean savePayments() {
        PaymentData currentData = paymentDataLiveData.getValue();
        if (currentData != null) {
            return fileStorageHelper.savePaymentData(currentData);
        }
        return false;
    }

    @Override
    public void loadPayments() {
        PaymentData loadedData = fileStorageHelper.loadPaymentData();
        if (loadedData != null) {
            paymentDataLiveData.setValue(loadedData);
        }
    }

    @Override
    public boolean hasPaymentType(PaymentType type) {
        PaymentData currentData = paymentDataLiveData.getValue();
        return currentData != null && currentData.hasPaymentType(type);
    }

    @Override
    public List<PaymentType> getAvailablePaymentTypes() {
        PaymentData currentData = paymentDataLiveData.getValue();
        if (currentData != null) {
            return currentData.getAvailablePaymentTypes();
        }
        return List.of(PaymentType.values());
    }
}
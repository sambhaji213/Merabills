package com.assignment.merabills.ui.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.assignment.merabills.data.model.Payment;
import com.assignment.merabills.data.model.PaymentData;
import com.assignment.merabills.data.model.PaymentType;
import com.assignment.merabills.data.repository.PaymentRepository;

import java.util.List;

public class PaymentViewModel extends ViewModel {

    private final PaymentRepository repository;
    private final MediatorLiveData<Double> totalAmountLiveData;

    public PaymentViewModel(PaymentRepository repository) {
        this.repository = repository;
        this.totalAmountLiveData = new MediatorLiveData<>();

        loadSavedPayments();

        this.totalAmountLiveData.addSource(repository.getPaymentData(), paymentData -> {
            if (paymentData != null) {
                totalAmountLiveData.setValue(paymentData.getTotalAmount());
            } else {
                totalAmountLiveData.setValue(0.0);
            }
        });
    }

    public LiveData<PaymentData> getPaymentData() {
        return repository.getPaymentData();
    }

    public LiveData<Double> getTotalAmount() {
        return totalAmountLiveData;
    }

    public void addPayment(Payment payment) {
        repository.addPayment(payment);
    }

    public void removePayment(PaymentType type) {
        repository.removePayment(type);
    }

    public boolean savePayments() {
        return repository.savePayments();
    }

    public void loadSavedPayments() {
        repository.loadPayments();
    }

    public boolean hasPaymentType(PaymentType type) {
        return repository.hasPaymentType(type);
    }

    public List<PaymentType> getAvailablePaymentTypes() {
        return repository.getAvailablePaymentTypes();
    }
}
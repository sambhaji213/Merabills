package com.assignment.merabills.data.storage;

import com.assignment.merabills.data.model.PaymentData;

public interface FileStorageHelper {

    boolean savePaymentData(PaymentData data);

    PaymentData loadPaymentData();
}
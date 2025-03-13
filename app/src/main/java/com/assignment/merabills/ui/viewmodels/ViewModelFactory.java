package com.assignment.merabills.ui.viewmodels;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.assignment.merabills.data.repository.PaymentRepository;
import com.assignment.merabills.data.repository.PaymentRepositoryImpl;
import com.assignment.merabills.data.storage.FileStorageHelper;
import com.assignment.merabills.data.storage.FileStorageHelperImpl;

public class ViewModelFactory implements ViewModelProvider.Factory {
    private final Context context;

    public ViewModelFactory(Context context) {
        this.context = context.getApplicationContext();
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(PaymentViewModel.class)) {
            FileStorageHelper fileStorageHelper = new FileStorageHelperImpl(context);
            PaymentRepository repository = new PaymentRepositoryImpl(fileStorageHelper);
            return (T) new PaymentViewModel(repository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class: " + modelClass.getName());
    }
}
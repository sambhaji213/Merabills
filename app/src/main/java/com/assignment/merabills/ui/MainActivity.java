package com.assignment.merabills.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.assignment.merabills.R;
import com.assignment.merabills.data.model.Payment;
import com.assignment.merabills.data.model.PaymentData;
import com.assignment.merabills.data.model.PaymentType;
import com.assignment.merabills.ui.adapters.PaymentChipAdapter;
import com.assignment.merabills.ui.dialogs.AddPaymentDialog;
import com.assignment.merabills.ui.viewmodels.PaymentViewModel;
import com.assignment.merabills.ui.viewmodels.ViewModelFactory;
import com.assignment.merabills.utils.CurrencyFormatter;
import com.assignment.merabills.utils.ToastUtils;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AddPaymentDialog.PaymentAddedListener, PaymentChipAdapter.OnPaymentDeletedListener {

    private PaymentViewModel viewModel;
    private PaymentChipAdapter chipAdapter;

    private TextView tvAddPayment;
    private RecyclerView rvPayments;
    private TextView tvTotalAmount;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewModelFactory factory = new ViewModelFactory(this);
        viewModel = new ViewModelProvider(this, factory).get(PaymentViewModel.class);

        initViews();
        initListeners();
        initViewModel();
    }

    private void initViews() {
        tvAddPayment = findViewById(R.id.tvAddPayment);
        rvPayments = findViewById(R.id.rvPayments);
        tvTotalAmount = findViewById(R.id.tvTotalAmount);
        btnSave = findViewById(R.id.btnSave);

        chipAdapter = new PaymentChipAdapter(new ArrayList<>(), this);
        rvPayments.setAdapter(chipAdapter);
    }

    private void initListeners() {
        tvAddPayment.setOnClickListener(v -> showAddPaymentDialog());

        btnSave.setOnClickListener(v -> {
            boolean saved = viewModel.savePayments();
            if (saved) {
                ToastUtils.showToast(this, getString(R.string.payment_details_saved_successfully));
            } else {
                ToastUtils.showToast(this, getString(R.string.failed_to_save_payment_details));
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void initViewModel() {
        viewModel.getPaymentData().observe(this, this::updatePaymentDisplay);

        viewModel.getTotalAmount().observe(this, totalAmount -> {
            if (totalAmount != null) {
                tvTotalAmount.setText(getString(R.string.rupee) + " " + CurrencyFormatter.formatAsRupees(totalAmount));
            } else {
                tvTotalAmount.setText(getString(R.string.rupee) + " " + CurrencyFormatter.formatAsRupees(0));
            }
        });
    }

    private void updatePaymentDisplay(PaymentData paymentData) {
        if (paymentData != null) {
            chipAdapter.updateList(paymentData.getPayments());
            updateAddPaymentVisibility(!paymentData.getAvailablePaymentTypes().isEmpty());
        } else {
            chipAdapter.updateList(null);
            updateAddPaymentVisibility(true);
        }
    }

    private void updateAddPaymentVisibility(boolean hasAvailableTypes) {
        tvAddPayment.setVisibility(hasAvailableTypes ? View.VISIBLE : View.GONE);
    }

    private void showAddPaymentDialog() {
        AddPaymentDialog dialog = AddPaymentDialog.newInstance(viewModel.getAvailablePaymentTypes());
        dialog.setPaymentAddedListener(this);
        dialog.show(getSupportFragmentManager(), dialog.getTag());
    }

    @Override
    public void onPaymentSave(Payment payment) {
        viewModel.addPayment(payment);
    }

    @Override
    public void onPaymentDeleted(PaymentType type) {
        viewModel.removePayment(type);
    }
}
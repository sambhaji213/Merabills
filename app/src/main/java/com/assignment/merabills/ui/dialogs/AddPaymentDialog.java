package com.assignment.merabills.ui.dialogs;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

import com.assignment.merabills.R;
import com.assignment.merabills.data.model.Payment;
import com.assignment.merabills.data.model.PaymentType;
import com.assignment.merabills.utils.ToastUtils;

import java.util.List;

public class AddPaymentDialog extends DialogFragment {

    private EditText etAmount;
    private Spinner spPaymentType;
    private ConstraintLayout clAdditionalDetails;
    private EditText etProvider;
    private EditText etTransactionReference;
    private AppCompatButton btnOk;
    private AppCompatButton btnCancel;

    private List<PaymentType> paymentTypes;
    private PaymentAddedListener listener;

    public static AddPaymentDialog newInstance(List<PaymentType> paymentTypes) {
        AddPaymentDialog dialog = new AddPaymentDialog();
        dialog.paymentTypes = paymentTypes;
        return dialog;
    }

    public void setPaymentAddedListener(PaymentAddedListener listener) {
        this.listener = listener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.AppDialog);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_add_payment, container, false);

        initViews(view);
        initListeners();

        return view;
    }

    private void initListeners() {
        spPaymentType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position >= 0 && position < paymentTypes.size()) {
                    PaymentType selectedType = paymentTypes.get(position);
                    clAdditionalDetails.setVisibility((selectedType == PaymentType.BANK_TRANSFER ||
                            selectedType == PaymentType.CREDIT_CARD) ? View.VISIBLE : View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                clAdditionalDetails.setVisibility(View.GONE);
            }
        });

        btnOk.setOnClickListener(v -> {
            if (validateInputDetails()) {
                savePaymentDetails();
            }
        });

        btnCancel.setOnClickListener(v -> dismiss());
    }

    private void savePaymentDetails() {
        try {
            double amount = Double.parseDouble(etAmount.getText().toString().trim());
            int selectedPosition = spPaymentType.getSelectedItemPosition();
            PaymentType selectedType = paymentTypes.get(selectedPosition);

            Payment payment;

            if (selectedType == PaymentType.BANK_TRANSFER || selectedType == PaymentType.CREDIT_CARD) {
                String provider = etProvider.getText().toString().trim();
                String transactionRef = etTransactionReference.getText().toString().trim();
                payment = new Payment(selectedType, amount, provider, transactionRef);
            } else {
                payment = new Payment(selectedType, amount);
            }

            if (listener != null) {
                listener.onPaymentSave(payment);
            }

            dismiss();
        } catch (Exception e) {
            ToastUtils.showToast(requireContext(), getString(R.string.error_adding_payment));
        }
    }

    private boolean validateInputDetails() {
        if (!validateAmount()) {
            etAmount.setError(getString(R.string.please_enter_valid_amount));
            etAmount.requestFocus();
            return false;
        }

        int selectedPosition = spPaymentType.getSelectedItemPosition();
        if (selectedPosition < 0 || selectedPosition >= paymentTypes.size()) {
            ToastUtils.showToast(requireContext(), getString(R.string.please_select_payment_type));
            return false;
        }

        PaymentType selectedType = paymentTypes.get(selectedPosition);
        if (selectedType == PaymentType.BANK_TRANSFER || selectedType == PaymentType.CREDIT_CARD) {
            String provider = etProvider.getText().toString().trim();
            String transactionRef = etTransactionReference.getText().toString().trim();

            if (TextUtils.isEmpty(provider)) {
                etProvider.setError(getString(R.string.please_enter_provider_name));
                etProvider.requestFocus();
                return false;
            }

            if (TextUtils.isEmpty(transactionRef)) {
                etTransactionReference.setError(getString(R.string.please_enter_transaction_reference));
                etTransactionReference.requestFocus();
                return false;
            }
        }

        return true;
    }

    private boolean validateAmount() {
        String amountText = etAmount.getText().toString().trim();
        boolean isValid = !TextUtils.isEmpty(amountText) && TextUtils.isDigitsOnly(amountText.replace(".", ""));

        if (isValid) {
            try {
                double amount = Double.parseDouble(amountText);
                isValid = amount > 0;
            } catch (NumberFormatException e) {
                isValid = false;
            }
        }

        return isValid;
    }

    private void initViews(View view) {
        etAmount = view.findViewById(R.id.etAmount);
        spPaymentType = view.findViewById(R.id.spPaymentType);
        clAdditionalDetails = view.findViewById(R.id.clAdditionalDetails);
        etProvider = view.findViewById(R.id.etProvider);
        etTransactionReference = view.findViewById(R.id.etTransactionReference);
        btnOk = view.findViewById(R.id.btnOk);
        btnCancel = view.findViewById(R.id.btnCancel);

        initPaymentTypes();
    }

    private void initPaymentTypes() {
        String[] typeNames = new String[paymentTypes.size()];
        for (int i = 0; i < paymentTypes.size(); i++) {
            typeNames[i] = paymentTypes.get(i).getDisplayName();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                typeNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spPaymentType.setAdapter(adapter);

        if (paymentTypes.isEmpty()) {
            ToastUtils.showToast(requireContext(), getString(R.string.all_payment_types_already_added));
        }
    }

    public interface PaymentAddedListener {
        void onPaymentSave(Payment payment);
    }
}
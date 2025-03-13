package com.assignment.merabills.ui.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.assignment.merabills.R;
import com.assignment.merabills.data.model.Payment;
import com.assignment.merabills.data.model.PaymentType;
import com.assignment.merabills.utils.CurrencyFormatter;

import java.util.List;

public class PaymentChipAdapter extends RecyclerView.Adapter<PaymentChipAdapter.ViewHolder> {

    private final OnPaymentDeletedListener listener;
    private final List<Payment> itemList;

    public PaymentChipAdapter(List<Payment> itemList, OnPaymentDeletedListener listener) {
        this.itemList = itemList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_payment_chip, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Payment item = itemList.get(position);
        String midValue = item.getType() == PaymentType.CASH ? " = Rs. " : ": Rs. ";

        holder.tvPaymentName.setText(item.getType().getDisplayName() + midValue + CurrencyFormatter.formatAsRupees(item.getAmount()));

        holder.ivRemove.setOnClickListener(v -> listener.onPaymentDeleted(item.getType()));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void updateList(List<Payment> newList) {
        itemList.clear();
        itemList.addAll(newList);
        notifyDataSetChanged();
    }

    public interface OnPaymentDeletedListener {
        void onPaymentDeleted(PaymentType type);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvPaymentName;
        ImageView ivRemove;

        public ViewHolder(View itemView) {
            super(itemView);
            tvPaymentName = itemView.findViewById(R.id.tvPaymentName);
            ivRemove = itemView.findViewById(R.id.ivRemove);
        }
    }
}

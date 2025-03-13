package com.assignment.merabills.utils;

import java.text.NumberFormat;
import java.util.Locale;

public class CurrencyFormatter {

    public static String formatAsRupees(double amount) {
        NumberFormat format = NumberFormat.getNumberInstance(Locale.getDefault());
        format.setMinimumFractionDigits(0);
        format.setMaximumFractionDigits(2);

        return format.format(amount);
    }
}
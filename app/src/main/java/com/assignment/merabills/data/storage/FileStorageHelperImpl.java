package com.assignment.merabills.data.storage;

import android.content.Context;
import android.util.Log;

import com.assignment.merabills.data.model.PaymentData;
import com.assignment.merabills.utils.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class FileStorageHelperImpl implements FileStorageHelper {

    private static final String TAG = "FileStorageHelperImpl";
    private final Context context;
    private final Gson gson;

    public FileStorageHelperImpl(Context context) {
        this.context = context;
        this.gson = new GsonBuilder().create();
    }

    @Override
    public boolean savePaymentData(PaymentData data) {
        if (data == null) {
            return false;
        }

        try {
            File file = new File(context.getFilesDir(), Constants.PAYMENT_FILE_NAME);
            FileOutputStream fos = new FileOutputStream(file);
            OutputStreamWriter writer = new OutputStreamWriter(fos);

            String jsonData = gson.toJson(data);
            writer.write(jsonData);
            writer.close();
            fos.close();

            return true;
        } catch (IOException e) {
            Log.e(TAG, "Error saving payment data", e);
            return false;
        }
    }

    @Override
    public PaymentData loadPaymentData() {
        File file = new File(context.getFilesDir(), Constants.PAYMENT_FILE_NAME);

        if (!file.exists()) {
            return new PaymentData();
        }

        try {
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader reader = new InputStreamReader(fis);

            StringBuilder jsonData = new StringBuilder();
            char[] buffer = new char[1024];
            int read;

            while ((read = reader.read(buffer)) != -1) {
                jsonData.append(buffer, 0, read);
            }

            reader.close();
            fis.close();

            return gson.fromJson(jsonData.toString(), PaymentData.class);
        } catch (IOException | com.google.gson.JsonSyntaxException e) {
            Log.e(TAG, "Error loading payment data", e);
            return new PaymentData();
        }
    }
}
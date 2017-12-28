package com.devops.krakenlabs.lanix.ventas;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devops.krakenlabs.lanix.R;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

public class VentasFecha extends Fragment implements Step {
    private static final String TAG = VentasFecha.class.getSimpleName();
    public VentasFecha() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ventas_fecha, container, false);
    }

    @Nullable
    @Override
    public VerificationError verifyStep() {
        Log.d(TAG, "verifyStep() called");
        return null;
    }

    @Override
    public void onSelected() {
        Log.d(TAG, "onSelected() called");
    }

    @Override
    public void onError(@NonNull VerificationError error) {
        Log.d(TAG, "onError() called with: error = [" + error + "]");

    }
}

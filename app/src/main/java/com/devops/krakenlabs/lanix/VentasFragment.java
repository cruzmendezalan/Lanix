package com.devops.krakenlabs.lanix;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devops.krakenlabs.lanix.ventas.MyStepperAdapter;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;


public class VentasFragment extends Fragment implements Step {
    private static final String TAG = VentasFragment.class.getSimpleName();
    private View rootView;
    
    public VentasFragment() {
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
        rootView =  inflater.inflate(R.layout.fragment_ventas, container, false);
        initUI();
        return rootView;
    }

    private void initUI() {
        MyStepperAdapter myStepperAdapter = new MyStepperAdapter(getFragmentManager(), getContext());
        StepperLayout stepperLayout = rootView.findViewById(R.id.stepperLayout);
        stepperLayout.setAdapter(myStepperAdapter);

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
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

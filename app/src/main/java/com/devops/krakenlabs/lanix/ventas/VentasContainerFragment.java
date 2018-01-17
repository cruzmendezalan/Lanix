package com.devops.krakenlabs.lanix.ventas;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devops.krakenlabs.lanix.R;
import com.stepstone.stepper.StepperLayout;
import com.stepstone.stepper.VerificationError;

public class VentasContainerFragment extends Fragment implements StepperLayout.StepperListener{
    private static final String TAG = VentasContainerFragment.class.getSimpleName();
    private View rootView;
    private StepperLayout stepperLayout;
    private VentasAdapter ventasAdapter;

    public VentasContainerFragment() {
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
       rootView = inflater.inflate(R.layout.fragment_ventas_container, container, false);
       stepperLayout = rootView.findViewById(R.id.stepperLayout);
       ventasAdapter = new VentasAdapter(getFragmentManager(),getActivity());
       stepperLayout.setAdapter(ventasAdapter);
       return rootView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onCompleted(View completeButton) {
        Log.d(TAG, "onCompleted() called with: completeButton = [" + completeButton + "]");
    }

    @Override
    public void onError(VerificationError verificationError) {
        Log.d(TAG, "onError() called with: verificationError = [" + verificationError + "]");
    }

    @Override
    public void onStepSelected(int newStepPosition) {
        Log.d(TAG, "onStepSelected() called with: newStepPosition = [" + newStepPosition + "]");
    }

    @Override
    public void onReturn() {
        Log.d(TAG, "onReturn() called");
    }

//    @Nullable
//    @Override
//    public VerificationError verifyStep() {
//        //return null if the user can go to the next step, create a new VerificationError instance otherwise
//        return null;
//    }
//
//    @Override
//    public void onSelected() {
//        //update UI when selected
//    }
//
//    @Override
//    public void onError(@NonNull VerificationError error) {
//        //handle error inside of the fragment, e.g. show error on EditText
//    }
}

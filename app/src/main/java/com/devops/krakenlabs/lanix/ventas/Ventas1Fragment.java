package com.devops.krakenlabs.lanix.ventas;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;

import com.devops.krakenlabs.lanix.R;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Ventas1Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Ventas1Fragment extends Fragment implements Step {
    private View rootView;
    private AutoCompleteTextView tvNombre;
    private AutoCompleteTextView tvApPat;
    private AutoCompleteTextView tvApMat;
    private AutoCompleteTextView tvEmail;
    private AutoCompleteTextView tvTicket;

    private void assignViews() {
        tvNombre = rootView.findViewById(R.id.tv_nombre);
        tvApPat  = rootView.findViewById(R.id.tv_ap_pat);
        tvApMat  = rootView.findViewById(R.id.tv_ap_mat);
        tvEmail  = rootView.findViewById(R.id.tv_email);
        tvTicket = rootView.findViewById(R.id.tv_ticket);
    }

    public Ventas1Fragment() {
        // Required empty public constructor
    }

    public static Ventas1Fragment newInstance() {
        Ventas1Fragment fragment = new Ventas1Fragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.fragment_ventas1, container, false);
        assignViews();
        return rootView;
    }


    @Nullable
    @Override
    public VerificationError verifyStep() {
        //return null if the user can go to the next step, create a new VerificationError instance otherwise
        return null;
    }

    @Override
    public void onSelected() {
        //update UI when selected
    }

    @Override
    public void onError(@NonNull VerificationError error) {
        //handle error inside of the fragment, e.g. show error on EditText
    }
}

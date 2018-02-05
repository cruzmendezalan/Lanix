package com.devops.krakenlabs.lanix.ventas;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;

import com.devops.krakenlabs.lanix.HomeActivity;
import com.devops.krakenlabs.lanix.R;
import com.devops.krakenlabs.lanix.base.LanixApplication;
import com.devops.krakenlabs.lanix.models.venta.VentasRequestt;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VentasFirstStepFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VentasFirstStepFragment extends Fragment implements Step,DatePickerDialog.OnDateSetListener {
    private static final String TAG = VentasFirstStepFragment.class.getSimpleName();
    private View rootView;
    private AutoCompleteTextView tvNombre;
    private AutoCompleteTextView tvApPat;
    private AutoCompleteTextView tvApMat;
    private AutoCompleteTextView tvEmail;
    private AutoCompleteTextView tvTicket;
    private AutoCompleteTextView tvFecha;
    private AutoCompleteTextView tvTienda;
    private VentasRequestt ventaRequest;

    private void assignViews() {
        tvNombre = rootView.findViewById(R.id.tv_nombre);
        tvApPat  = rootView.findViewById(R.id.tv_ap_pat);
        tvApMat  = rootView.findViewById(R.id.tv_ap_mat);
        tvEmail  = rootView.findViewById(R.id.tv_email);
        tvTicket = rootView.findViewById(R.id.tv_ticket);
        tvFecha = rootView.findViewById(R.id.tv_fecha);
        tvTienda = rootView.findViewById(R.id.tv_tienda);
    }

    public VentasFirstStepFragment() {
        // Required empty public constructor
    }

    public static VentasFirstStepFragment newInstance() {
        VentasFirstStepFragment fragment = new VentasFirstStepFragment();
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
        tvFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });
//        btn_clear.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                tvNombre .setText("");
//                tvApPat  .setText("");
//                tvApMat  .setText("");
//                tvEmail  .setText("");
//                tvTicket .setText("");
//                tvFecha .setText("");
//            }
//        });
        return rootView;
    }


    @Nullable
    @Override
    public VerificationError verifyStep() {
        String t = "Este campo es obligatorio";
//        if (tvNombre .getText().length() < 3){
//            tvNombre.setError(t);
//            return new VerificationError("Nombre incompleto");
//        };
//        if (tvApPat  .getText().length() < 3){
//            tvApPat.setError(t);
//            return new VerificationError("Apellido paterno incompleto");
//        };
//        if (tvApMat  .getText().length() < 3){
//            tvApMat.setError(t);
//            return new VerificationError("Apellido materno incompleto");
//        };
//        if (tvEmail  .getText().length() < 3){
//            tvEmail.setError(t);
//            return new VerificationError("Email incompleto");
//        };
        if (tvTicket .getText().length() < 1){
            tvTicket.setError(t);
            return new VerificationError("Ticket incompleto");
        };

        if (tvFecha.getText().length() < 5){
            tvTicket.setError(t);
            return new VerificationError("Es necesario agregar una fecha");
        }

        ventaRequest  = new VentasRequestt(tvNombre.getText().toString(), "",tvFecha.getText().toString(),tvApPat.getText().toString(),tvEmail.getText().toString(),
                "", UUID.randomUUID().toString(), tvTicket.getText().toString(), "",
                LanixApplication.getInstance().getAuthController().getUser().getSesion().getIdentificador(), "" );
        ventaRequest.setTienda(tvTienda.getText().toString());
        //return null if the user can go to the next step, create a new VerificationError instance otherwise
        return null;
    }

    @Override
    public void onSelected() {
        Log.d(TAG, "onSelected() called");
        //update UI when selected
    }

    @Override
    public void onError(@NonNull VerificationError error) {
        Log.d(TAG, "onError() called with: error = [" + error + "]");
        //handle error inside of the fragment, e.g. show error on EditText
    }

    @Override
    public void onDateSet(DatePicker datePicker, int day, int month, int year) {
        final String selectedDate = day + "/" + (month+1) + "/" + year;
        tvFecha.setText(selectedDate);
    }
    private void showDatePickerDialog() {
        HomeActivity ho = (HomeActivity) getActivity();
        DatePickerFragment datePicker = new DatePickerFragment();
        datePicker.setOnDateSetListener(this);
        datePicker.show(ho.getFragmentManager(),DatePickerFragment.class.getSimpleName());
    }

    public VentasRequestt getVentaRequest() {
        return ventaRequest;
    }
}

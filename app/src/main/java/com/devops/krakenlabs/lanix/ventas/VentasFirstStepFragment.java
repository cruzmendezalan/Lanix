package com.devops.krakenlabs.lanix.ventas;


import android.app.DatePickerDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.ImageView;

import com.devops.krakenlabs.lanix.HomeActivity;
import com.devops.krakenlabs.lanix.R;
import com.devops.krakenlabs.lanix.base.LanixApplication;
import com.devops.krakenlabs.lanix.models.venta.VentasRequestt;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;
import java.util.UUID;
import static android.content.Context.INPUT_METHOD_SERVICE;


public class VentasFirstStepFragment extends Fragment implements Step,DatePickerDialog.OnDateSetListener, HomeActivity.PhotoNotifier {
    private static final String TAG = VentasFirstStepFragment.class.getSimpleName();
    private View rootView;
    private AutoCompleteTextView tvNombre;
    private AutoCompleteTextView tvApPat;
    private AutoCompleteTextView tvApMat;
    private AutoCompleteTextView tvEmail;
    private AutoCompleteTextView tvTicket;
    private AutoCompleteTextView tvFecha;
    private AutoCompleteTextView tvTienda;
    private VentasRequestt       ventaRequest;
    private ImageView            ivCamera;
    private ImageView            ivPhoto;

    private void assignViews() {
        try{
            tvNombre = rootView.findViewById(R.id.tv_nombre);
            tvApPat  = rootView.findViewById(R.id.tv_ap_pat);
            tvApMat  = rootView.findViewById(R.id.tv_ap_mat);
            tvEmail  = rootView.findViewById(R.id.tv_email);
            tvTicket = rootView.findViewById(R.id.tv_ticket);
            tvFecha  = rootView.findViewById(R.id.tv_fecha);
            tvTienda = rootView.findViewById(R.id.tv_tienda);
            ivCamera = rootView.findViewById(R.id.iv_camera);
            ivPhoto  = rootView.findViewById(R.id.iv_photo);

            ivCamera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    requestPhoto();
                }
            });
         }catch (Exception e){
           e.printStackTrace();
        }
    }

    private void requestPhoto() {
        try{
           HomeActivity ho = (HomeActivity) getActivity();
           ho.setPhotoNotifier(this);
           ho.dispatchTakePictureIntent();
         }catch (Exception e){
           e.printStackTrace();
        }
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
        rootView =  inflater.inflate(R.layout.fragment_ventas1, container, false);
        assignViews();
        tvFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });
        return rootView;
    }


    @Nullable
    @Override
    public VerificationError verifyStep() {
        String t = "Este campo es obligatorio";
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
        hideSoftKeyboard();
        HomeActivity ho = (HomeActivity) getActivity();
        DatePickerFragment datePicker = new DatePickerFragment();
        datePicker.setOnDateSetListener(this);
        datePicker.show(ho.getFragmentManager(),DatePickerFragment.class.getSimpleName());
    }

    private void hideSoftKeyboard() {
        try{
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public VentasRequestt getVentaRequest() {
        return ventaRequest;
    }

    @Override
    public void photoTaked(Bitmap photo) {
        Log.d(TAG, "photoTaked() called with: photo = [" + photo + "]");
        if(null != ivPhoto){
            ivPhoto.setImageBitmap(photo);
        }
    }
}

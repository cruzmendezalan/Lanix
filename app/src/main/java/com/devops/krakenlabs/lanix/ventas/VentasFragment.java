package com.devops.krakenlabs.lanix.ventas;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.devops.krakenlabs.lanix.HomeActivity;
import com.devops.krakenlabs.lanix.R;
import com.devops.krakenlabs.lanix.adapters.ModelosAdapter;
import com.devops.krakenlabs.lanix.base.LanixApplication;
import com.devops.krakenlabs.lanix.models.catalogos.Catalog;
import com.devops.krakenlabs.lanix.models.catalogos.CatalogRequest;
import com.google.gson.Gson;

import org.json.JSONObject;

public class VentasFragment extends Fragment implements Response.ErrorListener, DatePickerDialog.OnDateSetListener  {
    private static final String TAG = VentasFragment.class.getSimpleName();
    private View viewRoot;
    private RecyclerView rvModelos;
    private EditText etFecha;
    private EditText etImei;
    private EditText etLccid;

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
        viewRoot =  inflater.inflate(R.layout.fragment_ventas, container, false);
        rvModelos = viewRoot.findViewById(R.id.rv_modelo);
        etFecha = viewRoot.findViewById(R.id.et_fecha);
        etFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });
        requestModels();
        return viewRoot;
    }

    private void initUI(String response) {
        Log.d(TAG, "initUI() called with: response = [" + response + "]");
        Gson gson =  new Gson();
        Catalog catalog = gson.fromJson(response, Catalog.class);
        Log.e(TAG, "initUI: "+catalog );
        ModelosAdapter modelosAdapter = new ModelosAdapter(catalog);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        rvModelos.setLayoutManager(mLayoutManager);
        rvModelos.setAdapter(modelosAdapter);
    }

    /**
     * Llamada de solicitud de los mdoelos
     */
    private void requestModels() {
        LanixApplication lanixApplication   = LanixApplication.getInstance();
        CatalogRequest catalogRequest = new CatalogRequest();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                (lanixApplication.getNetworkController().getServiceUrl(Catalog.class.getSimpleName())+lanixApplication.getAuthController().getUser().getSesion().getIdentificador()),
                catalogRequest.toJson(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG, "onResponse() called with: response = [" + response + "]");
                        initUI(response.toString());
                    }
                },this);
        lanixApplication.getNetworkController().getQueue().add(jsonObjectRequest);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    private void discoverModels(){
        Log.d(TAG, "discoverModels() called");
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.d(TAG, "onErrorResponse() called with: error = [" + error + "]");
    }

    private void showDatePickerDialog() {
        HomeActivity ho = (HomeActivity) getActivity();
        DatePickerFragment datePicker = new DatePickerFragment();
        datePicker.setOnDateSetListener(this);
        datePicker.show(ho.getFragmentManager(),DatePickerFragment.class.getSimpleName());
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        final String selectedDate = day + " / " + (month+1) + " / " + year;
        etFecha.setText(selectedDate);
    }
}

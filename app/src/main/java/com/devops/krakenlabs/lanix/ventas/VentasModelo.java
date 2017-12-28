package com.devops.krakenlabs.lanix.ventas;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.devops.krakenlabs.lanix.R;
import com.devops.krakenlabs.lanix.adapters.ModelosAdapter;
import com.devops.krakenlabs.lanix.base.LanixApplication;
import com.devops.krakenlabs.lanix.models.catalogos.Catalog;
import com.devops.krakenlabs.lanix.models.catalogos.CatalogRequest;
import com.google.gson.Gson;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import org.json.JSONObject;

public class VentasModelo extends Fragment implements Step, Response.ErrorListener {
    private static final String TAG = VentasModelo.class.getSimpleName();
    private View viewRoot;
    private RecyclerView rvModelos;
    
    
    public VentasModelo() {
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
        viewRoot =  inflater.inflate(R.layout.fragment_ventas_modelo, container, false);
        rvModelos = viewRoot.findViewById(R.id.rv_modelo);
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

    @Nullable
    @Override
    public VerificationError verifyStep() {
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

    private void discoverModels(){
        Log.d(TAG, "discoverModels() called");
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.d(TAG, "onErrorResponse() called with: error = [" + error + "]");
    }
}

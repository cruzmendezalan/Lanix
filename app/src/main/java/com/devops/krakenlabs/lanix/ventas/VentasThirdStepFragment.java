package com.devops.krakenlabs.lanix.ventas;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.devops.krakenlabs.lanix.HomeActivity;
import com.devops.krakenlabs.lanix.R;
import com.devops.krakenlabs.lanix.base.LanixApplication;
import com.devops.krakenlabs.lanix.models.venta.VentasRequestt;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import org.json.JSONObject;

/**
 * Created by Alan Giovani Cruz Méndez on 17/01/18 22:34.
 * cruzmendezalan@gmail.com
 */

public class VentasThirdStepFragment extends Fragment implements Step, Response.ErrorListener {
    private static final String TAG = VentasThirdStepFragment.class.getSimpleName();
    private View rootView;
    private ListView lvProductos;
    private Button btnConfirmarVenta;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView    = inflater.inflate(R.layout.f_third_step, container, false);
        lvProductos = rootView.findViewById(R.id.lv_productos);
        btnConfirmarVenta = rootView.findViewById(R.id.btn_confirmar_venta);
        btnConfirmarVenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendVenta();
            }
        });
        initListView();
        return rootView;
    }

    private void sendVenta() {
        HomeActivity ho = (HomeActivity) getActivity();
        LanixApplication lanixApplication   = LanixApplication.getInstance();
        JsonObjectRequest jsonObjectRequest  = new JsonObjectRequest(Request.Method.POST,
                lanixApplication.getNetworkController().getServiceUrl(VentasRequestt.class.getSimpleName()),
                ho.getVentasFirstStepFragment().getVentaRequest().toJson(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "onResponse() called with: response = [" + response + "]");
                        if (response != null){
                            notificarVenta();
                        }
                    }
                },this);
        lanixApplication.getNetworkController().getQueue().add(jsonObjectRequest);

    }

    private void notificarVenta() {
        HomeActivity ho = (HomeActivity) getActivity();
        Snackbar sn = Snackbar.make(rootView, "Se a completado el proceso de la venta", Snackbar.LENGTH_LONG);
        View snackBarView = sn.getView();
        snackBarView.setBackgroundColor(getResources().getColor(R.color.white));
        sn.show();
        Log.e(TAG, "notificarVenta: validar venta" );
        Intent home = new Intent(getContext(), HomeActivity.class);
        startActivity(home);
    }

    private void initListView() {
        Log.d(TAG, "initListView() called");
        try{
            HomeActivity ho = (HomeActivity) getActivity();
            String[] values = new String[ho.getVentasFirstStepFragment().getVentaRequest().getProductos().size()];
            for (int i = 0; i < ho.getVentasFirstStepFragment().getVentaRequest().getProductos().size(); i++) {
                values[i] = ho.getVentasFirstStepFragment().getVentaRequest().getProductos().get(i).getImei();
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, values);
            lvProductos.setAdapter(adapter);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        Log.e(TAG, "onResume() called");
        super.onResume();
//        initListView();
    }

    @Nullable
    @Override
    public VerificationError verifyStep() {
        Log.d(TAG, "verifyStep() called");
        sendVenta();
        return null;
    }

    @Override
    public void onSelected() {
        Log.d(TAG, "onSelected() called");
        initListView();
    }

    @Override
    public void onError(@NonNull VerificationError error) {

    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.d(TAG, "onErrorResponse() called with: error = [" + error + "]");
    }
}

package com.devops.krakenlabs.lanix.misventas;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.devops.krakenlabs.lanix.R;
import com.devops.krakenlabs.lanix.base.LanixApplication;
import com.devops.krakenlabs.lanix.models.historico.VentasHistoriaRequest;
import com.devops.krakenlabs.lanix.models.historico.VentasHistoriaResponse;
import com.google.gson.Gson;

import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class MisVentasFragment extends Fragment {
    private static final String TAG = MisVentasFragment.class.getSimpleName();
    private View rootView;
    private ListView lvMisVentas;
    private VentasHistoriaResponse misVentasHistoriaResponse;


    public MisVentasFragment() {
        // Required empty public constructor
        requestVentas();
    }

    private void requestVentas() {
        VentasHistoriaRequest v = new VentasHistoriaRequest();
        LanixApplication.getInstance().getNetworkController().requestData(v, Request.Method.GET, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, "onResponse() called with: response = [" + response + "]");
                displayVentas(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse() called with: error = [" + error + "]");
            }
        });
    }

    private void displayVentas(JSONObject response) {
        Log.d(TAG, "displayVentas() called with: response = [" + response + "]");
        if (response != null){
            Log.e(TAG, "displayVentas: "+response.toString() );
            Gson g = new Gson();
            misVentasHistoriaResponse = g.fromJson(response.toString(),VentasHistoriaResponse.class);
        }

        if (misVentasHistoriaResponse != null){
            try{
                String[] values = new String[misVentasHistoriaResponse.getVentas().size()];
                for (int i = 0; i < misVentasHistoriaResponse.getVentas().size(); i++) {
                    try{
                        values[i] = misVentasHistoriaResponse.getVentas().get(i).getFechaVenta() + " FOLIO:  "+ misVentasHistoriaResponse.getVentas().get(i).getVentaId() + "\n"+misVentasHistoriaResponse.getVentas().get(i).getProducto();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_2, android.R.id.text1, values);
                lvMisVentas.setAdapter(adapter);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView    = inflater.inflate(R.layout.f_mis_ventas, container, false);
        lvMisVentas = rootView.findViewById(R.id.lv_ventas);
        displayVentas(null);
        return rootView;
    }

}

package com.devops.krakenlabs.lanix.ventas;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.devops.krakenlabs.lanix.R;
import com.devops.krakenlabs.lanix.base.LanixApplication;
import com.devops.krakenlabs.lanix.models.catalogos.Catalog;
import com.devops.krakenlabs.lanix.models.catalogos.CatalogRequest;
import com.devops.krakenlabs.lanix.models.venta.ProductosItem;
import com.devops.krakenlabs.lanix.models.venta.VentaRequest;
import com.google.gson.Gson;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import org.json.JSONObject;

import java.util.ArrayList;

import gr.escsoft.michaelprimez.searchablespinner.SearchableSpinner;

public class VentasSecondStepFragment extends Fragment implements Response.ErrorListener, Step {
    private static final String TAG = VentasSecondStepFragment.class.getSimpleName();
    private View viewRoot;
//    private RecyclerView rvModelos;
    private EditText etImei;
    private EditText etLccid;
    private ModelosAdapter modelosAdapter;
    private Button btnVenta;
    private ArrayList<ProductosItem > ventaArr =  new ArrayList<>();
    private SearchableSpinner spinner;

    public VentasSecondStepFragment() {
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
//        rvModelos = viewRoot.findViewById(R.id.rv_modelo);
        etImei   = viewRoot.findViewById(R.id.et_imei);
        etLccid  = viewRoot.findViewById(R.id.et_lccid);
        btnVenta = viewRoot.findViewById(R.id.btn_guardar_venta);
        spinner = viewRoot.findViewById(R.id.searchableSpinner);

        btnVenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (etFecha.length() > 3 && etImei.length() > 3 && etLccid.length() > 3 ){
//                    enviarVenta();
//                }
                generateAndAddVenta();
            }
        });
        requestModels();
        return viewRoot;
    }

    // TODO: 17/01/18
    private void generateAndAddVenta() {
//        ventaArr.add(new ProductosItem("",1212,String.valueOf(mo.getModeloId()), mo.getModelo()));
    }

    private void initUI(String response) {
        Log.d(TAG, "initUI() called with: response = [" + response + "]");
        Gson gson =  new Gson();
        Catalog catalog = gson.fromJson(response, Catalog.class);
        Log.e(TAG, "initUI: "+catalog );
        modelosAdapter = new ModelosAdapter(getActivity(),R.layout.view_list_item,catalog);
        spinner.setAdapter(modelosAdapter);
//        modelosAdapter = new ModelosAdapterOld(catalog);
//        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
//        rvModelos.setLayoutManager(mLayoutManager);
//        rvModelos.setAdapter(modelosAdapter);
    }

    /**
     * Llamada de solicitud de los mdoelos
     */
    private void requestModels() {
        LanixApplication lanixApplication   = LanixApplication.getInstance();
        CatalogRequest catalogRequest = new CatalogRequest();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                (lanixApplication.getNetworkController().getServiceUrl(Catalog.class.getSimpleName())+
                        lanixApplication.getAuthController().getUser().getSesion().getIdentificador()),
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

    private void enviarVenta(){
        Log.d(TAG, "enviarVenta() called");

//        for (ModelosItem mo:
//             modelosAdapter.getCatalog().getModelos()) {
//            if (mo.getSelected()){//El producto fue seleccionado
//                ventaArr.add(new ProductosItem("",1212,String.valueOf(mo.getModeloId()), mo.getModelo()));
//            }
//        }
        if (ventaArr.size() > 0){


//            LanixApplication lanixApplication   = LanixApplication.getInstance();
//            JsonObjectRequest jsonObjectRequest  = new JsonObjectRequest(Request.Method.POST,
//                    lanixApplication.getNetworkController().getServiceUrl(VentaRequest.class.getSimpleName()),
//                    ventaRequest.toJson(),
//                    new Response.Listener<JSONObject>() {
//                        @Override
//                        public void onResponse(JSONObject response) {
//                            Log.d(TAG, "onResponse() called with: response = [" + response + "]");
//                        }
//                    },this);
//            lanixApplication.getNetworkController().getQueue().add(jsonObjectRequest);
        }else{
            Log.e(TAG, "enviarVenta: NO SE REALIZO VENTA" );
        }
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

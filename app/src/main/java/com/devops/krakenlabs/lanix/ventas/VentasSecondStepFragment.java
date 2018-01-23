package com.devops.krakenlabs.lanix.ventas;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.devops.krakenlabs.lanix.HomeActivity;
import com.devops.krakenlabs.lanix.R;
import com.devops.krakenlabs.lanix.base.LanixApplication;
import com.devops.krakenlabs.lanix.models.catalogos.Catalog;
import com.devops.krakenlabs.lanix.models.catalogos.CatalogRequest;
import com.devops.krakenlabs.lanix.models.venta.ProductosItem;
import com.devops.krakenlabs.lanix.models.venta.VentasRequestt;
import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import org.json.JSONObject;

import java.util.ArrayList;

import gr.escsoft.michaelprimez.searchablespinner.SearchableSpinner;
import gr.escsoft.michaelprimez.searchablespinner.interfaces.OnItemSelectedListener;

public class VentasSecondStepFragment extends Fragment implements Response.ErrorListener, Step {
    private static final String TAG = VentasSecondStepFragment.class.getSimpleName();
    private View viewRoot;
//    private RecyclerView rvModelos;
    private AutoCompleteTextView etImei;
    private EditText etLccid;
    private ModelosAdapter modelosAdapter;
    private ModelosAdapter catalogsadapter;
    private Button btnVenta;
    private ArrayList<ProductosItem > ventaArr =  new ArrayList<>();
    private SearchableSpinner spinner;
    private SearchableSpinner spinnerCatalog;
    private int positionSelected;
    private int positionCatalogSelected;
    private Catalog catalog;
    private ScrollView scrollView;

    public VentasSecondStepFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
        viewRoot.invalidate();
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
        spinnerCatalog = viewRoot.findViewById(R.id.searchableCatalog);
        btnVenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (etFecha.length() > 3 && etImei.length() > 3 && etLccid.length() > 3 ){
//                    enviarVenta();
//                }
                generateAndAddVenta();
            }
        });
        etImei.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initScanBarCode();
            }
        });
        etImei.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                initScanBarCode();
            }
        });
        etImei.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                initScanBarCode();
                return false;
            }
        });
        requestModels();
        return viewRoot;
    }

    private void initScanBarCode(){
        Log.d(TAG, "initScanBarCode() called");
        IntentIntegrator integrator = new IntentIntegrator(getActivity());
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("Escanea el código de barras");
        integrator.setCameraId(0);  // Use a specific camera of the device
        integrator.setBeepEnabled(true);
        integrator.setOrientationLocked(true);
        integrator.setBarcodeImageEnabled(true);
        integrator.initiateScan();

    }


    // TODO: 17/01/18
    private void generateAndAddVenta() {
        ventaArr.add(new ProductosItem("",1212,String.valueOf(catalog.getModelos().get(positionSelected-1).getModeloId()), catalog.getModelos().get(positionSelected-1).getModelo()));
        etImei   .setText("");
        etLccid  .setText("");
        Snackbar sn = Snackbar.make(viewRoot, "Se a agregado el smartphone a la lista de venta", Snackbar.LENGTH_LONG);
        View snackBarView = sn.getView();
        snackBarView.setBackgroundColor(getResources().getColor(R.color.white));
        sn.show();
    }

    private void initUI(String response) {
        Log.d(TAG, "initUI() called with: response = [" + response + "]");
        Gson gson =  new Gson();
        catalog = gson.fromJson(response, Catalog.class);
        Log.e(TAG, "initUI: "+catalog );
        modelosAdapter = new ModelosAdapter(getActivity(),R.layout.view_list_item,catalog, 0);
        catalogsadapter = new ModelosAdapter(getActivity(), R.layout.view_list_item,catalog,1);
        spinner.setAdapter(modelosAdapter);
        spinner.setOnItemSelectedListener(mOnItemSelectedListener);

        spinnerCatalog.setAdapter(catalogsadapter);
        spinnerCatalog.setOnItemSelectedListener(mOnItemSelectedCatalogs);
    }

    /**
     * Llamada de solicitud de los mdoelos
     */
    private void requestModels() {
        try{
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
        }catch (Exception e){
            e.printStackTrace();
        }
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
        if (ventaArr.size() > 0){
            HomeActivity ho = (HomeActivity) getActivity();
            LanixApplication lanixApplication   = LanixApplication.getInstance();
            JsonObjectRequest jsonObjectRequest  = new JsonObjectRequest(Request.Method.POST,
                    lanixApplication.getNetworkController().getServiceUrl(VentasRequestt.class.getSimpleName()),
                    ho.getVentasFirstStepFragment().getVentaRequest().toJson(),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, "onResponse() called with: response = [" + response + "]");
                            goHome();
                        }
                    },this);
            lanixApplication.getNetworkController().getQueue().add(jsonObjectRequest);
        }else{
            Log.e(TAG, "enviarVenta: NO SE REALIZO VENTA" );
        }
    }

    private void goHome() {
        HomeActivity ho = (HomeActivity) getActivity();
    }

    @Nullable
    @Override
    public VerificationError verifyStep() {
        try{
            HomeActivity ho = (HomeActivity) getActivity();
            //return null if the user can go to the next step, create a new VerificationError instance otherwise
            ho.getVentasFirstStepFragment().getVentaRequest().setCadenaComercialId(""+catalog.getCadenasComerciales().get(positionCatalogSelected).getCadenaComercialId());
            if (ventaArr.size() > 0){
                ho.getVentasFirstStepFragment().getVentaRequest().setProductos(ventaArr);
            }
            if (ventaArr.size() > 0){
                return null;
            }else{
                if (etImei.getText().length() > 3 || etLccid.getText().length() > 5){
                    generateAndAddVenta();
                    ho.getVentasFirstStepFragment().getVentaRequest().setProductos(ventaArr);
                    return null;
                }
            }
            return new VerificationError("Venta incompleta");
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onSelected() {
        Log.d(TAG, "onSelected() called");
        onResume();
        //update UI when selected
    }

    @Override
    public void onError(@NonNull VerificationError error) {
        Log.d(TAG, "onError() called with: error = [" + error.getErrorMessage() + "]");
        //handle error inside of the fragment, e.g. show error on EditText
    }

    private OnItemSelectedListener mOnItemSelectedListener = new OnItemSelectedListener() {
        @Override
        public void onItemSelected(View view, int position, long id) {
            positionSelected = position;
        }

        @Override
        public void onNothingSelected() {
        }
    };

    private OnItemSelectedListener mOnItemSelectedCatalogs = new OnItemSelectedListener() {
        @Override
        public void onItemSelected(View view, int position, long id) {
            positionCatalogSelected = position;
        }

        @Override
        public void onNothingSelected() {
        }
    };

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy() called");
        super.onDestroy();
    }

    public void setTvImei(String t){
        etImei.setText(t);
    }
}

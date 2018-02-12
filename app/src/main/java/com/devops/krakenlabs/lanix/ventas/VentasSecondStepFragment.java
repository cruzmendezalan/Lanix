package com.devops.krakenlabs.lanix.ventas;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.devops.krakenlabs.lanix.HomeActivity;
import com.devops.krakenlabs.lanix.R;
import com.devops.krakenlabs.lanix.base.LanixApplication;
import com.devops.krakenlabs.lanix.models.catalogos.Catalog;
import com.devops.krakenlabs.lanix.models.catalogos.CatalogRequest;
import com.devops.krakenlabs.lanix.models.venta.ProductosItem;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import org.json.JSONObject;

import java.util.ArrayList;

import gr.escsoft.michaelprimez.searchablespinner.SearchableSpinner;
import gr.escsoft.michaelprimez.searchablespinner.interfaces.OnItemSelectedListener;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class VentasSecondStepFragment extends Fragment implements Response.ErrorListener, Step {
    private static final String TAG = VentasSecondStepFragment.class.getSimpleName();
    private View viewRoot;
//    private RecyclerView rvModelos;
    private AutoCompleteTextView etImei;
    private EditText etLccid;
    private ImageView ivCamera;
    private ModelosAdapter productosAdapter;
    private ModelosAdapter cadenaComercialadapter;
    private ModelosAdapter modelosAdapter;
    private Button btnVenta;
    private ArrayList<ProductosItem > ventaArr =  new ArrayList<>();
    private SearchableSpinner spinnerProductos;
    private SearchableSpinner spinnerCadenaComercial;
    private int positionSelected;
    private int positionCatalogSelected;
    private Catalog catalog;
    private ScrollView scrollView;
    private SearchableSpinner spinnerModelo;

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
        hideSoftKeyboard();
    }

    public void hideSoftKeyboard() {
        Log.d(TAG, "hideSoftKeyboard() called");
        try{
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
        }catch (Exception e){
            e.printStackTrace();
        }
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
        spinnerProductos = viewRoot.findViewById(R.id.searchableProducto);
        spinnerCadenaComercial = viewRoot.findViewById(R.id.searchableCadenaComercial);
        spinnerModelo = viewRoot.findViewById(R.id.searchableModelo);
        ivCamera = viewRoot.findViewById(R.id.tv_camera);
        ivCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initScanBarCode();
            }
        });
        btnVenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generateAndAddVenta();
            }
        });
        requestModels();
        return viewRoot;
    }

    private void initScanBarCode(){
        try{
            Log.d(TAG, "initScanBarCode() called");
            BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(getActivity()).build();

            IntentIntegrator integrator = new IntentIntegrator(getActivity());
            integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
            integrator.setPrompt("Escanea el código de barras");
            integrator.setCameraId(0);  // Use a specific camera of the device
            integrator.setBeepEnabled(true);
            integrator.setOrientationLocked(true);
            integrator.setBarcodeImageEnabled(true);
            integrator.initiateScan();
        }catch (Exception  e){
            e.printStackTrace();
        }
    }


    // TODO: 17/01/18
    private void generateAndAddVenta() {
        try{
            ventaArr.add(new ProductosItem(catalog.getProductos().get(positionSelected-1).getModeloId(), etImei.getText().toString(),catalog.getProductos().get(positionSelected-1).getModelo()));
            etImei   .setText("");
            etLccid  .setText("");
            Snackbar sn = Snackbar.make(viewRoot, "Se a agregado el smartphone a la lista de venta", Snackbar.LENGTH_LONG);
            View snackBarView = sn.getView();
            snackBarView.setBackgroundColor(getResources().getColor(R.color.white));
            sn.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void initUI(String response) {
        try{
            Log.d(TAG, "initUI() called with: response = [" + response + "]");
            Gson gson =  new Gson();
            catalog = gson.fromJson(response, Catalog.class);
            Log.e(TAG, "initUI: "+catalog );
            productosAdapter       = new ModelosAdapter(getActivity(),R.layout.view_list_item,catalog, 0);
            cadenaComercialadapter = new ModelosAdapter(getActivity(), R.layout.view_list_item,catalog,1);
            modelosAdapter         = new ModelosAdapter(getActivity(), R.layout.view_list_item,catalog,2);
            spinnerProductos.setAdapter(productosAdapter);
            spinnerProductos.setOnItemSelectedListener(mOnProdcutoSelected);

            spinnerCadenaComercial.setAdapter(cadenaComercialadapter);
            spinnerCadenaComercial.setOnItemSelectedListener(mOnCadenaComercialSelected);

            spinnerModelo.setAdapter(modelosAdapter);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Llamada de solicitud de los mdoelos
     */
    private void requestModels() {
        try{
            CatalogRequest catalogRequest = new CatalogRequest();
            LanixApplication.getInstance().getNetworkController().requestData(catalogRequest, Request.Method.GET,new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.e(TAG, "onResponse() called with: response = [" + response + "]");
                    initUI(response.toString());
                }
            },this);
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
            if (etImei.getText().length() > 3 ){
                generateAndAddVenta();
                ho.getVentasFirstStepFragment().getVentaRequest().setProductos(ventaArr);
                return null;
            }
            if (ventaArr.size() > 0){
                return null;
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

    private OnItemSelectedListener mOnProdcutoSelected = new OnItemSelectedListener() {
        @Override
        public void onItemSelected(View view, int position, long id) {
            positionSelected = position;
            modelosAdapter.updateModelos(catalog.getModelos().get(position).getModeloId());
//            spinnerModelo.setAdapter(modelosAdapter);
            modelosAdapter.notifyDataSetChanged();
            spinnerModelo.invalidate();

        }

        @Override
        public void onNothingSelected() {
        }
    };

    private OnItemSelectedListener mOnCadenaComercialSelected = new OnItemSelectedListener() {
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

package com.devops.krakenlabs.lanix;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.devops.krakenlabs.lanix.base.LanixApplication;
import com.devops.krakenlabs.lanix.controllers.AuthController;
import com.devops.krakenlabs.lanix.controllers.GPSController;
import com.devops.krakenlabs.lanix.controllers.NetworkController;
import com.devops.krakenlabs.lanix.models.EventEntradaRequest;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class HomeActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener, Response.Listener<JSONObject>,Response.ErrorListener {
    public static String TAG = HomeActivity.class.getSimpleName();
    private GoogleMap mapa;
    private ArrayList permissionsToRequest;
    private ArrayList permissionsRejected = new ArrayList();
    private ArrayList permissions = new ArrayList();

    private final static int ALL_PERMISSIONS_RESULT = 101;
    private GPSController GPSController;
    private boolean isPaused;
    private double latitude;
    private double longitude;

    private CardView cardEntrada;
    private CardView cardSalidaC;
    private CardView cardRegresoC;
    private CardView cardSalida;

    private AuthController authController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f_asistencia);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        isPaused = false;
        mapFragment.getMapAsync(this);
        permissions.add(ACCESS_FINE_LOCATION);
        permissions.add(ACCESS_COARSE_LOCATION);

        permissionsToRequest = findUnAskedPermissions(permissions);
        //get the permissions we have asked for before but are not granted..
        //we will store this in a global list to access later.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (permissionsToRequest.size() > 0)
                requestPermissions((String[]) permissionsToRequest.toArray(new String[permissionsToRequest.size()]), ALL_PERMISSIONS_RESULT);
        }
        authController = LanixApplication.getInstance().getAuthController();

        /**
         * init ui
         */
        cardEntrada  = findViewById(R.id.cv_entrada);
        cardSalidaC  = findViewById(R.id.cv_salida_comer);
        cardRegresoC = findViewById(R.id.cv_regreso_comer);
        cardSalida   = findViewById(R.id.cv_salida);
        cardEntrada.setOnClickListener(this);
        cardSalidaC  .setOnClickListener(this);
        cardRegresoC .setOnClickListener(this);
        cardSalida   .setOnClickListener(this);
    }


    private ArrayList findUnAskedPermissions(ArrayList wanted) {
        ArrayList result = new ArrayList();

        for (Object perm : wanted) {
            if (!hasPermission(String.valueOf(perm))) {
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission) {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private boolean canMakeSmores() {
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    @Override
    protected void onPause() {
        super.onPause();
        isPaused = true;
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {

            case ALL_PERMISSIONS_RESULT:
                for (Object perms : permissionsToRequest) {
                    if (!hasPermission(String.valueOf(perms))) {
                        permissionsRejected.add(perms);
                    }
                }

                if (permissionsRejected.size() > 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(String.valueOf(permissionsRejected.get(0)))) {
                            showMessageOKCancel("Estos permisos son necesarios para la aplicación. Por favor permite el acceso.",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions((String[]) permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                            }
                                        }
                                    });
                            return;
                        }
                    }
                }

                break;
        }

    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(HomeActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancelar", null)
                .create()
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GPSController.stopListener();
        isPaused = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        isPaused = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isPaused = false;
        refreshLocation();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "onMapReady() called with: googleMap = [" + googleMap + "]");
        mapa = googleMap;
    }

    private void refreshLocation(){
        Log.e(TAG, "refreshLocation() called");
        if (mapa != null){
            GPSController = new GPSController(HomeActivity.this);
            if (GPSController.canGetLocation()) {
                longitude = GPSController.getLongitude();
                latitude  = GPSController.getLatitude();
                LatLng latLng = new LatLng(latitude, longitude);
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 18);
                mapa.clear();
                mapa.addMarker(new MarkerOptions().position(latLng).title("Tú ubicación"));
                mapa.animateCamera(cameraUpdate);
                Toast.makeText(getApplicationContext(), "Longitude:" + Double.toString(longitude) + "\nLatitude:" + Double.toString(latitude), Toast.LENGTH_SHORT).show();
            } else {
                GPSController.showSettingsAlert();
            }
        }

        if (!isPaused){
            new CountDownTimer(5000, 1000) {
                public void onTick(long millisUntilFinished) {
                }

                public void onFinish() {
                    refreshLocation();
                }

            }.start();
        }
    }

    private void eventoUsuario(String evento){
        try{
            if (latitude != 0 && longitude != 0){
                Calendar c = Calendar.getInstance();
                System.out.println("Current time => "+c.getTime());

                SimpleDateFormat df = new SimpleDateFormat("dd/MM/YYYY HH:mm");
                String formattedDate = df.format(c.getTime());
                Log.e(TAG, "eventoUsuario: "+formattedDate );
                EventEntradaRequest eventEntradaRequest = new EventEntradaRequest(Double.toString(latitude),
                        authController.getUser().getSesion().getIdentificador(),
                        evento,
                        "",
                        Double.toString(longitude),
                        formattedDate );
                Log.e(TAG, "eventoUsuario: "+eventEntradaRequest.toJson().toString() );
                NetworkController networkController = LanixApplication.getInstance().getNetworkController();
                JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST,
                        networkController.getServiceUrl(eventEntradaRequest.TAG),
                        eventEntradaRequest.toJson(),
                        this,
                        this);
                networkController.getQueue().add(jsObjRequest);
            }else{
                Log.e(TAG, "eventoUsuario: AHORITA NO JOVEN");
            }

        }catch (Exception e){e.printStackTrace();}
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.cv_entrada:{
                eventoUsuario("1");
                break;
            }
            case R.id.cv_salida_comer:{
                eventoUsuario("2");
                break;
            }
            case R.id.cv_regreso_comer:{
                eventoUsuario("3");
                break;
            }
            case R.id.cv_salida:{
                eventoUsuario("4");
                break;
            }
            default:{
                Log.e(TAG, "onClick: COCAS" );
            }
        }
    }

    @Override
    public void onResponse(JSONObject response) {
        Log.w(TAG, "onResponse() called with: response = [" + response + "]");
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.e(TAG, "onErrorResponse() called with: error = [" + error + "]");
    }
}

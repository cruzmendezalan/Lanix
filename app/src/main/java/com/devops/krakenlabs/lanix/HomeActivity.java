package com.devops.krakenlabs.lanix;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.devops.krakenlabs.lanix.base.LanixApplication;
import com.devops.krakenlabs.lanix.controllers.AuthController;
import com.devops.krakenlabs.lanix.controllers.GPSController;
import com.devops.krakenlabs.lanix.controllers.NetworkController;
import com.devops.krakenlabs.lanix.models.EventEntradaRequest;
import com.devops.krakenlabs.lanix.models.asistencia.AsistenciaResponse;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class HomeActivity extends FragmentActivity implements OnMapReadyCallback, View.OnClickListener, Response.Listener<JSONObject>, Response.ErrorListener {
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
    private MaterialDialog notificacionDialog;

    private String dateTime;
    private String eventoString;
    private String POSITIVE_MSG = "LISTO!";

    private static String HORAENTRADA = "1";
    private static String HORASALIDA = "4";
    private static String HORASALIDACOMIDA = "2";
    private static String HORAENTRADACOMIDA = "3";

    private TextView promotorName;
    private FusedLocationProviderClient mFusedLocationClient;
    private Location tLocation;

    private FrameLayout frameLayout;
    private LinearLayout llAsistencia;

    private Fragment activeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f_asistencia);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
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
        cardEntrada = findViewById(R.id.cv_entrada);
        cardSalidaC = findViewById(R.id.cv_salida_comer);
        cardRegresoC = findViewById(R.id.cv_regreso_comer);
        cardSalida = findViewById(R.id.cv_salida);
        promotorName = findViewById(R.id.tv_promotor_name);
        try{
            promotorName.setText(authController.getUser().getPromotor().getNombres() + " " + authController.getUser().getPromotor().getApellidoPaterno());
        }catch (Exception e){
            e.printStackTrace();
        }
        cardEntrada.setOnClickListener(this);
        cardSalidaC.setOnClickListener(this);
        cardRegresoC.setOnClickListener(this);
        cardSalida.setOnClickListener(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        Log.d(TAG, "onSuccess() called with: location = [" + location + "]");
                        if (location != null) {
                            // Logic to handle location object
                            refreshByServices(location);
                        }
                    }
                });
        frameLayout = findViewById(R.id.fl_container);
        frameLayout.setVisibility(View.VISIBLE);

        llAsistencia = findViewById(R.id.ll_asistencia);

        activeFragment = new MenuFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.fl_container, activeFragment).commit();
    }

    private void refreshByServices(Location location) {
        if (location != null){
            tLocation = location;
        }
        if (mapa != null && tLocation != null){
            longitude = tLocation.getLongitude();
            latitude  = tLocation.getLatitude();
            LatLng latLng = new LatLng(latitude, longitude);
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 18);
            mapa.clear();
            mapa.addMarker(new MarkerOptions().position(latLng).title("Tú ubicación"));
            mapa.animateCamera(cameraUpdate);
        }else{
            Log.e(TAG, "refreshByServices: Map null..." );
        }
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
        if (GPSController != null){
            GPSController.stopListener();
        }
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
        refreshByServices(null);
    }

    private void refreshLocation(){
        if (mapa != null){
            GPSController = new GPSController(HomeActivity.this);
            if (GPSController.canGetLocation() && GPSController.getLoc() != null) {
                longitude = GPSController.getLongitude();
                latitude  = GPSController.getLatitude();
                LatLng latLng = new LatLng(latitude, longitude);
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 18);
                mapa.clear();
                mapa.addMarker(new MarkerOptions().position(latLng).title("Tú ubicación"));
                mapa.animateCamera(cameraUpdate);
//                Toast.makeText(getApplicationContext(), "Longitude:" + Double.toString(longitude) + "\nLatitude:" + Double.toString(latitude), Toast.LENGTH_SHORT).show();
            } else {
//                GPSController.showSettingsAlert();
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
                showDialog("LANIX", "Enviando información", null);
                Calendar c = Calendar.getInstance();
                if (evento.equals(HORAENTRADA)){
                    eventoString = "Se a registrado tu hora de entrada a ";
                }else if(evento.equals(HORAENTRADACOMIDA)){
                    eventoString = "Se a registrado tu hora de entrada despues de comida a ";
                }else if(evento.equals(HORASALIDACOMIDA)){
                    eventoString = "Se a registrado tu hora de salida a comer a ";
                }else if(evento.equals(HORASALIDA)){
                    eventoString = "Se a registrado tu salida a ";
                }
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                dateTime = df.format(c.getTime());
                Log.e(TAG, "eventoUsuario: "+dateTime );
                EventEntradaRequest eventEntradaRequest = new EventEntradaRequest(Double.toString(latitude),
                        authController.getUser().getSesion().getIdentificador(),
                        evento,
                        "",
                        Double.toString(longitude),
                        dateTime );
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
                eventoUsuario(HORAENTRADA);
                break;
            }
            case R.id.cv_salida_comer:{
                eventoUsuario(HORASALIDACOMIDA);
                break;
            }
            case R.id.cv_regreso_comer:{
                eventoUsuario(HORAENTRADACOMIDA);
                break;
            }
            case R.id.cv_salida:{
                eventoUsuario(HORASALIDACOMIDA);
                break;
            }
            default:{
                Log.e(TAG, "onClick: COCAS" );
            }
        }
    }

    private void showDialog(String titulo, String contenido, String positive){
        Log.d(TAG, "showDialog() called with: titulo = [" + titulo + "], contenido = [" + contenido + "], positive = [" + positive + "]");
        if (positive == null){
            notificacionDialog = new MaterialDialog.Builder(this)
                    .title(titulo)
                    .content(contenido)
                    .show();
        }
    }

    private void dimissDialog(String titulo, String contenido, String positive){
        Log.d(TAG, "dimissDialog() called with: titulo = [" + titulo + "], contenido = [" + contenido + "], positive = [" + positive + "]");
        notificacionDialog.dismiss();
        notificacionDialog = new MaterialDialog.Builder(this)
                .title(titulo)
                .content(contenido)
                .positiveText(positive)
                .show();

    }
    @Override
    public void onResponse(JSONObject response) {
        Log.e(TAG, "onResponse: "+response );
        Gson g = new Gson();
        AsistenciaResponse asistenciaResponse = g.fromJson(response.toString(), AsistenciaResponse.class);
        if (asistenciaResponse.getError().getNo() == 0){
            dimissDialog("LANIX",eventoString+dateTime,POSITIVE_MSG);
        }else{
            dimissDialog("LANIX","Ooops! Parece que tenemos un problema, por favor vuelve a intentarlo ","Ok");
            authController.syncDevice();
        }

        Log.w(TAG, "onResponse() called with: response = [" + response + "]");
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed() called");
        if (activeFragment != null ){
            activeFragment = new MenuFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.fl_container, activeFragment).commit();
            llAsistencia.setVisibility(View.GONE);
            frameLayout.setVisibility(View.VISIBLE);
        }
//        super.onBackPressed();
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.e(TAG, "onErrorResponse() called with: error = [" + error + "]");
        dimissDialog("LANIX","Ooops! Parece que tenemos un problema, por favor vuelve a intentarlo ","Ok");
    }

    public void hideFragmentContainer(){
        Log.d(TAG, "hideFragmentContainer() called");
        frameLayout.setVisibility(View.GONE);
        llAsistencia.setVisibility(View.VISIBLE);
    }
}

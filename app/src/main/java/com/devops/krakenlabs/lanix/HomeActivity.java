package com.devops.krakenlabs.lanix;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.crashlytics.android.Crashlytics;
import com.devops.krakenlabs.lanix.base.LanixApplication;
import com.devops.krakenlabs.lanix.controllers.AuthController;
import com.devops.krakenlabs.lanix.controllers.GPSController;
import com.devops.krakenlabs.lanix.models.EventEntradaRequest;
import com.devops.krakenlabs.lanix.models.asistencia.AsistenciaResponse;
import com.devops.krakenlabs.lanix.ventas.VentasContainerFragment;
import com.devops.krakenlabs.lanix.ventas.VentasFirstStepFragment;
import com.devops.krakenlabs.lanix.ventas.VentasSecondStepFragment;
import com.devops.krakenlabs.lanix.ventas.VentasThirdStepFragment;
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
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.stepstone.stepper.Step;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import io.fabric.sdk.android.Fabric;

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

    private Button cardEntrada;
    private Button cardSalidaC;
    private Button cardRegresoC;
    private Button cardSalida;

    private Button btnGps;
    private boolean ubicationFromGooglePlay;
    private Location locationFromServices;

    private AuthController authController;
    private MaterialDialog notificacionDialog;

    private String dateTime;
    private String eventoString;
    private String POSITIVE_MSG = "ENTENDIDO";

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

    private VentasSecondStepFragment ventasSecondStepFragment;
    private VentasFirstStepFragment ventasFirstStepFragment;
    private VentasContainerFragment ventasContainerFragment;
    private VentasThirdStepFragment ventasThirdStepFragmentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
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
        btnGps      = findViewById(R.id.btn_gps);
        btnGps.setOnClickListener(this);
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
//                        Log.d(TAG, "onSuccess() called with: location = [" + location + "]");
                        if (location != null) {
                            // Logic to handle location object
                            locationFromServices = location;
//                            refreshByServices(location);
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
        try{
            if (location != null){
                updateMap(location.getLatitude(), location.getLongitude());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
////        Log.d(TAG, "refreshByServices() called with: location = [" + location + "]");
//        if (location != null){
//            tLocation = location;
//        }
//        if (mapa != null && tLocation != null){
//            longitude = tLocation.getLongitude();
//            latitude  = tLocation.getLatitude();
//            LatLng latLng = new LatLng(latitude, longitude);
//            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 18);
//            mapa.clear();
//            mapa.addMarker(new MarkerOptions().position(latLng).title("Tú ubicación"));
//            mapa.animateCamera(cameraUpdate);
//        }
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
//        refreshLocationByGPS();
        updateMap();
    }

    private void updateMap(){
        Log.d(TAG, "updateMap() called ubicationFromGooglePlay => "+ubicationFromGooglePlay);
        if (!isPaused){
            if (ubicationFromGooglePlay){
                refreshByServices(locationFromServices);
            }else{
                refreshLocationByGPS();
            }
        }
        new CountDownTimer(2000, 500) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                updateMap();
            }

        }.start();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
//        Log.d(TAG, "onMapReady() called with: googleMap = [" + googleMap + "]");
        mapa = googleMap;
    }
    private void updateMap(double lat, double lon){
        try{
            latitude  = lat;
            longitude = lon;
            LatLng latLng = new LatLng(lat, lon);
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 16);
            mapa.clear();
            mapa.addMarker(new MarkerOptions().position(latLng).title("Tu ubicación"));
            mapa.animateCamera(cameraUpdate);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void refreshLocationByGPS(){
//        Log.d(TAG, "refreshLocationByGPS() called");
        if (mapa != null){
            GPSController = new GPSController(HomeActivity.this);
            if (GPSController.canGetLocation() && GPSController.getLoc() != null) {
                updateMap(GPSController.getLatitude(),GPSController.getLongitude());
            }
        }

    }

    private void eventoUsuario(String evento){
        try{
            if (latitude != 0 && longitude != 0){
                showDialog("LANIX", "Enviando información", null);
                Calendar c = Calendar.getInstance();
                if (evento.equals(HORAENTRADA)){
                    eventoString = "Se ha registrado tu hora de entrada a ";
                }else if(evento.equals(HORAENTRADACOMIDA)){
                    eventoString = "Se ha registrado tu hora de entrada después de comida a ";
                }else if(evento.equals(HORASALIDACOMIDA)){
                    eventoString = "Se ha registrado tu hora de salida a comer a ";
                }else if(evento.equals(HORASALIDA)){
                    eventoString = "Se ha registrado tu salida a ";
                }
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                dateTime = df.format(c.getTime());
//                Log.e(TAG, "eventoUsuario: "+dateTime );
                EventEntradaRequest eventEntradaRequest = new EventEntradaRequest(Double.toString(latitude),
                        Double.toString(longitude),
                        authController.getUser().getSesion().getIdentificador(),
                        evento,
                        dateTime );
//                Log.e(TAG, "eventoUsuario: "+eventEntradaRequest.toString() );
//                NetworkController networkController = LanixApplication.getInstance().getNetworkController();
//                JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST,
//                        networkController.getServiceUrl(eventEntradaRequest.TAG),
//                        eventEntradaRequest.toJson(),
//                        this,
//                        this);
//                networkController.getQueue().add(jsObjRequest);
                LanixApplication.getInstance().getNetworkController().requestData(eventEntradaRequest,Request.Method.POST,this,this);
            }else{
//                Log.e(TAG, "eventoUsuario: AHORITA NO JOVEN");
            }

        }catch (Exception e){e.printStackTrace();}
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.btn_gps:{
                ubicationFromGooglePlay = !ubicationFromGooglePlay;
                break;
            }

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
                eventoUsuario(HORASALIDA);
                break;
            }
            default:{
//                Log.e(TAG, "onClick: COCAS" );
            }
        }
    }

    private void showDialog(String titulo, String contenido, String positive){
//        Log.d(TAG, "showDialog() called with: titulo = [" + titulo + "], contenido = [" + contenido + "], positive = [" + positive + "]");
        if (positive == null){
            notificacionDialog = new MaterialDialog.Builder(this)
                    .title(titulo)
                    .content(contenido)
                    .icon(getDrawable(R.drawable.logo_lanix))
                    .show();
        }
    }

    private void dimissDialog(String titulo, String contenido, String positive){
//        Log.d(TAG, "dimissDialog() called with: titulo = [" + titulo + "], contenido = [" + contenido + "], positive = [" + positive + "]");
        notificacionDialog.dismiss();
        notificacionDialog = new MaterialDialog.Builder(this)
                .title(titulo)
                .content(contenido)
                .positiveText(positive)
                .icon(getDrawable(R.drawable.rsz_logo_lanix))
                .show();

    }
    @Override
    public void onResponse(JSONObject response) {
//        Log.e(TAG, "onResponse: "+response );
        Gson g = new Gson();
        AsistenciaResponse asistenciaResponse = g.fromJson(response.toString(), AsistenciaResponse.class);
        if (asistenciaResponse.getError().getNo() == 0){
            dimissDialog("LANIX",eventoString+dateTime,POSITIVE_MSG);
        }else{
            dimissDialog("LANIX","Ooops! Parece que tenemos un problema, por favor vuelve a intentarlo ","Ok");
            authController.syncDevice();
        }

//        Log.w(TAG, "onResponse() called with: response = [" + response + "]");
    }

    @Override
    public void onBackPressed() {
//        Log.d(TAG, "onBackPressed() called");
        if (activeFragment != null ){
            goHome();
        }
//        super.onBackPressed();
    }

    public void goHome(){
//        activeFragment = new MenuFragment();
//        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//        ft.replace(R.id.fl_container, activeFragment).commit();
//        llAsistencia.setVisibility(View.GONE);
//        frameLayout.setVisibility(View.VISIBLE);
        Intent home = new Intent(this, HomeActivity.class);
        startActivity(home);
    }
    @Override
    public void onErrorResponse(VolleyError error) {
//        Log.e(TAG, "onErrorResponse() called with: error = [" + error + "]");
        dimissDialog("LANIX","Ooops! Parece que tenemos un problema, por favor vuelve a intentarlo ","Ok");
    }

    public void hideFragmentContainer(){
//        Log.d(TAG, "hideFragmentContainer() called");
        frameLayout.setVisibility(View.GONE);
        llAsistencia.setVisibility(View.VISIBLE);
    }

    public VentasSecondStepFragment getVentasSecondStepFragment() {
        return ventasSecondStepFragment;
    }

    public void setVentasSecondStepFragment(VentasSecondStepFragment ventasSecondStepFragment) {
        this.ventasSecondStepFragment = ventasSecondStepFragment;
    }

    public VentasFirstStepFragment getVentasFirstStepFragment() {
        return ventasFirstStepFragment;
    }

    public void setVentasFirstStepFragment(VentasFirstStepFragment ventasFirstStepFragment) {
        this.ventasFirstStepFragment = ventasFirstStepFragment;
    }

    public VentasContainerFragment getVentasContainerFragment() {
        return ventasContainerFragment;
    }

    public void setVentasContainerFragment(VentasContainerFragment ventasContainerFragment) {
        this.ventasContainerFragment = ventasContainerFragment;
    }

    public Step getVentasThirdStepFragmentFragment() {
        return ventasThirdStepFragmentFragment;
    }

    public void setVentasThirdStepFragmentFragment(VentasThirdStepFragment ventasThirdStepFragmentFragment) {
        this.ventasThirdStepFragmentFragment = ventasThirdStepFragmentFragment;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try{
//            Log.d(TAG, "onActivityResult() called with: requestCode = [" + requestCode + "], resultCode = [" + resultCode + "], data = [" + data + "]");
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if(result != null) {
                if(result.getContents() == null) {
//                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
                } else {
//                Toast.makeText(this, "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                    ventasSecondStepFragment.setTvImei(result.getContents());
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

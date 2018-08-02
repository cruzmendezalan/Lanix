package com.devops.krakenlabs.lanix;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.crashlytics.android.Crashlytics;
import com.devops.krakenlabs.lanix.base.LanixApplication;
import com.devops.krakenlabs.lanix.controllers.AuthController;
import com.devops.krakenlabs.lanix.controllers.GPSController;
import com.devops.krakenlabs.lanix.controllers.ImageManager;
import com.devops.krakenlabs.lanix.listeners.SessionNotifier;
import com.devops.krakenlabs.lanix.models.EventEntradaRequest;
import com.devops.krakenlabs.lanix.models.asistencia.AsistenciaResponse;
import com.devops.krakenlabs.lanix.models.venta.VentaResponse;
import com.devops.krakenlabs.lanix.models.venta.VentasRequestt;
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
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import io.fabric.sdk.android.Fabric;
import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class HomeActivity extends FragmentActivity implements OnMapReadyCallback,
    View.OnClickListener, Response.Listener<JSONObject>, Response.ErrorListener, SessionNotifier {
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

  private static final String SPF_NAME = "SALES"; // <--- Account
  private static final String SALES_ID = "SALES_ID_";
  private static final String SALES_NU = "SALES_NU";


  private static final String SPF_NAMEL = "vidslogin"; // <--- Account
  private static final String USERNAME = "username"; // <--- To save username
  private static final String PASSWORD = "password"; // <--- To save password
  private static final String SAVED = "saved"; // <--- To save password

  private ImageView ivPhoto;
  private HashMap<String, Bitmap> photosArray;
  private PhotoNotifier photoNotifier;
  private SimpleDateFormat sdf;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Fabric.with(this, new Crashlytics());
    setContentView(R.layout.f_asistencia);
    mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    SupportMapFragment mapFragment =
        (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
    isPaused = false;
    mapFragment.getMapAsync(this);
    permissions.add(ACCESS_FINE_LOCATION);
    permissions.add(ACCESS_COARSE_LOCATION);

    permissionsToRequest = findUnAskedPermissions(permissions);
    // get the permissions we have asked for before but are not granted..
    // we will store this in a global list to access later.
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      if (permissionsToRequest.size() > 0)
        requestPermissions(
            (String[]) permissionsToRequest.toArray(new String[permissionsToRequest.size()]),
            ALL_PERMISSIONS_RESULT);
    }
    authController = LanixApplication.getInstance().getAuthController();
    authController.setmContext(this);
    /**
     * init ui
     */
    cardEntrada = findViewById(R.id.cv_entrada);
    cardSalidaC = findViewById(R.id.cv_salida_comer);
    cardRegresoC = findViewById(R.id.cv_regreso_comer);
    cardSalida = findViewById(R.id.cv_salida);
    promotorName = findViewById(R.id.tv_promotor_name);
    btnGps = findViewById(R.id.btn_gps);
    ivPhoto = findViewById(R.id.iv_photo);
    btnGps.setOnClickListener(this);
    try {
      promotorName.setText(authController.getUser().getPromotor().getNombres() + " "
          + authController.getUser().getPromotor().getApellidoPaterno());


    } catch (Exception e) {
      e.printStackTrace();

    }
    cardEntrada.setOnClickListener(this);
    cardSalidaC.setOnClickListener(this);
    cardRegresoC.setOnClickListener(this);
    cardSalida.setOnClickListener(this);
    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
        && ActivityCompat.checkSelfPermission(this,
            android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
      return;
    }
    mFusedLocationClient.getLastLocation().addOnSuccessListener(this,
        new OnSuccessListener<Location>() {
          @Override
          public void onSuccess(Location location) {
            if (location != null) {
              locationFromServices = location;
            }
          }
        });
    frameLayout = findViewById(R.id.fl_container);
    frameLayout.setVisibility(View.VISIBLE);
    llAsistencia = findViewById(R.id.ll_asistencia);
    activeFragment = new MenuFragment();
    photosArray = new HashMap<>();

    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
    ft.add(R.id.fl_container, activeFragment).commit();

  }

  private void refreshByServices(Location location) {
    try {
      if (location != null) {
        updateMap(location.getLatitude(), location.getLongitude());
      }
    } catch (Exception e) {
      e.printStackTrace();
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
    try {
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
                showMessageOKCancel(
                    "Estos permisos son necesarios para la aplicación. Por favor permite el acceso.",
                    new DialogInterface.OnClickListener() {
                      @Override
                      public void onClick(DialogInterface dialog, int which) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                          requestPermissions((String[]) permissionsRejected
                              .toArray(new String[permissionsRejected.size()]),
                              ALL_PERMISSIONS_RESULT);
                        }
                      }
                    });
                return;
              }
            }
          }

          break;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
    new AlertDialog.Builder(HomeActivity.this).setMessage(message)
        .setPositiveButton("OK", okListener).setNegativeButton("Cancelar", null).create().show();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (GPSController != null) {
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
    updateMap();
    relogin();
  }

  private void updateMap() {
    try {
      // Log.d(TAG, "updateMap() called ubicationFromGooglePlay => "+ubicationFromGooglePlay);
      if (!isPaused) {
        if (ubicationFromGooglePlay) {
          refreshByServices(locationFromServices);
        } else {
          refreshLocationByGPS();
        }
      }
      new CountDownTimer(2000, 500) {
        public void onTick(long millisUntilFinished) {}

        public void onFinish() {
          updateMap();
        }

      }.start();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void onMapReady(GoogleMap googleMap) {
    // Log.d(TAG, "onMapReady() called with: googleMap = [" + googleMap + "]");
    mapa = googleMap;
  }

  private void updateMap(double lat, double lon) {
    try {
      latitude = lat;
      longitude = lon;
      LatLng latLng = new LatLng(lat, lon);
      CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 16);
      mapa.clear();
      mapa.addMarker(new MarkerOptions().position(latLng).title("Tu ubicación"));
      mapa.animateCamera(cameraUpdate);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void refreshLocationByGPS() {
    try {
      // Log.d(TAG, "refreshLocationByGPS() called");
      if (mapa != null) {
        GPSController = new GPSController(HomeActivity.this);
        if (GPSController.canGetLocation() && GPSController.getLoc() != null) {
          updateMap(GPSController.getLatitude(), GPSController.getLongitude());
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void eventoUsuario(String evento) {
    try {
      if (latitude != 0 && longitude != 0) {
        showDialog("LANIX", "Enviando información", null);
        Calendar c = Calendar.getInstance();
        if (evento.equals(HORAENTRADA)) {
          eventoString = "Se ha registrado tu hora de entrada a ";
        } else if (evento.equals(HORAENTRADACOMIDA)) {
          eventoString = "Se ha registrado tu hora de entrada después de comida a ";
        } else if (evento.equals(HORASALIDACOMIDA)) {
          eventoString = "Se ha registrado tu hora de salida a comer a ";
        } else if (evento.equals(HORASALIDA)) {
          eventoString = "Se ha registrado tu salida a ";
        }
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        dateTime = df.format(c.getTime());
        // Log.e(TAG, "eventoUsuario: "+dateTime );
        EventEntradaRequest eventEntradaRequest =
            new EventEntradaRequest(Double.toString(latitude), Double.toString(longitude),
                authController.getUser().getSesion().getIdentificador(), evento, dateTime);
        LanixApplication.getInstance().getNetworkController()
            .requestData(eventEntradaRequest, Request.Method.POST, this, this);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void onClick(View view) {
    int id = view.getId();
    switch (id) {
      case R.id.btn_gps: {
        // ubicationFromGooglePlay = !ubicationFromGooglePlay;
        dispatchTakePictureIntent();
        break;
      }

      case R.id.cv_entrada: {
        eventoUsuario(HORAENTRADA);
        break;
      }
      case R.id.cv_salida_comer: {
        eventoUsuario(HORASALIDACOMIDA);
        break;
      }
      case R.id.cv_regreso_comer: {
        eventoUsuario(HORAENTRADACOMIDA);
        break;
      }
      case R.id.cv_salida: {
        eventoUsuario(HORASALIDA);
        break;
      }
      default: {
        // Log.e(TAG, "onClick: COCAS" );
      }
    }
  }

  private void showDialog(String titulo, String contenido, String positive) {
    // Log.d(TAG, "showDialog() called with: titulo = [" + titulo + "], contenido = [" + contenido +
    // "], positive = [" + positive + "]");
    try {
      if (positive == null) {
        notificacionDialog =
            new MaterialDialog.Builder(this).title(titulo).content(contenido)
                .icon(getDrawable(R.drawable.logo_lanix)).show();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void dimissDialog(String titulo, String contenido, String positive) {
    try {
      notificacionDialog.dismiss();
      notificacionDialog =
          new MaterialDialog.Builder(this).title(titulo).content(contenido).positiveText(positive)
              .icon(getDrawable(R.drawable.rsz_logo_lanix)).show();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void onResponse(JSONObject response) {
    Gson g = new Gson();
    AsistenciaResponse asistenciaResponse =
        g.fromJson(response.toString(), AsistenciaResponse.class);
    relogin();
    if (asistenciaResponse.getError().getNo() == 0) {
      dimissDialog("LANIX", eventoString + dateTime, POSITIVE_MSG);
    } else {
      dimissDialog("LANIX", "Verifica tu conexión a internet. ", "Ok");
    }
  }

  public void relogin() {
    try {
      SharedPreferences loginPreferences = getSharedPreferences(SPF_NAMEL, Context.MODE_PRIVATE);
      authController.setSessionNotifier(null);
      authController.login(loginPreferences.getString(USERNAME, ""),
          loginPreferences.getString(PASSWORD, ""));
      authController.syncDevice();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void onBackPressed() {
    if (activeFragment != null) {
      goHome();
    }
  }

  public void goHome() {
    try {
      Intent home = new Intent(this, HomeActivity.class);
      startActivity(home);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void onErrorResponse(VolleyError error) {
    dimissDialog("LANIX", "Ooops! Parece que tenemos un problema, por favor vuelve a intentarlo ",
        "Ok");
  }

  public void hideFragmentContainer() {
    try {
      frameLayout.setVisibility(View.GONE);
      llAsistencia.setVisibility(View.VISIBLE);
    } catch (Exception e) {
      e.printStackTrace();
    }
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

  public void setVentasThirdStepFragmentFragment(
      VentasThirdStepFragment ventasThirdStepFragmentFragment) {
    this.ventasThirdStepFragmentFragment = ventasThirdStepFragmentFragment;
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    Log.d(TAG, "onActivityResult() called with: requestCode = [" + requestCode
        + "], resultCode = [" + resultCode + "], data = [" + data + "]");
    super.onActivityResult(requestCode, resultCode, data);
    try {
      if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
        try {
          if (photoNotifier != null) {
            photoNotifier.photoTaked(data.getData());
          }
          // Bundle extras = data.getExtras();
          // Bitmap imageBitmap = (Bitmap) extras.get("data");
          //
          // if(null != imageBitmap){
          // if (photoNotifier != null){
          // photoNotifier.photoTaked(data.getData());
          // }
          // }
          // ivPhoto.setImageBitmap(imageBitmap);
          return;

        } catch (Exception e) {
          e.printStackTrace();
        }

      }

      IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
      if (result != null) {
        if (result.getContents() == null) {
        } else {
          ventasSecondStepFragment.setTvImei(result.getContents());
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void uploadImage(Uri bitmap, final String imageName) {
    Log.d(TAG, "uploadImage() called with: imageUri = [" + bitmap + "]");
    try {

      // int byteSize = bitmap.getRowBytes() * bitmap.getHeight();
      // ByteBuffer byteBuffer = ByteBuffer.allocate(byteSize);
      // bitmap.copyPixelsToBuffer(byteBuffer);
      //
      // byte[] byteArray = byteBuffer.array();
      // final InputStream imageStream = new ByteArrayInputStream(byteArray);
      final InputStream imageStream = getContentResolver().openInputStream(bitmap);
      final int imageLength = imageStream.available();

      final Handler handler = new Handler();

      Thread th = new Thread(new Runnable() {
        public void run() {
          try {
            ImageManager.UploadImage(imageStream, imageLength, imageName);

            handler.post(new Runnable() {

              public void run() {
                Toast.makeText(HomeActivity.this, "Imagen enviada. Nombre = " + imageName,
                    Toast.LENGTH_SHORT).show();
              }
            });
          } catch (Exception ex) {
            ex.printStackTrace();
            final String exceptionMessage = ex.getMessage();
            handler.post(new Runnable() {
              public void run() {
                Toast.makeText(HomeActivity.this, "error", Toast.LENGTH_SHORT).show();
              }
            });
          }
        }
      });
      th.start();
    } catch (Exception ex) {
      ex.printStackTrace();
      Toast.makeText(this, ex.getMessage(), Toast.LENGTH_SHORT).show();
    }
  }


  public boolean isNetworkOnline() {
    boolean status = false;
    try {
      ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
      NetworkInfo netInfo = cm.getNetworkInfo(0);
      if (netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED) {
        status = true;
      } else {
        netInfo = cm.getNetworkInfo(1);
        if (netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED)
          status = true;
      }
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    return status;

  }

  public void storeSale(String sale) {
    try {
      SharedPreferences loginPreferences = getSharedPreferences(SPF_NAME, Context.MODE_PRIVATE);
      int saleNumber = loginPreferences.getInt(SALES_NU, 0);
      loginPreferences.edit().putString(SALES_ID + saleNumber, sale)
          .putInt(SALES_NU, 1 + saleNumber).commit();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void sendSales() {
    try {
      if (isNetworkOnline()) {
        SharedPreferences loginPreferences =
            getSharedPreferences("vidslogin", Context.MODE_PRIVATE);
        AuthController.getInstance(this).setSessionNotifier(this);
        AuthController.getInstance(this).login(loginPreferences.getString(USERNAME, ""),
            loginPreferences.getString(PASSWORD, ""));
      } else {
        Toast.makeText(this, "No hay conexión a internet :(", Toast.LENGTH_LONG).show();
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public int getNotSendedSales() {
    try {
      SharedPreferences loginPreferences = getSharedPreferences(SPF_NAME, Context.MODE_PRIVATE);
      return loginPreferences.getInt(SALES_NU, 0);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return 0;
  }

  @Override
  public void sessionComplete() {
    try {
      SharedPreferences loginPreferences = getSharedPreferences(SPF_NAME, Context.MODE_PRIVATE);
      int saleNumber = loginPreferences.getInt(SALES_NU, 0);
      if (saleNumber == 0) {
        Log.e(TAG, "sendSales: No hay ventas por enviar");
      } else {
        Gson g = new Gson();
        for (int i = 0; i < saleNumber; i++) {
          Log.w(TAG, "StoredSales => " + loginPreferences.getString(SALES_ID + i, ""));
          VentasRequestt r =
              g.fromJson(loginPreferences.getString(SALES_ID + i, ""), VentasRequestt.class);
          Log.e(TAG, "sessionComplete: " + r.toString());
          LanixApplication.getInstance().getNetworkController()
              .requestData(r, Request.Method.POST, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                  Log.d(TAG, "onResponse() called with: response = [" + response + "]");
                  if (response != null) {
                    Gson g = new Gson();
                    try {
                      VentaResponse ve = g.fromJson(response.toString(), VentaResponse.class);
                      if (ve.getVentaId() > 0) {
                        showVenta("Se a registrado correctamente tu venta con el folio "
                            + ve.getVentaId());
                      }
                    } catch (Exception e) {
                      e.printStackTrace();
                    }
                  }
                }
              }, new Response.ErrorListener() {
                /**
                 * Callback method that an error has been occurred with the provided error code and
                 * optional user-readable message.
                 *
                 * @param error
                 */
                @Override
                public void onErrorResponse(VolleyError error) {
                  Log.d(TAG, "onErrorResponse() called with: error = [" + error + "]");
                }
              });
        }
        loginPreferences.edit().putInt(SALES_NU, 0).commit();
        onResume();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void showVenta(String msg) {
    try {
      Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  static final int REQUEST_IMAGE_CAPTURE = 10001;

  public void dispatchTakePictureIntent() {
    try {
      Log.d(TAG, "dispatchTakePictureIntent() called");
      // Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
      // if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
      // startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
      // }
      Intent intent = new Intent();
      intent.setType("image/*");
      intent.setAction(Intent.ACTION_GET_CONTENT);
      startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_IMAGE_CAPTURE);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public interface PhotoNotifier {
    void photoTaked(Uri photo);
  }

  public void setPhotoNotifier(PhotoNotifier photoNotifier) {
    this.photoNotifier = photoNotifier;
  }
}

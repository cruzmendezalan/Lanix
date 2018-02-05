package com.devops.krakenlabs.lanix.ventas;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.devops.krakenlabs.lanix.HomeActivity;
import com.devops.krakenlabs.lanix.R;
import com.devops.krakenlabs.lanix.base.LanixApplication;
import com.devops.krakenlabs.lanix.models.venta.VentaResponse;
import com.google.gson.Gson;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.VerificationError;

import org.json.JSONObject;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by Alan Giovani Cruz Méndez on 17/01/18 22:34.
 * cruzmendezalan@gmail.com
 */

public class VentasThirdStepFragment extends Fragment implements Step, Response.ErrorListener {
    private static final String TAG = VentasThirdStepFragment.class.getSimpleName();
    private View rootView;
    private ListView lvProductos;
    private Button btnConfirmarVenta;
    public static final String channelId = "4565";
    public static final CharSequence channelName = "LanixChannel";
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
        try{
            HomeActivity ho = (HomeActivity) getActivity();
//            LanixApplication lanixApplication   = LanixApplication.getInstance();
//            JsonObjectRequest jsonObjectRequest  = new JsonObjectRequest(Request.Method.POST,
//                    lanixApplication.getNetworkController().getServiceUrl(VentasRequestt.class.getSimpleName()),
//                    ho.getVentasFirstStepFragment().getVentaRequest().toJson(),
//                    new Response.Listener<JSONObject>() {
//                        @Override
//                        public void onResponse(JSONObject response) {
//                            Log.d(TAG, "onResponse() called with: response = [" + response + "]");
//                            if (response != null){
//                                Gson g = new Gson();
//                                try{
//                                    VentaResponse ve = g.fromJson(response.toString(),VentaResponse.class);
//                                    if (ve.getVentaId() > 0){
//                                        sendNotification("Venta registrada", "Se a registrado correctamente tu venta con el folio "+ve.getVentaId());
//                                    }else{
//                                        sendNotification("¡Ooops!", "Al parecer hemos tenido un problema al registrar tu venta");
//                                    }
//                                }catch (Exception e){
//                                    e.printStackTrace();
//                                    sendNotification("¡Ooops!", "Al parecer hemos tenido un problema al registrar tu venta");
//                                }
//                            }
//                        }
//                    },this);
//            lanixApplication.getNetworkController().getQueue().add(jsonObjectRequest);
            LanixApplication.getInstance().getNetworkController().requestData(ho.getVentasFirstStepFragment().getVentaRequest(),Request.Method.POST,new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d(TAG, "onResponse() called with: response = [" + response + "]");
                    if (response != null){
                        Gson g = new Gson();
                        try{
                            VentaResponse ve = g.fromJson(response.toString(),VentaResponse.class);
                            if (ve.getVentaId() > 0){
                                sendNotification("Venta registrada", "Se a registrado correctamente tu venta con el folio "+ve.getVentaId());
                            }else{
                                sendNotification("¡Ooops!", "Al parecer hemos tenido un problema al registrar tu venta");
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                            sendNotification("¡Ooops!", "Al parecer hemos tenido un problema al registrar tu venta");
                        }
                    }
                }
            },this);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void notificarVenta() {
        HomeActivity ho = (HomeActivity) getActivity();
        Snackbar sn = Snackbar.make(rootView, "Se a completado el proceso de la venta", Snackbar.LENGTH_LONG);
        View snackBarView = sn.getView();
        snackBarView.setBackgroundColor(getResources().getColor(R.color.white));
        sn.show();
        Log.e(TAG, "notificarVenta: validar venta" );

    }

    private void sendNotification(String title, String text) {
        makeAChannel();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            Notification notification  = new Notification.Builder(getContext())
                    .setCategory(Notification.CATEGORY_MESSAGE)
                    .setContentTitle(title)
                    .setContentText(text)
                    .setAutoCancel(true)
                    .setChannelId(channelId)
                    .setSmallIcon(R.drawable.ic_create_24dp)
                    .setVisibility(Notification.VISIBILITY_PUBLIC).build();
            NotificationManager notificationManager =
                    (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(1212212, notification );
        }else{
            Notification notification  = new Notification.Builder(getContext())
                    .setCategory(Notification.CATEGORY_MESSAGE)
                    .setContentTitle(title)
                    .setContentText(text)
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.ic_create_24dp)
                    .setVisibility(Notification.VISIBILITY_PUBLIC).build();
            NotificationManager notificationManager =
                    (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(1212212, notification );
        }
        Intent home = new Intent(getContext(), HomeActivity.class);
        startActivity(home);
    }

    private void makeAChannel(){
        NotificationManager notificationManager =
                (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        int importance = NotificationManager.IMPORTANCE_LOW;
        NotificationChannel notificationChannel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel(channelId, channelName, importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            notificationManager.createNotificationChannel(notificationChannel);
        }

    }

    private void initListView() {
        Log.d(TAG, "initListView() called");
        try{
            HomeActivity ho = (HomeActivity) getActivity();
            String[] values = new String[ho.getVentasFirstStepFragment().getVentaRequest().getProductos().size()];
            for (int i = 0; i < ho.getVentasFirstStepFragment().getVentaRequest().getProductos().size(); i++) {
                values[i] = ho.getVentasFirstStepFragment().getVentaRequest().getProductos().get(i).getModeloName();
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
        hideSoftKeyboard();
//        initListView();
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

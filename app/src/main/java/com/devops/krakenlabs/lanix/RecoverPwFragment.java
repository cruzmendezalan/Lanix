package com.devops.krakenlabs.lanix;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.devops.krakenlabs.lanix.base.LanixApplication;
import com.devops.krakenlabs.lanix.controllers.NetworkController;
import com.devops.krakenlabs.lanix.models.session.Contrasenia;
import com.devops.krakenlabs.lanix.models.session.RecoverPassword;

import org.json.JSONObject;

import java.util.regex.Pattern;

;

public class RecoverPwFragment extends DialogFragment implements Response.ErrorListener, Response.Listener<JSONObject>{
    private static final String TAG = RecoverPwFragment.class.getSimpleName();
    private View rootView;
    private TextView tvEmail;
    private Button btnSendCode;
    private TextView tvCode;
    private TextView tvPw;
    private TextView tvPw2;
    private Button btnChangePw;

    public RecoverPwFragment() {
        // Required empty public constructor
    }

    public static RecoverPwFragment newInstance() {
        RecoverPwFragment fragment = new RecoverPwFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView    = inflater.inflate(R.layout.f_recover_pw, container, false);
        tvEmail     = rootView.findViewById(R.id.actv_email);
        btnSendCode = rootView.findViewById(R.id.btn_send_code);
        tvCode      = rootView.findViewById(R.id.actv_code);
        tvPw        = rootView.findViewById(R.id.actv_pw);
        tvPw2       = rootView.findViewById(R.id.actv_pw2);
        btnChangePw = rootView.findViewById(R.id.btn_send_new_pw);
        initUI();

        return rootView;
    }

    private void initUI() {
        tvEmail.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE){
                    sendCode();
                    return false;
                }
                return false;
            }
        });

        btnSendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendCode();
            }
        });
        btnChangePw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendNewPW();
            }
        });
    }

    private void sendNewPW() {
        Contrasenia contrasenia = new Contrasenia(tvPw.getText().toString(),tvPw2.getText().toString(),"",tvCode.getText().toString());
        NetworkController networkController = LanixApplication.getInstance().getNetworkController();
        JsonObjectRequest requestChangePw = new JsonObjectRequest(Request.Method.POST,
                networkController.getServiceUrl(Contrasenia.TAG),
                contrasenia.toJson(),
                this,
                this);
        networkController.getQueue().add(requestChangePw);
    }

    private void sendCode() {
        hideSoftKeyboard();
        if (checkEmail(tvEmail.getText().toString())){
            NetworkController networkController = LanixApplication.getInstance().getNetworkController();
            JsonObjectRequest requestChangePw = new JsonObjectRequest(Request.Method.POST,
                    networkController.getServiceUrl(RecoverPassword.TAG),
                    new RecoverPassword(tvEmail.getText().toString()).toJson(),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d(TAG, "onResponse() called with: response = [" + response + "]");
                            showNotification();
                        }
                    },
                    this);
            networkController.getQueue().add(requestChangePw);
        }else{
            tvEmail.setError("Oops! Al parecer has introducido un correo erroneo");
            tvEmail.requestFocus();
        }
        Log.d(TAG, "sendCode() called");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
    private boolean checkEmail(String email) {
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }

    public static final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.d(TAG, "onErrorResponse() called with: error = [" + error + "]");
    }

    @Override
    public void onResponse(JSONObject response) {
        Log.d(TAG, "onResponse() called with: response = [" + response + "]");
        this.dismiss();
    }

    public void hideSoftKeyboard() {
        View view = getView();
        if (view != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity()
                    .getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    private void showNotification(){
        Snackbar sn = Snackbar.make(rootView, getString(R.string.msg_recover_pw), Snackbar.LENGTH_LONG);
        View snackBarView = sn.getView();
        snackBarView.setBackgroundColor(getResources().getColor(R.color.white));
        sn.show();
    }
}

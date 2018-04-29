package com.devops.krakenlabs.lanix;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.devops.krakenlabs.lanix.base.LanixApplication;
import com.devops.krakenlabs.lanix.controllers.AuthController;
import com.devops.krakenlabs.lanix.listeners.SessionNotifier;
import com.devops.krakenlabs.lanix.privacidad.AvisoDePrivacidadFragment;

import java.util.ArrayList;
import java.util.List;

import io.fabric.sdk.android.Fabric;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor>, SessionNotifier, ControllerNotifier, ActivityCompat.OnRequestPermissionsResultCallback {
    public  static       String TAG = LoginActivity.class.getSimpleName();
    private static final String SPF_NAME = "vidslogin"; //  <--- Account
    private static final String USERNAME = "username";  //  <--- To save username
    private static final String PASSWORD = "password";  //  <--- To save password
    private static final String SAVED    = "saved";  //  <--- To save password
    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;
    private static final int REQUEST_READ_PHONE_STATE = 1;

    private AuthController authController;
    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private CheckBox cbRememberMe;
    private View mProgressView;
    private View mLoginFormView;
    private Button mEmailSignInButton;
    private TextView tvChangePw;
    private LinearLayout llSplash;
    private TextView tvPrivacidad;
    private TextView tvVersion;


    // The request code used in ActivityCompat.requestPermissions()
    // and returned in the Activity's onRequestPermissionsResult()
    int PERMISSION_ALL = 10001;
    String[] PERMISSIONS = {
            android.Manifest.permission.WRITE_CONTACTS,
            android.Manifest.permission.READ_CONTACTS,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            android.Manifest.permission.READ_PHONE_STATE,
            android.Manifest.permission.INTERNET,
            android.Manifest.permission.CAMERA};


    private static Double TIME_SPLASH       = 10.0; //MILISEGUNDOS
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
//        if(!hasPermissions(this, PERMISSIONS)){
//            Log.w(TAG, "onCreate: requestPermissions" );
//
//        }else{
//
//        }
        Log.e(TAG, "onCreate: PERMISOS OTORGADOS" );
        authController = AuthController.getInstance(this);
        authController.syncDevice();


        setContentView(R.layout.activity_login);
        authController = AuthController.getInstance(this);
        authController.setmContext(this);
        authController.setControllerNotifier(this);
        // Set up the login form.
        mEmailView = findViewById(R.id.user);
        populateAutoComplete();

        mPasswordView = findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
        llSplash           = findViewById(R.id.ll_splash);
        mEmailSignInButton = findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView  = findViewById(R.id.login_progress);
        cbRememberMe   = findViewById(R.id.rempasswordcheckbox);
        authController = LanixApplication.getInstance().getAuthController();

        tvVersion = findViewById(R.id.tv_version);

        tvChangePw = findViewById(R.id.tv_change_pw);
        tvChangePw.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                initChangePassword();
            }
        });
        SharedPreferences loginPreferences = getSharedPreferences(SPF_NAME,
                Context.MODE_PRIVATE);
        if (loginPreferences.getBoolean(SAVED,false)){
            mEmailView.append(loginPreferences.getString(USERNAME, ""));
            mPasswordView.setText(loginPreferences.getString(PASSWORD, ""));
            cbRememberMe.setChecked(true);
        }

        
        tvPrivacidad = findViewById(R.id.tv_privacidad);
        tvPrivacidad.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                openPrivacidad();
            }
        });

        Fabric.with(this, new Crashlytics());
        tvVersion.setText("V "+requestVersion());
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
                    return true;
                }else{
                    Log.e(TAG, "hasPermissions: APP DONT HAVE THIS PERMISSION "+permission );
                }
            }
        }
        return false;
    }

    public void hideSoftKeyboard() {
        Log.d(TAG, "hideSoftKeyboard() called");
        try{
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void openPrivacidad() {
        Log.d(TAG, "openPrivacidad() called");
        AvisoDePrivacidadFragment recoverPwFragment = new AvisoDePrivacidadFragment();
        recoverPwFragment.show(getSupportFragmentManager(),recoverPwFragment.getClass().getSimpleName());
    }

    private void initChangePassword() {
        Log.d(TAG, "initChangePassword() called");
        RecoverPwFragment recoverPwFragment = RecoverPwFragment.newInstance();
        recoverPwFragment.show(getSupportFragmentManager(),recoverPwFragment.getClass().getSimpleName());
    }


    private void populateAutoComplete() {
        Log.d(TAG, "populateAutoComplete() called");
    }



    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult() called with: requestCode = [" + requestCode + "], permissions = [" + permissions + "], grantResults = [" + grantResults + "]");
        authController.syncDevice();
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
        if (requestCode == REQUEST_READ_PHONE_STATE) {
//            authController.syncDevice();
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        Log.d(TAG, "attemptLogin() called");
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email    = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;
        Log.e(TAG, "attemptLogin: "+email+ "   "+password );
        // Check for a valid password, if the user entered one.
        if (!isPasswordValid(password)) {
            mEmailView.setError(getString(R.string.error_invalid_password));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
            return;
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            hideKeyboard();
            authController.setSessionNotifier(this);
            ArrayList<String> localValidations = authController.login(email,password);
            if (localValidations == null){
                //El request se esta solicitando
            }else{
                //Mostrar errores
            }
        }
    }

    private void hideKeyboard(){
        Log.d(TAG, "hideKeyboard() called");
        try{
            mEmailView.clearFocus();
            mPasswordView.clearFocus();
            mEmailSignInButton.requestFocus();
            if(getCurrentFocus()!=null) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        Log.d(TAG, "addEmailsToAutoComplete() called with: emailAddressCollection = [" + emailAddressCollection + "]");
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }

    @Override
    public void sessionComplete() {
        Log.e(TAG, "sessionComplete() called");
        showProgress(false);
        hideKeyboard();
        // TODO: 12/11/17 implementar si falló la sesion
        try{
            if (authController.getUser() != null && (authController.getUser().getError().getNo() == 0)){
                llSplash.setVisibility(View.VISIBLE);
                if (cbRememberMe.isChecked()){
                    saveCredentials(mEmailView.getText().toString(), mPasswordView.getText().toString(), true);
                }else{
                    clearCredentials();
                    saveCredentials(mEmailView.getText().toString(), mPasswordView.getText().toString(), false);
                }
                Intent home = new Intent(this, HomeActivity.class);
                startActivity(home);
            }else{
                mEmailView.setError(getString(R.string.error_invalid_password));
                mEmailView.requestFocus();
            }
         }catch (Exception e){
           e.printStackTrace();
        }
    }

    /**
     * Save accounts
     * @param user
     * @param pw
     */
    private void saveCredentials(String user, String pw, Boolean isPermanent) {
        Log.d(TAG, "saveCredentials() called with: user = [" + user + "], pw = [" + pw + "]");
        SharedPreferences loginPreferences = getSharedPreferences(SPF_NAME, Context.MODE_PRIVATE);
        loginPreferences.edit().putString(USERNAME, user).putString(PASSWORD, pw).putBoolean(SAVED,isPermanent).commit();
    }

    private void clearCredentials(){
        SharedPreferences loginPreferences = getSharedPreferences(SPF_NAME, Context.MODE_PRIVATE);
        loginPreferences.edit().clear().commit();
    }

    @Override
    public void tokenDeviceComplete() {
        Log.d(TAG, "tokenDeviceComplete() called " +authController.getDevice());
        Log.e(TAG, "tokenDeviceComplete: "+authController.getDevice().getVersionApp() +" === "+requestVersion() );
//        if (authController.getDevice().getError().getNo() == 0 && !authController.getDevice().getVersionApp().equals(requestVersion())){//Version más nueva de la aplicacion
//            MaterialDialog x = new MaterialDialog.Builder(this)
//                    .onPositive(new MaterialDialog.SingleButtonCallback() {
//                        @Override
//                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                            openBrowser();
//                        }
//                    }).dismissListener(new DialogInterface.OnDismissListener() {
//                        @Override
//                        public void onDismiss(DialogInterface dialogInterface) {
//                            Log.e(TAG, "onDismiss: " );
//                            finish();
//                        }
//                    })
//                    .title("Actualización disponible")
//                    .content("Existe una nueva version disponible, descargala aquí")
//                    .positiveText("Aceptar")
//                    .show();
//        }
    }

    private void openBrowser() {
        try{
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(authController.getDevice().getRutaDeDescargaApp()));
            startActivity(browserIntent);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void catalogsUpdateComplete() {
        Log.d(TAG, "catalogsUpdateComplete() called");
    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideSplash(TIME_SPLASH);
    }

    private void hideSplash(final double milisecondsDisplayed){
        try {
            new CountDownTimer(500, 100) {
                public void onTick(long millisUntilFinished) {
                }

                public void onFinish() {
                    if (llSplash != null){
                        llSplash.setVisibility(View.GONE);
                        hideSoftKeyboard();
                    }else{
                        hideSplash(milisecondsDisplayed);
                    }
                }

            }.start();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String requestVersion(){
        String t = "";
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            return pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return t;
    }

}


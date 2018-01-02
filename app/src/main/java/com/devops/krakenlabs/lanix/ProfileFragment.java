package com.devops.krakenlabs.lanix;


import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.devops.krakenlabs.lanix.base.LanixApplication;


public class ProfileFragment extends DialogFragment {
    private static final String TAG = ProfileFragment.class.getSimpleName();
    private View viewRoot;
    private TextView tvApPat;
    private TextView tvApMat;
    private TextView tvCorreo;
    private TextView tvCelular;
    private TextView tvOperador;
    private TextView tvModulo;
    private TextView tvRegion;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        Point size = new Point();

        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);

        int width = size.x;

        window.setLayout((int) (width * 0.95), WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
//        Dialog dialog = getDialog();
//        if (dialog != null) {
//            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewRoot = inflater.inflate(R.layout.fragment_profile, container, false);
        tvApPat = viewRoot.findViewById(R.id.tv_ap_pat);
        tvApMat = viewRoot.findViewById(R.id.tv_ap_mat);
        tvCorreo = viewRoot.findViewById(R.id.tv_correo);
        tvCelular = viewRoot.findViewById(R.id.tv_celular);
        tvOperador = viewRoot.findViewById(R.id.tv_operador);
        tvModulo = viewRoot.findViewById(R.id.tv_modulo);
        tvRegion = viewRoot.findViewById(R.id.tv_region);
        fillUI();
        return viewRoot;
    }

    private void fillUI() {
        try{
            tvApPat .setText(LanixApplication.getInstance().getAuthController().getUser().getPromotor().getApellidoPaterno());
            tvApMat .setText(LanixApplication.getInstance().getAuthController().getUser().getPromotor().getApellidoMaterno());
            tvCorreo .setText(LanixApplication.getInstance().getAuthController().getUser().getPromotor().getCorreoElectronico());
            tvCelular .setText(LanixApplication.getInstance().getAuthController().getUser().getPromotor().getTelefonoCelular());
            tvOperador .setText(LanixApplication.getInstance().getAuthController().getUser().getPromotor().getCarrier());
            tvModulo .setText(LanixApplication.getInstance().getAuthController().getUser().getPromotor().getModulo());
            tvRegion .setText(LanixApplication.getInstance().getAuthController().getUser().getPromotor().getRegion());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}

package com.devops.krakenlabs.lanix;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.devops.krakenlabs.lanix.base.LanixApplication;
import com.devops.krakenlabs.lanix.ventas.VentasContainerFragment;


public class MenuFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = MenuFragment.class.getSimpleName();
    private View viewRoot;
    private Button btnProfile;
    private Button btnVentas;
    private Button btnAsistencia;
    private Button btnConsultas;
    private TextView tvNombre;


    public MenuFragment() {
        // Required empty public constructor
    }

    public static MenuFragment newInstance() {
        MenuFragment fragment = new MenuFragment();
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
        viewRoot      = inflater.inflate(R.layout.fragment_menu, container, false);
        btnVentas     = viewRoot.findViewById(R.id.btn_ventas);
        btnAsistencia = viewRoot.findViewById(R.id.btn_asistencia);
        btnProfile    = viewRoot.findViewById(R.id.btn_perfil);
        btnConsultas  = viewRoot.findViewById(R.id.btn_pw);
        tvNombre      = viewRoot.findViewById(R.id.tv_nombre);


        fillUI();
        btnVentas.setOnClickListener(this);
        btnAsistencia.setOnClickListener(this);
        btnConsultas.setOnClickListener(this);
        btnProfile.setOnClickListener(this);
        return viewRoot;
    }

    /**
     *
     */
    private void fillUI() {
        try{
            tvNombre.setText(LanixApplication.getInstance().getAuthController().getUser().getPromotor().getNombres());
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

    @Override
    public void onClick(View view) {
        if (view instanceof Button){
            switch (view.getId()){
                case R.id.btn_ventas:{
                    HomeActivity ho = (HomeActivity) getActivity();
                    ho.setVentasContainerFragment(new VentasContainerFragment());
                    switchFragment(ho.getVentasContainerFragment());
                    break;
                }

                case R.id.btn_asistencia:{
                    HomeActivity ho = (HomeActivity) getActivity();
                    ho.hideFragmentContainer();
                    break;
                }

                case R.id.btn_pw:{
                    initChangePassword();
                    break;
                }

                case R.id.btn_perfil:{
                    initProfile();
//                    break;
                }
            }
        }
    }

    private void initProfile() {
        Log.d(TAG, "initChangePassword() called");
        ProfileFragment recoverPwFragment = new ProfileFragment();
        recoverPwFragment.show(getActivity().getSupportFragmentManager(),recoverPwFragment.getClass().getSimpleName());
    }

    private void switchFragment(Fragment fragment){
        HomeActivity homeActivity = (HomeActivity) getActivity();
        FragmentTransaction ft = homeActivity.getSupportFragmentManager().beginTransaction();
        ft.add(R.id.fl_container, fragment).commit();
    }

    private void initChangePassword() {
        Log.d(TAG, "initChangePassword() called");
        RecoverPwFragment recoverPwFragment = RecoverPwFragment.newInstance();
        recoverPwFragment.show(getActivity().getSupportFragmentManager(),recoverPwFragment.getClass().getSimpleName());
    }
}

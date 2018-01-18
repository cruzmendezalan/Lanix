package com.devops.krakenlabs.lanix.ventas;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;

import com.devops.krakenlabs.lanix.HomeActivity;
import com.stepstone.stepper.Step;
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter;

/**
 * Created by Alan Giovani Cruz Méndez on 17/01/18 00:08.
 * cruzmendezalan@gmail.com
 */

public class VentasAdapter extends AbstractFragmentStepAdapter {

    private HomeActivity homeActivity;

    public VentasAdapter(@NonNull FragmentManager fm, @NonNull Context context) {
        super(fm, context);
        homeActivity = (HomeActivity) context;
        homeActivity.setVentasFirstStepFragment(new VentasFirstStepFragment());
        homeActivity.setVentasSecondStepFragment(new VentasSecondStepFragment());
        homeActivity.setVentasThirdStepFragmentFragment(new VentasThirdStepFragment());
    }

    @Override
    public Step createStep(int position) {
        switch (position){
            case 0:{
                return homeActivity.getVentasFirstStepFragment();
            }
            case 1:{
                return homeActivity.getVentasSecondStepFragment();
            }
            case 2:{
                return homeActivity.getVentasThirdStepFragmentFragment();
            }
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }
}

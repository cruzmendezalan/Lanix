package com.devops.krakenlabs.lanix.ventas;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;

import com.stepstone.stepper.Step;
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter;

/**
 * Created by Alan Giovani Cruz Méndez on 17/01/18 00:08.
 * cruzmendezalan@gmail.com
 */

public class VentasAdapter extends AbstractFragmentStepAdapter {
    public VentasAdapter(@NonNull FragmentManager fm, @NonNull Context context) {
        super(fm, context);
    }

    @Override
    public Step createStep(int position) {
        switch (position){
            case 0:{
                return new Ventas1Fragment();
            }
            case 1:{
                return new VentasFragment();
            }
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}

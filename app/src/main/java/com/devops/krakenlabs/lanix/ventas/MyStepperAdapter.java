package com.devops.krakenlabs.lanix.ventas;

import android.content.Context;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;

import com.stepstone.stepper.Step;
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter;
import com.stepstone.stepper.viewmodel.StepViewModel;

/**
 * Created by Alan Giovani Cruz Méndez on 27/12/17 23:15.
 * cruzmendezalan@gmail.com
 */

public class MyStepperAdapter extends AbstractFragmentStepAdapter {

    public MyStepperAdapter(FragmentManager fm, Context context) {
        super(fm, context);
    }

    @Override
    public Step createStep(int position) {
        switch (position){
            case 0:{
                VentasModelo step = new VentasModelo();
                return step;
            }
            case 1:{
                VentasFecha step = new VentasFecha();
                return step;
            }
            case 2:{
                VentasImeiFragment step = new VentasImeiFragment();
                return step;
            }
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @NonNull
    @Override
    public StepViewModel getViewModel(@IntRange(from = 0) int position) {
        //Override this method to set Step title for the Tabs, not necessary for other stepper types
        return new StepViewModel.Builder(context)
                .setTitle("Titulo") //can be a CharSequence instead
                .create();
    }
}

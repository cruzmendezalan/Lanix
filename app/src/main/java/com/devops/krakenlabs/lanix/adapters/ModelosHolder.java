package com.devops.krakenlabs.lanix.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.devops.krakenlabs.lanix.R;

/**
 * Created by Alan Giovani Cruz Méndez on 28/12/17 14:39.
 * cruzmendezalan@gmail.com
 */

public class ModelosHolder extends RecyclerView.ViewHolder {
    private static final String TAG = ModelosHolder.class.getSimpleName();
    private TextView tvModelo;


    public ModelosHolder(View itemView) {
        super(itemView);
        tvModelo = itemView.findViewById(R.id.tv_model_name);
        tvModelo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick() called with: view = [" + view + "]");
            }
        });
    }

    public void setUI(String name){
        tvModelo.setText(name);
    }
}

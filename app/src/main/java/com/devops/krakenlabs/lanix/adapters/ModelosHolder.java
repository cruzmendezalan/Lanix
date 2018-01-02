package com.devops.krakenlabs.lanix.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.devops.krakenlabs.lanix.R;

/**
 * Created by Alan Giovani Cruz Méndez on 28/12/17 14:39.
 * cruzmendezalan@gmail.com
 */

public class ModelosHolder extends RecyclerView.ViewHolder {
    private static final String TAG = ModelosHolder.class.getSimpleName();
    private TextView tvModelo;
    private CheckBox cbSelected;
    private int position;
    private TelefonoSelected telefonoSelected;

    public ModelosHolder(View itemView, final TelefonoSelected telefonoSelected) {
        super(itemView);
        tvModelo = itemView.findViewById(R.id.tv_model_name);
        this.telefonoSelected = telefonoSelected;
        tvModelo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick() called with: view = [" + view + "]");
            }
        });
        cbSelected =  itemView.findViewById(R.id.shoppingListCheckBox);
        cbSelected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Log.d(TAG, "onCheckedChanged() called with: compoundButton = [" + compoundButton + "], b = [" + b + "]");
                telefonoSelected.positionSelected(position,b);
            }
        });
    }

    public void setUI(String name, int position){
        tvModelo.setText(name);
        this.position = position;
    }
}

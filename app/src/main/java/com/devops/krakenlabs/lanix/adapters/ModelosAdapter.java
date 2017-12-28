package com.devops.krakenlabs.lanix.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devops.krakenlabs.lanix.R;
import com.devops.krakenlabs.lanix.models.catalogos.Catalog;

/**
 * Created by Alan Giovani Cruz Méndez on 28/12/17 14:39.
 * cruzmendezalan@gmail.com
 */

public class ModelosAdapter extends RecyclerView.Adapter<ModelosHolder> {
    private static final String TAG = ModelosAdapter.class.getSimpleName();
    private Catalog catalog;
    private View rootView;

    public ModelosAdapter(Catalog catalog) {
        this.catalog = catalog;
    }

    @Override
    public ModelosHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.i_modelo,parent,false);
        return new ModelosHolder(rootView);
    }

    @Override
    public void onBindViewHolder(ModelosHolder model, int position) {
        model.setUI(catalog.getModelos().get(position).getModelo());
    }

    @Override
    public int getItemCount() {
        return catalog.getModelos().size();
    }
}

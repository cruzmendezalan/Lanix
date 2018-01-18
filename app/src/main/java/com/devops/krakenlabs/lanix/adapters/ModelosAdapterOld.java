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

public class ModelosAdapterOld extends RecyclerView.Adapter<ModelosHolder> implements TelefonoSelected{
    private static final String TAG = ModelosAdapterOld.class.getSimpleName();
    private Catalog catalog;
    private View rootView;

    public ModelosAdapterOld(Catalog catalog) {
        this.catalog = catalog;
    }

    @Override
    public ModelosHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.i_modelo,parent,false);
        return new ModelosHolder(rootView, this);
    }

    @Override
    public void onBindViewHolder(ModelosHolder model, int position) {
        model.setUI(catalog.getModelos().get(position).getModelo(), position);
    }

    @Override
    public int getItemCount() {
        if (catalog != null){
            return catalog.getModelos().size();
        }
        return 0;
    }

    @Override
    public void positionSelected(int position, Boolean selected) {
        catalog.getModelos().get(position).setSelected(selected);
    }

    public Catalog getCatalog() {
        return catalog;
    }
}

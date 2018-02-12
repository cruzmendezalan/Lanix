package com.devops.krakenlabs.lanix.ventas;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;


import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.devops.krakenlabs.lanix.R;
import com.devops.krakenlabs.lanix.models.catalogos.Catalog;

import java.util.ArrayList;

import java.util.ArrayList;

import gr.escsoft.michaelprimez.searchablespinner.interfaces.ISpinnerSelectedView;
import gr.escsoft.michaelprimez.searchablespinner.tools.UITools;

/**
 * Created by Alan Giovani Cruz Méndez on 17/01/18 21:35.
 * cruzmendezalan@gmail.com
 */

public class ModelosAdapter extends ArrayAdapter<String> implements Filterable, ISpinnerSelectedView {
    private static final String TAG = ModelosAdapter.class.getSimpleName();
    private Catalog catalog;
    private Context mContext;
    private ArrayList<String> mBackupStrings;
    private ArrayList<String> mStrings;
    private StringFilter mStringFilter = new StringFilter();
    private int lType;

    public ModelosAdapter(@NonNull Context context, int resource, Catalog catalog, int type) {
        super(context, resource);
        lType = type;
        mContext = context;
        mStrings = new ArrayList<>();
        mBackupStrings = new ArrayList<>();
        this.catalog = catalog;
        if (type == 0){
            try{
                for (int i = 0; i < catalog.getModelos().size(); i++) {
                    mStrings.add(catalog.getModelos().get(i).getModelo()  );
                    mBackupStrings.add(catalog.getModelos().get(i).getModelo());
                }
            }catch (Exception e){e.printStackTrace();}
        }else if(type == 1){
            for (int i = 0; i < catalog.getCadenasComerciales().size(); i++) {
                try{
                    mStrings.add(catalog.getCadenasComerciales().get(i).getCadenaComercial());
                    mBackupStrings.add(catalog.getCadenasComerciales().get(i).getCadenaComercial());
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }

    public int updateModelos(int modeloIDID){
        Log.d(TAG, "updateModelos() called with: modeloIDID = [" + modeloIDID + "]");
        int k = 0;
        mStrings = new ArrayList<>();
        mBackupStrings = new ArrayList<>();
        try{
            for (int i = 0; i < catalog.getProductos().size(); i++) {
                if (catalog.getProductos().get(i).getModeloId() == modeloIDID){
                    k++;
                    mStrings.add(catalog.getProductos().get(i).getModelo() + " " + catalog.getProductos().get(i).getColor());
                    mBackupStrings.add(catalog.getProductos().get(i).getModelo() + " " + catalog.getProductos().get(i).getColor());
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        Log.e(TAG, "FINAL SIZE: "+mStrings.size() );
        notifyDataSetChanged();
        return k;
    }

    @Override
    public int getCount() {
        return mStrings == null ? 0 : mStrings.size() + 1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        try{
            if (position == 0) {
                view = getNoSelectionView();
            } else {
                view = View.inflate(mContext, R.layout.view_list_item, null);
                ImageView letters = (ImageView) view.findViewById(R.id.ImgVw_Letters);
                TextView dispalyName = (TextView) view.findViewById(R.id.TxtVw_DisplayName);
                letters.setImageDrawable(getTextDrawable(mStrings.get(position-1)));
                dispalyName.setText(mStrings.get(position-1));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return view;
    }

    @Override
    public View getSelectedView(int position) {
        View view = null;
        if (position == 0) {
            view = getNoSelectionView();
        } else {
            view = View.inflate(mContext, R.layout.view_list_item, null);
            ImageView letters = (ImageView) view.findViewById(R.id.ImgVw_Letters);
            TextView dispalyName = (TextView) view.findViewById(R.id.TxtVw_DisplayName);
            letters.setImageDrawable(getTextDrawable(mStrings.get(position-1)));
            dispalyName.setText(mStrings.get(position-1));
        }
        return view;
    }

    @Override
    public View getNoSelectionView() {
        View view = View.inflate(mContext, R.layout.view_list_no_selection_item, null);
        return view;
    }

    private TextDrawable getTextDrawable(String displayName) {
        TextDrawable drawable = null;
        if (!TextUtils.isEmpty(displayName)) {
            int color2 = ColorGenerator.MATERIAL.getColor(displayName);
            drawable = TextDrawable.builder()
                    .beginConfig()
                    .width(UITools.dpToPx(mContext, 32))
                    .height(UITools.dpToPx(mContext, 32))
                    .textColor(Color.WHITE)
                    .toUpperCase()
                    .endConfig()
                    .round()
                    .build(displayName.substring(0, 1), color2);
        } else {
            drawable = TextDrawable.builder()
                    .beginConfig()
                    .width(UITools.dpToPx(mContext, 32))
                    .height(UITools.dpToPx(mContext, 32))
                    .endConfig()
                    .round()
                    .build("?", Color.GRAY);
        }
        return drawable;
    }

    @Override
    public Filter getFilter() {
        return mStringFilter;
    }

    @Override
    public String getItem(int position) {
        if (mStrings != null && position > 0)
            return mStrings.get(position - 1);
        else
            return null;
    }

    @Override
    public long getItemId(int position) {
        if (mStrings == null && position > 0)
            return mStrings.get(position).hashCode();
        else
            return -1;
    }


    public class StringFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            final FilterResults filterResults = new FilterResults();
            if (TextUtils.isEmpty(constraint)) {
                filterResults.count = mBackupStrings.size();
                filterResults.values = mBackupStrings;
                return filterResults;
            }
            final ArrayList<String> filterStrings = new ArrayList<>();
            for (String text : mBackupStrings) {
                if (text.toLowerCase().contains(constraint)) {
                    filterStrings.add(text);
                }
            }
            filterResults.count = filterStrings.size();
            filterResults.values = filterStrings;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mStrings = (ArrayList) results.values;
            notifyDataSetChanged();
        }
    }
}

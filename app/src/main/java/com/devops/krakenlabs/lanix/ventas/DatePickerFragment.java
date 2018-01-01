package com.devops.krakenlabs.lanix.ventas;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by Alan Giovani Cruz Méndez on 01/01/18 14:15.
 * cruzmendezalan@gmail.com
 */

public class DatePickerFragment extends DialogFragment {
    private static final String TAG = DatePickerFragment.class.getSimpleName();
    private DatePickerDialog.OnDateSetListener onDateSetListener;

    public DatePickerFragment() {
    }

    public void setOnDateSetListener(DatePickerDialog.OnDateSetListener onDateSetListener) {
        this.onDateSetListener = onDateSetListener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), onDateSetListener, year, month, day);
    }
}

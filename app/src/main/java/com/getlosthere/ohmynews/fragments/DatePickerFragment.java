package com.getlosthere.ohmynews.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import java.util.Calendar;

/**
 * Created by violetaria on 7/30/16.
 */
public class DatePickerFragment extends DialogFragment {
    private long date;

    public DatePickerFragment() {

    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();

        date = getArguments().getLong("date");
        if (date != -1 ) {
            c.setTimeInMillis(date);
        }
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog.OnDateSetListener listener = (DatePickerDialog.OnDateSetListener) getTargetFragment();

        return new DatePickerDialog(getActivity(), listener, year, month, day);
    }
}
package com.getlosthere.ohmynews.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.getlosthere.ohmynews.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by violetaria on 7/31/16.
 */
public class FilterFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{
    // variables to hold filters
    private String filterSortOrder;
    private long filterDate;
    private boolean filterArts;
    private boolean filterFashion;
    private boolean filterSports;
    private int code;
    SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");

    Spinner spSortOrder;
    CheckBox cbArts;
    CheckBox cbFashion;
    CheckBox cbSports;
    EditText etDate;
    Button btnSave;

    public FilterFragment() {

    }

    public static FilterFragment newInstance(String title, String sortOrder, Long date, boolean arts, boolean fashion, boolean sports){
        FilterFragment frag = new FilterFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("sort_order",sortOrder);
        args.putLong("date",date);
        args.putBoolean("arts",arts);
        args.putBoolean("fashion",fashion);
        args.putBoolean("sports",sports);

        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_filter, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        filterSortOrder = getArguments().getString("sort_order");
        filterDate = getArguments().getLong("date",-1);
        filterArts = getArguments().getBoolean("arts", false);
        filterFashion = getArguments().getBoolean("fashion", false);
        filterSports = getArguments().getBoolean("sports", false);
        code = getArguments().getInt("code",0);
        setupViews(view);

        String title = getArguments().getString("title", "Enter Name");
        getDialog().setTitle(title);
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

    // 1. Defines the listener interface with a method passing back data result.
    public interface FilterListener {
        void onFinishedFilterDialog( String sortOrder, Long date, boolean arts, boolean fashion, boolean sports);
    }


    public void setupViews(View view) {
        etDate = (EditText) view.findViewById(R.id.etDate);
        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                DatePickerFragment dateFragment = new DatePickerFragment();
                Calendar calender = Calendar.getInstance();
                Bundle args = new Bundle();
                args.putLong("date", filterDate);
                dateFragment.setArguments(args);
//                dateFragment.setCallBack(ondate);
                dateFragment.setTargetFragment(FilterFragment.this,300);
                dateFragment.show(fm, "datePicker");
            }
        });
        if (filterDate != -1 ) {
            final Calendar c = Calendar.getInstance();
            c.setTimeInMillis(filterDate);
            etDate.setText(sdFormat.format(c.getTime()));
        }

        spSortOrder = (Spinner) view.findViewById(R.id.spSortOrder);
        setSpinnerValue(spSortOrder, filterSortOrder);

        cbArts = (CheckBox) view.findViewById(R.id.cbArts);
        cbArts.setChecked(filterArts);

        cbFashion = (CheckBox) view.findViewById(R.id.cbFashion);
        cbFashion.setChecked(filterFashion);


        cbSports = (CheckBox) view.findViewById(R.id.cbSports);
        cbSports.setChecked(filterSports);

        btnSave = (Button) view.findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FilterListener listener = (FilterListener) getActivity();
                listener.onFinishedFilterDialog(spSortOrder.getSelectedItem().toString(),filterDate,cbArts.isChecked(),cbFashion.isChecked(),cbSports.isChecked());
                dismiss();
            }
        });
    }

    public void setSpinnerValue(Spinner spinner, String value) {
        int index = 0;
        SpinnerAdapter adapter = spinner.getAdapter();
        for (int i=0; i < adapter.getCount(); i++) {
            if (adapter.getItem(i).equals(value)) {
                index = i;
                break;
            }
        }
        spinner.setSelection(index);
    }

    public void onDateSelected(int year, int month, int day) {
        final Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);
        filterDate = c.getTimeInMillis();
        etDate.setText(sdFormat.format(c.getTime()));
    }

    // handle the date selected
    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        // store the values selected into a Calendar instance
        final Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, monthOfYear);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        filterDate = c.getTimeInMillis();
        etDate.setText(sdFormat.format(c.getTime()));
    }

}

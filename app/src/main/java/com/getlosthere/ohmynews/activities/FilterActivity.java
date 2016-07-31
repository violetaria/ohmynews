package com.getlosthere.ohmynews.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import com.getlosthere.ohmynews.R;
import com.getlosthere.ohmynews.fragments.DatePickerFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FilterActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    // variables to hold filters
    private String filterSortOrder;
    private long filterDate;
    private boolean filterArts;
    private boolean filterFashion;
    private boolean filterSports;
    private int code;
    SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");

    @BindView(R.id.spSortOrder) Spinner spSortOrder;
    @BindView(R.id.cbArts) CheckBox  cbArts;
    @BindView(R.id.cbFashion) CheckBox  cbFashion;
    @BindView(R.id.cbSports) CheckBox cbSports;
    @BindView(R.id.etDate) EditText etDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        ButterKnife.bind(this);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.news);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        getSupportActionBar().setTitle("Filter Settings");

        Intent i = getIntent();
        filterSortOrder = i.getStringExtra("sort_order");

        filterDate = i.getLongExtra("date",-1);
        filterArts = i.getBooleanExtra("arts", false);
        filterFashion = i.getBooleanExtra("fashion", false);
        filterSports = i.getBooleanExtra("sports", false);
        code = i.getIntExtra("code",0);
        setupViews();
    }

    public void setupViews() {
        if (filterDate != -1 ) {
            final Calendar c = Calendar.getInstance();
            c.setTimeInMillis(filterDate);
            etDate.setText(sdFormat.format(c.getTime()));
        }
        setSpinnerValue(spSortOrder, filterSortOrder);
        cbArts.setChecked(filterArts);
        cbFashion.setChecked(filterFashion);
        cbSports.setChecked(filterSports);
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

    // attach to an onclick handler to show the date picker
    public void showDatePickerDialog(View v) {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
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

    public void saveSettings(View view) {
        Intent data = new Intent();

        data.putExtra("sort_order", spSortOrder.getSelectedItem().toString());
        data.putExtra("date", filterDate);
        data.putExtra("arts",cbArts.isChecked());
        data.putExtra("fashion",cbFashion.isChecked());
        data.putExtra("sports",cbSports.isChecked());
        data.putExtra("code",code);

        setResult(RESULT_OK,data);
        this.finish();
    }
}

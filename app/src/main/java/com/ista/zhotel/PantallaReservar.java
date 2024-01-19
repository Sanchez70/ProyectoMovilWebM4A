package com.ista.zhotel;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import android.app.DatePickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class PantallaReservar extends AppCompatActivity {
    private TextView txtFechaIn;
    private TextView txtFechaFin;
    int dia,mes,ano;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_reservar);
        calendar();
    }

    public void calendar(){

        txtFechaIn = findViewById(R.id.fechaInic);
        txtFechaFin = findViewById(R.id.fechafin);

        txtFechaIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerIn();
            }
        });

        txtFechaFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerFin();
            }
        });
    }

    private void showDatePickerIn() {
        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Seleccione la fecha de inicio");
        builder.setCalendarConstraints(constraintsBuilder.build());

        MaterialDatePicker<Long> datePicker = builder.build();

        datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selection) {
                // Manejar la fecha seleccionada
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeZone(TimeZone.getDefault());
                calendar.setTimeInMillis(selection);

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                String selectedDate = dateFormat.format(calendar.getTime());
                dateFormat.setTimeZone(TimeZone.getDefault());
                txtFechaFin.setText(selectedDate);
            }
        });

        datePicker.show(getSupportFragmentManager(), "DATE_PICKER_TAG");
    }
    

    private void showDatePickerFin() {
        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Seleccione la fecha final");
        builder.setCalendarConstraints(constraintsBuilder.build());

        MaterialDatePicker<Long> datePicker = builder.build();

        datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selection) {
                // Manejar la fecha seleccionada
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeZone(TimeZone.getDefault());
                calendar.setTimeInMillis(selection);

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                String selectedDate = dateFormat.format(calendar.getTime());
                dateFormat.setTimeZone(TimeZone.getDefault());
                txtFechaFin.setText(selectedDate);
            }
        });

        datePicker.show(getSupportFragmentManager(), "DATE_PICKER_TAG");
    }
}
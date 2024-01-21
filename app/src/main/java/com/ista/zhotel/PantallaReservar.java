package com.ista.zhotel;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.TextView;

import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import android.app.DatePickerDialog;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class PantallaReservar extends AppCompatActivity {
    private TextView txtFechaIn;
    private TextView txtFechaFin;
    private String selectedDateIn;
    private String selectedDateFin;

    private TextView txtTotal;
    private TextView txtDias, txtprecio;
    public static double precio;
    public static int idHabicionRe;
     private AutoCompleteTextView spnPersonas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_reservar);
        //Cargar datos al spinner.
        spnPersonas = findViewById(R.id.nroPersonas);
        Integer[] datos={1,2,3,4};
        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,datos);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnPersonas.setAdapter(adapter);
        calendar();
        txtprecio.setText(String.valueOf(precio));

    }

    public void calendar(){

        txtFechaIn = findViewById(R.id.fechaInic);
        txtFechaFin = findViewById(R.id.fechafin);
        txtDias = findViewById(R.id.txtDias);
        txtprecio= findViewById(R.id.precioHabi);
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
        //Establezco una fecha límite para el inicio
        Calendar minFecha = Calendar.getInstance();
        minFecha.set(Calendar.YEAR, 2024);
        minFecha.set(Calendar.MONTH, Calendar.JANUARY);
        minFecha.set(Calendar.DAY_OF_MONTH, 1);
        constraintsBuilder.setStart(minFecha.getTimeInMillis());

        //Establezco una fecha máxima para el inicio
        Calendar maxFecha = Calendar.getInstance();
        maxFecha.set(Calendar.YEAR, 2025);
        maxFecha.set(Calendar.MONTH, Calendar.APRIL);
        maxFecha.set(Calendar.DAY_OF_MONTH, 30);
        constraintsBuilder.setEnd(maxFecha.getTimeInMillis());

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
                dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                selectedDateIn = dateFormat.format(calendar.getTime());
                txtFechaIn.setText(selectedDateIn);
            }
        });

        datePicker.show(getSupportFragmentManager(), "DATE_PICKER_TAG");
    }
    

    private void showDatePickerFin() {
        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
        Calendar minFecha = Calendar.getInstance();
        minFecha.set(Calendar.YEAR, 2024);
        minFecha.set(Calendar.MONTH, Calendar.JANUARY);
        minFecha.set(Calendar.DAY_OF_MONTH, 1);
        constraintsBuilder.setStart(minFecha.getTimeInMillis());

        Calendar maxFecha = Calendar.getInstance();
        maxFecha.set(Calendar.YEAR, 2025);
        maxFecha.set(Calendar.MONTH, Calendar.APRIL);
        maxFecha.set(Calendar.DAY_OF_MONTH, 30);
        constraintsBuilder.setEnd(maxFecha.getTimeInMillis());

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
                dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                selectedDateFin = dateFormat.format(calendar.getTime());
                txtFechaFin.setText(selectedDateFin);
                txtDias.setText(calcularDias());





            }
        });

        datePicker.show(getSupportFragmentManager(), "DATE_PICKER_TAG");
    }

    public String calcularDias(){
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate fechaIn = LocalDate.parse(selectedDateIn,format);
        LocalDate fechaFin = LocalDate.parse(selectedDateFin,format);
        int total = (int) fechaIn.until(fechaFin).getDays();

        return String.valueOf(total);
    }




}
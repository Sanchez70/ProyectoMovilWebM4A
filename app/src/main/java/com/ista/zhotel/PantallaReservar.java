package com.ista.zhotel;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.gson.Gson;

import android.app.DatePickerDialog;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class PantallaReservar extends AppCompatActivity {
    private TextView txtFechaIn;
    private TextView txtFechaFin;
    private String selectedDateIn;
    private String selectedDateFin;
    private String cedula;
    private TextView txtTotal;
    private TextView txtDias, txtprecio;
    public static double precio;
    public static int idHabicionRe;
    public static String correoUsuRe;

    public static Bitmap decodedByte1;

     private AutoCompleteTextView spnPersonas;
    ImageView adaptarImagen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_reservar);
        //Cargar datos al spinner.
        spnPersonas = findViewById(R.id.nroPersonas);
        adaptarImagen= findViewById(R.id.imagenBase);
        adaptarImagen.setImageBitmap(decodedByte1);
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

                totalPrecio();



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

    public void totalPrecio(){
        String inputText = txtDias.getText().toString().trim();
        txtTotal = findViewById(R.id.txtTotal);
        if (!inputText.isEmpty()) {
            try {
                double precioIni = Double.valueOf(inputText);
                double totalDo = precioIni * precio;
                txtTotal.setText(String.valueOf(totalDo));
            } catch (NumberFormatException e) {
                txtTotal.setText("0");
                e.printStackTrace();
            }
        } else {
            txtTotal.setText("0");
        }
    }
    public void getDatos(String usuario){
        String url="http://192.168.18.5:8081/api/clientes/usuario/"+usuario;//endpoint.
        Log.d("He","Llegue al metodo cargar cliente");
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url,null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    if(response.length()>0) {
                        JSONObject jsonObjectCliente = response.getJSONObject(0);
                        cedula = jsonObjectCliente.getString("cedula_persona");
                        Log.d("He", cedula);
                    }else{
                        Toast.makeText(getApplicationContext(), "Cliente no encontrado", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException j) {
                    j.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("He",error.getMessage());
            }
        });
        Volley.newRequestQueue(this).add(jsonArrayRequest);
    }


    private <T>void realizarSolicitudPOST(String url,  final T objeto) {
        // Obtener la instancia de la cola de solicitudes de Volley
        RequestQueue queue = Volley.newRequestQueue(this);
        Gson gson = new Gson();
        final String personaJson = gson.toJson(objeto);
        // Crear una solicitud POST
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Manejar la respuesta del servidor aquí
                        Log.d("TAG", "Respuesta del servidor: " + response);
                        try {
                            // Puedes convertir la respuesta a un objeto JSON si es necesario
                            JSONObject jsonResponse = new JSONObject(response);
                            // Manejar el objeto JSON según tus necesidades
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejar errores de la solicitud aquí
                        Log.e("TAG", "Error en la solicitud: " + error.toString());
                    }
                }) {
            @Override
            public byte[] getBody() {
                // Aquí puedes especificar los datos que deseas enviar en el cuerpo de la solicitud
                return personaJson.getBytes();
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                // Configurar el encabezado Content-Type
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };
        queue.add(stringRequest);
    }

public void guardarReserva(){
        getDatos(correoUsuRe);

}


}
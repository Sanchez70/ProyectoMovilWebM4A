package com.ista.zhotel;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
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
import com.ista.zhotel.model.Callback;
import com.ista.zhotel.model.DetalleFactura;
import com.ista.zhotel.model.EncabezadoCallBack;
import com.ista.zhotel.model.EncabezadoFactura;
import com.ista.zhotel.model.Reservas;

import android.app.DatePickerDialog;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeParseException;
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
    public  Long cedula;
    public  Long idReserva;
    public  Long idEncabezado;
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
        guardarReserva();
        getDatos(correoUsuRe);
        Log.d("TAG","CEDULA INICIANTE: " +cedula);


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
        String url="http://192.168.40.228:8081/api/clientes/usuario/"+usuario;//endpoint.
        //String url="http://192.168.0.119:8081/api/clientes/usuario/"+usuario;//endpoint.
        //String url="http://192.168.19.119:8081/api/clientes/usuario/"+usuario;//endpoint.
        Log.d("He","Llegue al metodo cargar cliente");
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url,null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    if(response.length()>0) {
                        JSONObject jsonObjectCliente = response.getJSONObject(0);
                        cedula = jsonObjectCliente.getLong("idCliente");
                        Log.d("He", "cedula "+cedula);
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
        RequestQueue queue = Volley.newRequestQueue(this);
        Gson gson = new Gson();
        final String personaJson = gson.toJson(objeto);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("TAG", "Respuesta del servidor: " + response);
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("TAG", "Error en la solicitud: " + error.toString());
                    }
                }) {
            @Override
            public byte[] getBody() {
                return personaJson.getBytes();
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        queue.add(stringRequest);
    }

    public void guardarReserva() {
        Button guardar = findViewById(R.id.button10);
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText diasRece = findViewById(R.id.txtDias);
                EditText fechaEntara = findViewById(R.id.fechaInic);
                EditText fechaFin = findViewById(R.id.fechafin);
                EditText personaRece = findViewById(R.id.nroPersonas);
                TextView totalRece = findViewById(R.id.txtTotal);
                String fechaEntradaString = fechaEntara.getText().toString();
                DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                LocalDate fechaEntrada = LocalDate.parse(fechaEntradaString, inputFormatter);

                // Obtener la fecha de salida y formatearla
                String fechaSalidaString = fechaFin.getText().toString();
                LocalDate fechaSalida = LocalDate.parse(fechaSalidaString, inputFormatter);

                // Convertir las fechas al formato "yyyy-MM-dd"
                DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd",Locale.getDefault());
                String fechaEntradaFormatted = fechaEntrada.format(outputFormatter);
                String fechaSalidaFormatted = fechaSalida.format(outputFormatter);
                Reservas miReserva = new Reservas();
                miReserva.setIdHabitaciones((long) idHabicionRe);
                miReserva.setDias(Integer.valueOf(diasRece.getText().toString()));
                Log.d("TAG","Fechas;"+fechaEntradaFormatted+fechaSalidaFormatted);
                try {


                    // Convertir LocalDate a java.util.Date
                    miReserva.setFechaEntrada(fechaEntradaFormatted);
                    miReserva.setFechaSalida(fechaSalidaFormatted);
                    miReserva.setIdRecepcionista(null);
                    miReserva.setIdPago(null);
                    miReserva.setIdCliente(cedula);
                    miReserva.setEstado("Pendiente");
                    miReserva.setnPersona(Integer.valueOf(personaRece.getText().toString()));
                    miReserva.setTotal(Double.valueOf(totalRece.getText().toString()));
                    Log.d("TAG", "RESERVA:" + miReserva.toString());
                    realizarSolicitudPOST("http://192.168.40.228:8081/api/reservas", miReserva);
                    //realizarSolicitudPOST("http://192.168.0.119:8081/api/reservas", miReserva);
                    //realizarSolicitudPOST("http://192.168.19.119:8081/api/reservas", miReserva);
                    crearEncabezad();
                    crearDetalle();
                } catch (DateTimeParseException | NumberFormatException e) {
                    e.printStackTrace();
                    // Manejar el caso en el que la fecha o el número de personas no son válidos
                    Toast.makeText(getApplicationContext(), "Fecha o número de personas no válidos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

public void buscarReser(final EncabezadoCallBack callBack){
    String url="http://192.168.40.228:8081/api/reservas";//endpoint.
    //String url="http://192.168.0.119:8081/api/reservas";//endpoint.
    //String url="http://192.168.19.119:8081/api/reservas";//endpoint.
    Log.d("He","Llegue al metodo cargar reserva");
    JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url,null, new Response.Listener<JSONArray>() {
        @Override
        public void onResponse(JSONArray response) {
            try {
                if(response.length()>0) {

                    JSONObject jsonObjectCliente = response.getJSONObject(response.length() -1);
                    idReserva = jsonObjectCliente.getLong("idReserva");
                    callBack.onReservaObtenido(idReserva);
                    Log.d("He", "id encabezado:  " + idReserva);

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

public void buscarEcabezado(final Callback callback){
    String url="http://192.168.40.228:8081/api/encabezadofactura";//endpoint.
    //String url="http://192.168.0.119:8081/api/encabezadofactura";//endpoint.
    //String url="http://192.168.19.119:8081/api/encabezadofactura";//endpoint.
    Log.d("He","Llegue al metodo cargar detalle");
    JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url,null, new Response.Listener<JSONArray>() {
        @Override
        public void onResponse(JSONArray response) {
            try {
                if(response.length()>0) {
                    JSONObject jsonObjectCliente = response.getJSONObject(response.length() -1);
                    idEncabezado = jsonObjectCliente.getLong("idEncabezado");
                    Log.d("He", "id encabezado:  " + idEncabezado);
                    callback.onEncabezadoObtenido(idEncabezado);
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

public void crearEncabezad(){

    buscarReser(new EncabezadoCallBack() {
        @Override
        public void onReservaObtenido(long idReserva) {
            Long rservas = idReserva ;
            Log.d("He", "id reservar en el crear:  " + rservas);
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd",Locale.getDefault());
            LocalDate fechaFin1 = LocalDate.now();
            String fechaFactura = fechaFin1.format(outputFormatter);
            TextView totalRece = findViewById(R.id.txtTotal);
            EncabezadoFactura encabezado= new EncabezadoFactura();
            getDatos(correoUsuRe);
            encabezado.setIdCliente(cedula);
            encabezado.setFechaFactura(fechaFactura);
            encabezado.setIdReserva(rservas);
            encabezado.setTotal(Double.valueOf(totalRece.getText().toString()));
            realizarSolicitudPOST("http://192.168.40.228:8081/api/encabezadofactura", encabezado);
            //realizarSolicitudPOST("http://192.168.0.119:8081/api/encabezadofactura", encabezado);
            //realizarSolicitudPOST("http://192.168.19.119:8081/api/encabezadofactura", encabezado);
        }

        @Override
        public void onError(String errorMessage) {

        }
    });


    }
    public void crearDetalle(){
        buscarEcabezado(new Callback() {
            @Override
            public void onEncabezadoObtenido(long idEncabezado) {
                Long restabel= idEncabezado;
                Log.d("He", "id encabezado en el crear:  " + restabel);
                DetalleFactura detalle= new DetalleFactura();
                detalle.setIdEncabezado(restabel);
                detalle.setSubTotal(precio);
                realizarSolicitudPOST("http://192.168.40.228:8081/api/detallefactura", detalle);
                //realizarSolicitudPOST("http://192.168.0.119:8081/api/detallefactura", detalle);
                //realizarSolicitudPOST("http://192.168.19.119:8081/api/detallefactura", detalle);
            }

            @Override
            public void onError(String errorMessage) {
                Log.d("He", "callback  " + errorMessage);
            }
        });
    }
}
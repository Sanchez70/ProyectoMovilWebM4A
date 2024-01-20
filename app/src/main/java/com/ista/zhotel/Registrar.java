package com.ista.zhotel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.ista.zhotel.model.Persona;
import com.ista.zhotel.model.cliente;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class Registrar extends AppCompatActivity {
    TextInputEditText emailText, paswordText;
    Button boton1;
    TextView textView1;
    private FirebaseAuth mAuth;
    private TextView birthDateTextView;
    public void onStart () {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            currentUser.reload();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);
        emailText= findViewById(R.id.email);
        paswordText=findViewById(R.id.pasword);
        mAuth = FirebaseAuth.getInstance();
        boton1= findViewById(R.id.button);
        textView1 = findViewById(R.id.textViewRe);
        calendar();
        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getApplicationContext(),Login.class);
                startActivity(intent);
                finish();
            }
        });
        boton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailUser= emailText.getText().toString().trim();
                String passwordlUser= paswordText.getText().toString().trim();
                if (TextUtils.isEmpty(emailUser)){
                    Toast.makeText(Registrar.this, "Enten email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(passwordlUser)){
                    Toast.makeText(Registrar.this, "Enter pasword", Toast.LENGTH_SHORT).show();
                    return;

                }
                mAuth.createUserWithEmailAndPassword(emailUser, passwordlUser).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        try {

                            if (task.isSuccessful()){
                                guardarPersona();
                                guardarClietnes();

                                EditText auxUsuario=findViewById(R.id.email);
                                PantallaPrincipal.correoUsuario= auxUsuario.getText().toString();

                                Toast.makeText(Registrar.this, "Authentication Exit.",
                                        Toast.LENGTH_SHORT).show();
                                Intent intent= new Intent(getApplicationContext(),Login.class);
                                startActivity(intent);
                                finish();

                            } else {
                                Toast.makeText(Registrar.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                ;
                            }
                        }catch (Exception e){

                        }

                    }
                });

            }
        });

    }
    public void calendar(){


            birthDateTextView = findViewById(R.id.birthDateTextView);

            birthDateTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDatePicker();
                }
            });
        }

    private void showDatePicker() {
        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
        // Puedes personalizar las restricciones del calendario según tus necesidades.

        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Selecciona tu fecha de nacimiento");
        builder.setCalendarConstraints(constraintsBuilder.build());

        MaterialDatePicker<Long> datePicker = builder.build();

        datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selection) {
                // Manejar la fecha seleccionada
                Calendar calendar = Calendar.getInstance();
                // Establecer la zona horaria del dispositivo
                calendar.setTimeZone(TimeZone.getDefault());
                calendar.setTimeInMillis(selection);

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                // Establecer la zona horaria del nuevo Calendar
                dateFormat.setTimeZone(TimeZone.getDefault());

                String selectedDate = dateFormat.format(calendar.getTime());

                birthDateTextView.setText(selectedDate);
            }
        });

        datePicker.show(getSupportFragmentManager(), "DATE_PICKER_TAG");
    }
    private void guardarPersona(){
        EditText auxcedula=findViewById(R.id.cedula);
        EditText auxnombre=findViewById(R.id.Nombre);
        EditText auxnombre1=findViewById(R.id.Nombre1);
        EditText auxapelldio=findViewById(R.id.Apellido);
        EditText auxapellido2=findViewById(R.id.Apellido1);
        EditText auxtelefono=findViewById(R.id.telefono);
        Persona persona = new Persona();
        persona.setCedula_persona(auxcedula.getText().toString());
        persona.setNombre(auxnombre.getText().toString());
        persona.setNombre2(auxnombre1.getText().toString());
        persona.setApellido(auxapelldio.getText().toString());
        persona.setApellido2(auxapellido2.getText().toString());
        persona.setTelefono(auxtelefono.getText().toString());

        realizarSolicitudPOST("http://192.168.18.5:8081/api/personas",persona);
    }
    public void guardarClietnes() {
        EditText auxcorreo = findViewById(R.id.email);
        EditText auxContraseña = findViewById(R.id.pasword);
        EditText auxcedula = findViewById(R.id.cedula);

        cliente clienteNuevo= new cliente();
        clienteNuevo.setContrasena(auxContraseña.getText().toString());
        clienteNuevo.setUsuario(auxcorreo.getText().toString());
        clienteNuevo.setCedula_persona(auxcedula.getText().toString());
        realizarSolicitudPOST("http://192.168.18.5:8081/api/clientes",clienteNuevo);

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



}
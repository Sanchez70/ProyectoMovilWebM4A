package com.ista.zhotel;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PantallaPerfilUsuario extends AppCompatActivity {
    private String cedula;
    private Long idCliente;
    private EditText txtTelefono,txtnombre,txtnombre2,txtapellido,txtapellido2,txtcontrasena;
    private Button btnEditar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_perfil_usuario);
        getDatos(PantallaPrincipal.correoUsuario);
        Toast.makeText(getApplicationContext(), PantallaPrincipal.correoUsuario, Toast.LENGTH_LONG).show();
        actualizarDatos();
    }

    public void actualizarDatos(){
        btnEditar = findViewById(R.id.btnEditar);
        txtnombre = findViewById(R.id.txtNombre);
        txtnombre2 = findViewById(R.id.txtNombre2);
        txtapellido = findViewById(R.id.txtApellido);
        txtapellido2 = findViewById(R.id.txtApellido2);
        txtTelefono = findViewById(R.id.txtTelefono);
        txtcontrasena = findViewById(R.id.pasword);
        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //updateDatosUsuario(txtcontrasena.getText().toString(),idCliente);
                updateDatosPersona(txtnombre.getText().toString(),txtnombre2.getText().toString(),txtapellido.getText().toString(),txtapellido2.getText().toString(),txtTelefono.getText().toString(),cedula);
            }
        });
    }

    //Obtenr datos de Cliente
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
                        idCliente = jsonObjectCliente.getLong("idCliente");
                        String contrasena = jsonObjectCliente.getString("contrasena");
                        txtcontrasena = findViewById(R.id.pasword);
                        txtcontrasena.setText(contrasena);
                        getDatosByCedula(cedula);
                        Log.d("He", contrasena);
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

    //Obtener datos de persona
    public void getDatosByCedula(String cedula){
        String url="http://192.168.18.5:8081/api/personas/"+cedula;//endpoint.

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if(response.length()>0) {
                        String nombre = response.getString("nombre");
                        String nombre2 = response.getString("nombre2");
                        String apellido = response.getString("apellido");
                        String apellido2 = response.getString("apellido2");
                        String telefono = response.getString("telefono");
                        txtnombre = findViewById(R.id.txtNombre);
                        txtnombre2 = findViewById(R.id.txtNombre2);
                        txtapellido = findViewById(R.id.txtApellido);
                        txtapellido2 = findViewById(R.id.txtApellido2);
                        txtTelefono = findViewById(R.id.txtTelefono);
                        txtnombre.setText(nombre);
                        txtnombre2.setText(nombre2);
                        txtapellido.setText(apellido);
                        txtapellido2.setText(apellido2);
                        txtTelefono.setText(telefono);
                        Log.d("He", apellido);
                        Log.d("He", nombre);
                    }else{
                        Toast.makeText(getApplicationContext(), "Persona no encontrada", Toast.LENGTH_LONG).show();
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
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }


    //ACTUALIZAR DATOS DE USUARIO
    public void updateDatosUsuario(String contrasena,Long id){
        String url="http://192.168.18.5:8081/api/clientes/"+id;

        JSONObject requestBodyUsuario = new JSONObject();
        try{
            requestBodyUsuario.put("contrasena",contrasena);
        }catch(JSONException j){
            j.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequestUsuario = new JsonObjectRequest(Request.Method.PUT, url, requestBodyUsuario, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(getApplicationContext(), "ACTUALIZACIÓN CORRECTA", Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(this).add(jsonObjectRequestUsuario);
    }

    //ACTUALIZAR DATOS DE PERSONA
    public void updateDatosPersona(String nom1,String nom2, String ape1, String ape2, String tel, String cedula){
        String url="http://192.168.18.5:8081/api/personas/"+cedula;

        JSONObject requestBodyPersona = new JSONObject();
        try{
            requestBodyPersona.put("nombre",nom1);
            requestBodyPersona.put("nombre2",nom2);
            requestBodyPersona.put("apellido",ape1);
            requestBodyPersona.put("apellido2",ape2);
            requestBodyPersona.put("telefono",tel);
        }catch(JSONException j){
            j.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequestPersona = new JsonObjectRequest(Request.Method.PUT, url, requestBodyPersona, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Toast.makeText(getApplicationContext(), "ACTUALIZACIÓN CORRECTA", Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(this).add(jsonObjectRequestPersona);
    }
}
package com.ista.zhotel;

import static com.ista.zhotel.PantallaPrincipal.habitacion;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.ista.zhotel.model.Habi;
import com.ista.zhotel.model.MyAdapter;
import com.ista.zhotel.model.Servi;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Servicio extends AppCompatActivity {
    RecyclerView recyclerView;
    MyAdapter adapter;
    ArrayList<Servi> servis = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servicio);
        getDatos();
        recyclerView = findViewById(R.id.recyclerViewhabi);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyAdapter(this, servis);
        recyclerView.setAdapter(adapter);
        List<Habi> habitacion = PantallaPrincipal.habitacion;
        Habi opcionSeleccione = new Habi();
        opcionSeleccione.setIdHabitaciones(0L);
        habitacion.add(0, opcionSeleccione);
        Spinner spinner = (Spinner) findViewById(R.id.spinner1);
        ArrayAdapter<Habi> adapterh = new ArrayAdapter<Habi>(this, android.R.layout.simple_spinner_item, habitacion);
        adapterh.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterh);
        adapter.setOnItemClickListener(new MyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final int position) {
                Habi habitacionSeleccionada = (Habi) spinner.getSelectedItem();
                if (habitacionSeleccionada != null && habitacionSeleccionada.getIdHabitaciones() != 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Servicio.this);
                    builder.setTitle("Describa su Servicio");
                    final EditText input = new EditText(Servicio.this);
                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                    builder.setView(input);
                    builder.setPositiveButton("Solicitar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String descripcion = input.getText().toString();
                            long idHabitaciones = habitacionSeleccionada.getIdHabitaciones();
                            Servi serviSeleccionado = servis.get(position);
                            long idTipo_servicio = serviSeleccionado.getIdTipo_servicio();
                            String estado = "Pendiente";
                            guardarDatos(descripcion, idHabitaciones, idTipo_servicio, estado);
                            new AlertDialog.Builder(Servicio.this)
                                    .setTitle("Confirmación")
                                    .setMessage("Servicio solicitado correctamente")
                                    .setPositiveButton(android.R.string.ok, null)
                                    .setIcon(android.R.drawable.ic_dialog_info)
                                    .show();
                        }
                    });
                    builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                } else {
                    new AlertDialog.Builder(Servicio.this)
                            .setTitle("Alerta")
                            .setMessage("Selecciona la habitación primero")
                            .setPositiveButton(android.R.string.yes, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            }
        });
    }

    private void getDatos(){
        servis.clear();
        String url="http://192.168.40.228:8081/api/tiposervicio";//
        //String url="http://192.168.0.119:8081/api/tiposervicio";
        JsonArrayRequest jsonArrayRequest= new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                pasarJson(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Tu mensaje aquí", Toast.LENGTH_SHORT).show();Toast.makeText(Servicio.this, "Error al obtener datos", Toast.LENGTH_SHORT).show();
            }
        }
        );
        Volley.newRequestQueue(this).add(jsonArrayRequest);//hacemos la peticion al API
    }

    private void pasarJson( JSONArray array){
        for(int i=0;i<array.length();i++){
            JSONObject json=null;
            Servi cliente= new Servi();
            try {
                json=array.getJSONObject(i);
                cliente.setIdTipo_servicio(json.getInt("idTipo_servicio"));//como viene del API
                cliente.setTitulo(json.getString("titulo"));
                cliente.setDescripcion(json.getString("descripciontipo"));
                String base64Image = json.getString("foto");
                if (base64Image.startsWith("data:image/jpeg;base64,")) {
                    base64Image = base64Image.substring("data:image/jpeg;base64,".length());
                } else if (base64Image.startsWith("data:image/jpg;base64,")) {
                    base64Image = base64Image.substring("data:image/jpg;base64,".length());
                } else if (base64Image.startsWith("data:image/png;base64,")) {
                    base64Image = base64Image.substring("data:image/png;base64,".length());
                }
                cliente.setFoto(base64Image);
                servis.add(cliente);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        adapter.notifyDataSetChanged();
    }

    private void guardarDatos(String descripcion, Long idHabitaciones, Long idTipo_servicio, String estado) {
        String url = "http://192.168.40.228:8081/api/servicio";
        //String url = "http://192.168.0.119:8081/api/servicio";
        JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put("descripcion", descripcion);
            jsonBody.put("idHabitaciones", idHabitaciones);
            jsonBody.put("idTipo_servicio", idTipo_servicio);
            jsonBody.put("estado", estado);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // manejar el error
            }
        });
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

    public void Perfil(View view){
        Intent perfil = new Intent(this, PantallaPerfilUsuario.class);
        startActivity(perfil);
    }

    public void Sesion(View v) {
        FirebaseAuth.getInstance().signOut();
        Intent intent= new Intent(getApplicationContext(), Login.class);
        startActivity(intent);
        finish();
    }
}
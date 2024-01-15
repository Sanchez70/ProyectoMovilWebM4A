package com.ista.zhotel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.ista.zhotel.model.MyAdapter;
import com.ista.zhotel.model.Servi;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Servicio extends AppCompatActivity {
    RecyclerView recyclerView;
    MyAdapter adapter;
    ArrayList<Servi> servis = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servicio);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyAdapter(this, servis);
        recyclerView.setAdapter(adapter);

        getDatos();
    }

    private void getDatos(){
        String url="http://192.168.40.228:8080/api/tiposervicio";//endpoint.
        JsonArrayRequest jsonArrayRequest= new JsonArrayRequest(url, new Response.Listener<JSONArray>() {

            @Override
            public void onResponse(JSONArray response) {
                pasarJson(response);
                Log.d("Response", response.toString());
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
            Servi servi=new Servi();
            try {
                json=array.getJSONObject(i);
                servi.setId(json.getInt("id"));//como viene del API
                servi.setTitulo(json.getString("titulo"));
                servi.setDescripcion(json.getString("descripcion"));
                //servi.setFoto(json.getString("email"));
                servis.add(servi);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        adapter.notifyDataSetChanged();
    }
}
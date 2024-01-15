package com.ista.zhotel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class PantallaPrincipal extends AppCompatActivity {
    Spinner comboCate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_principal);


        comboCate=(Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter= ArrayAdapter.createFromResource(this,R.array.categorias, android.R.layout.simple_spinner_item);
        comboCate.setAdapter(adapter);
    }
    public void Iniciar(View view){
        Intent servicio = new Intent(this, Servicio.class);
        startActivity(servicio);
    }
}
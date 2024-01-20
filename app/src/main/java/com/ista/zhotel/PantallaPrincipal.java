package com.ista.zhotel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class PantallaPrincipal extends AppCompatActivity {
    Spinner comboCate;
    FirebaseAuth auth;
    Button inicio;
    static String correoUsuario;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_principal);
        inicio= findViewById(R.id.button4);
        Log.d("TAG", "Respuesta del servidor: " + correoUsuario);
                inicio.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirebaseAuth.getInstance().signOut();
                        Intent intent= new Intent(getApplicationContext(), Login.class);
                        startActivity(intent);
                        finish();
                    }
                });


        comboCate=(Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter= ArrayAdapter.createFromResource(this,R.array.categorias, android.R.layout.simple_spinner_item);
        comboCate.setAdapter(adapter);
    }
    public void Iniciar(View view){
        Intent servicio = new Intent(this, Servicio.class);
        startActivity(servicio);
    }

    public void Reservar(View view){
        Intent reservar = new Intent(this, PantallaReservar.class);
        startActivity(reservar);
    }

    public void Perfil(View view){
        Intent perfil = new Intent(this, PantallaPerfilUsuario.class);
        startActivity(perfil);
    }
}
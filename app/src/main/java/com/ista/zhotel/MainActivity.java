package com.ista.zhotel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void Registro(View view){
        Intent registro = new Intent(this, Registrar.class);
        startActivity(registro);
    }
    public void Iniciar(View view){
        Intent registro = new Intent(this, Registrar.class);
        startActivity(registro);
    }
}
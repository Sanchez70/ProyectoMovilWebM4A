package com.ista.zhotel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth auth;
    Button inicio;
    FirebaseUser user;
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
        Intent registro1 = new Intent(this, Login.class);
        startActivity(registro1);
    }

}
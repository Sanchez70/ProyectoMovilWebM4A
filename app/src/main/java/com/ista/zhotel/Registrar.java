package com.ista.zhotel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Registrar extends AppCompatActivity {
    TextInputEditText emailText, paswordText;
    Button boton1;
    TextView textView1;
    private FirebaseAuth mAuth;
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
        TextView textView = findViewById(R.id.textView);
        CalendarView calendarView = findViewById(R.id.calendarView);

        // Establecer un listener para manejar eventos de selección de fecha
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                // Manejar la fecha seleccionada
                String selectedDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                Toast.makeText(Registrar.this, "Fecha seleccionada: " + selectedDate, Toast.LENGTH_SHORT).show();
            }
        });
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
                        if (task.isSuccessful()) {
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
                    }
                });

            }
        });

    }



}
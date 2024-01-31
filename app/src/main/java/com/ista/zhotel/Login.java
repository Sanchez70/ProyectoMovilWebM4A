package com.ista.zhotel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    EditText emailTextLogin, paswordTextLogin;
    Button botonLogin;
    TextView textView2;
    private FirebaseAuth mAuth;

    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent= new Intent(getApplicationContext(),PantallaPrincipal.class);
            startActivity(intent);
            finish();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailTextLogin= findViewById(R.id.emailLogin);
        paswordTextLogin=findViewById(R.id.paswordLogin);
        mAuth = FirebaseAuth.getInstance();
        botonLogin= findViewById(R.id.buttonLogin);
        textView2 = findViewById(R.id.textViewLo);
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(getApplicationContext(),Registrar.class);
                startActivity(intent);
                finish();
            }
        });
        botonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailUser= emailTextLogin.getText().toString().trim();
                String passwordlUser= paswordTextLogin.getText().toString().trim();
                if (TextUtils.isEmpty(emailUser)){
                    Toast.makeText(Login.this, "Enten email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(passwordlUser)){
                    Toast.makeText(Login.this, "Enter pasword", Toast.LENGTH_SHORT).show();
                    return;

                }
                mAuth.signInWithEmailAndPassword(emailUser, passwordlUser).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            EditText auxUsuario=findViewById(R.id.emailLogin);
                            PantallaPrincipal.correoUsuario= auxUsuario.getText().toString();
                            Toast.makeText(getApplicationContext(), "Ingreso Exitoso", Toast.LENGTH_SHORT).show();
                            Intent intent= new Intent(getApplicationContext(),PantallaPrincipal.class);
                            startActivity(intent);
                            finish();

                        } else {;
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            ;
                        }
                    }
                });
            }
        });
    }
}
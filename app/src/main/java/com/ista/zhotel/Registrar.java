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
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
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

}
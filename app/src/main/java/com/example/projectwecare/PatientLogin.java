package com.example.projectwecare;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class PatientLogin extends AppCompatActivity {

    EditText inputmobile;
    ProgressBar progressBar;
    Button getotp;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(PatientLogin.this, Home_Activity.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_login);

        inputmobile = findViewById(R.id.inputmobile);
        progressBar = findViewById(R.id.progressbar1);
        getotp = findViewById(R.id.btnsend);

        getotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isvalidatemobile()) {
                    progressBar.setVisibility(View.VISIBLE);
                    getotp.setVisibility(View.INVISIBLE);
                    sendotp();
                }
            }
        });

    }

    private boolean isvalidatemobile() {
        if (!Patterns.PHONE.matcher(inputmobile.getText().toString()).matches()) {
            inputmobile.setError("Enter valid Number");
            return false;
        } else if (inputmobile.getText().toString().trim().isEmpty()) {
            inputmobile.setError("Enter Mobile");
            return false;
        } else {
            return true;
        }
    }

    private void sendotp() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Registered Patients");
        reference.orderByChild("contactnumbet").equalTo(inputmobile.getText().toString()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    PhoneAuthProvider.getInstance().verifyPhoneNumber("+91" + inputmobile.getText().toString(),
                            60,
                            TimeUnit.SECONDS,
                            PatientLogin.this,
                            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                                @Override
                                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                    progressBar.setVisibility(View.GONE);
                                    getotp.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onVerificationFailed(@NonNull FirebaseException e) {
                                    progressBar.setVisibility(View.GONE);
                                    getotp.setVisibility(View.VISIBLE);
                                    Toast.makeText(PatientLogin.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                    progressBar.setVisibility(View.GONE);
                                    getotp.setVisibility(View.VISIBLE);

                                    Intent intent = new Intent(getApplicationContext(), verify_otp.class);
                                    intent.putExtra("mobile", inputmobile.getText().toString());
                                    intent.putExtra("verificationId", s);
                                    startActivity(intent);

                                }
                            });
                } else {
                    progressBar.setVisibility(View.INVISIBLE);
                    getotp.setVisibility(View.VISIBLE);
                    Toast.makeText(PatientLogin.this, "Number Does not exist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
package com.example.study2gather;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {
    private Button btnresetpword;
    private EditText txtresetpword;
    private ProgressBar pgbar;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_password);
//        pgbar = findViewById(R.id.pgbarresetpassword);
//        txtresetpword = findViewById(R.id.txtresetPassword);
//        btnresetpword = findViewById(R.id.btnresetPassword);
        auth = FirebaseAuth.getInstance();
        btnresetpword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });
    }

    private void resetPassword() {
        String email = txtresetpword.getText().toString().trim();
        //add validations
        pgbar.setVisibility(View.VISIBLE);
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(ForgotPassword.this, "Check your email to reset your password!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(ForgotPassword.this, "Something went wrong Try again!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
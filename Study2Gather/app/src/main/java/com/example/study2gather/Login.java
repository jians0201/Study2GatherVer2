package com.example.study2gather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class Login extends AppCompatActivity implements View.OnClickListener{

    private LinearLayout linearLayoutLogin;
    private TextInputLayout loginEmail, loginPword;
    private ProgressBar pgbar;

    private FirebaseAuth fAuth;

    private Animation anim;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Launch the layout -> splash.xml
        setContentView(R.layout.login);
        linearLayoutLogin = findViewById(R.id.linearLayoutLogin);
        loginEmail = findViewById(R.id.loginEmailAddress);
        loginPword = findViewById(R.id.loginPassword);
        pgbar = findViewById(R.id.loginProgressBar);
        fAuth = FirebaseAuth.getInstance();

        prefs = getSharedPreferences("login",MODE_PRIVATE);
        anim = AnimationUtils.loadAnimation(this, R.anim.fade);
        linearLayoutLogin.startAnimation(anim);
        //check if user is already logged in
        if(prefs.getBoolean("logged",false)){
            startActivity(new Intent(Login.this, MainActivity.class));
        }
    }

    private boolean validateEmail() {
        String email = Objects.requireNonNull(loginEmail.getEditText()).getText().toString().trim();
        if (email.isEmpty()) {
            loginEmail.setError(getString(R.string.loginEmptyError));
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            loginEmail.setError(getString(R.string.loginEmailError));
            return false;
        } else {
            loginEmail.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {
        String password = Objects.requireNonNull(loginPword.getEditText()).getText().toString().trim();
        if (password.isEmpty()) {
            loginPword.setError(getString(R.string.loginEmptyError));
            return false;
        } else if (password.length() < 8) {
            loginPword.setError(getString(R.string.loginPasswordError));
            return false;
        } else {
            loginPword.setError(null);
            return true;
        }
    }

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.loginLoginButton:
                String email = loginEmail.getEditText().getText().toString().trim();
                String password = loginPword.getEditText().getText().toString().trim();
                if (validateEmail() && validatePassword()) {
                    pgbar.setVisibility(View.VISIBLE);
                    fAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                pgbar.setVisibility(View.GONE);
                                prefs.edit().putBoolean("logged",true).apply();
                                startActivity(new Intent(Login.this, MainActivity.class));
                            } else {
                                Toast.makeText(Login.this, "Failed to login! Email or Password is incorrect!", Toast.LENGTH_LONG).show();
                                pgbar.setVisibility(View.GONE);
                            }
                        }
                    }).addOnFailureListener(e -> {
                        Log.d("Login Fail", e.getMessage());
                    });
                }
                break;
            case R.id.loginRegisterButton:
                startActivity(new Intent(Login.this, Registration.class));
                break;
            case R.id.loginForgotPasswordBtn:
                startActivity(new Intent(Login.this, ForgotPassword.class));
                break;
        }
    }
}

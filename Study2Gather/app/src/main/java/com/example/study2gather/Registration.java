package com.example.study2gather;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import android.util.Patterns;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class Registration extends AppCompatActivity implements View.OnClickListener{
    // Global vars
    private String DOBStr = "";
    private String gender = "";

    private TextInputLayout rgUname, rgEmail, rgPword, rgCfmPword;
    private TextView DOBTextField, rgSexLabel, rgDOBLabel;
    private ProgressBar pgbar;
    private RadioButton rgSexMale, rgSexFemale;

    private FirebaseAuth fAuth;
    private FirebaseDatabase fDb;

    private Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Launch the layout -> splash.xml
        setContentView(R.layout.registration);
        DOBTextField = findViewById(R.id.registerDOBTxt);
        rgUname = findViewById(R.id.registerUsername);
        rgEmail = findViewById(R.id.registerEmail);
        rgPword = findViewById(R.id.registerPassword);
        rgCfmPword = findViewById(R.id.registerConfirmPassword);
        fAuth = FirebaseAuth.getInstance();
        rgSexLabel = findViewById(R.id.registerSexLabel);
        rgSexMale = findViewById(R.id.registerSexMale);
        rgSexFemale = findViewById(R.id.registerSexFemale);
        rgDOBLabel = findViewById(R.id.registerDOBLabel);
        pgbar = findViewById(R.id.registerProgressBar);
    }

    // Handles radio button validations
    public void onRadioButtonClicked(View v){
        boolean checked = ((RadioButton) v).isChecked();
        rgSexFemale.setError(null);
        rgSexMale.setError(null);
        switch(v.getId()){
            case R.id.registerSexMale:
                if(checked){
                    // Code for checking male
                    gender = "male";
                }
                break;
            case R.id.registerSexFemale:
                if(checked){
                    // Code for checking female
                    gender = "female";
                }
                break;
        }
    }

    private boolean validateUsername() {
        String uname = Objects.requireNonNull(rgUname.getEditText()).getText().toString().trim();
        if (uname.isEmpty()) {
            rgUname.setError(getString(R.string.registerEmptyError));
            return false;
        } else if (uname.length() < 4 || uname.length() > 18) {
            rgUname.setError("Username must be 4-18 characters!");
            return false;
        } else {
            rgUname.setError(null);
            return true;
        }
    }

    private boolean validateEmail() {
        String email = Objects.requireNonNull(rgEmail.getEditText()).getText().toString().trim();
        if (email.isEmpty()) {
            rgEmail.setError(getString(R.string.registerEmptyError));
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            rgEmail.setError(getString(R.string.registerEmailError));
            return false;
        } else {
            rgEmail.setError(null);
            return true;
        }
    }

    private boolean validatePassword() {
        String password = Objects.requireNonNull(rgPword.getEditText()).getText().toString().trim();
        if (password.isEmpty()) {
            rgPword.setError(getString(R.string.registerEmptyError));
            return false;
        } else if (password.length() < 8) {
            rgPword.setError(getString(R.string.registerPasswordError));
            return false;
        } else {
            rgPword.setError(null);
            return true;
        }
    }

    private boolean validateConfirmPassword() {
        String password = Objects.requireNonNull(rgPword.getEditText()).getText().toString().trim();
        String confirmPassword = Objects.requireNonNull(rgCfmPword.getEditText()).getText().toString().trim();
        if (confirmPassword.isEmpty()) {
            rgCfmPword.setError(getString(R.string.registerEmptyError));
            return false;
        } else if (!confirmPassword.equals(password)) {
            rgCfmPword.setError(getString(R.string.registerConfirmPasswordError));
            return false;
        } else {
            rgCfmPword.setError(null);
            return true;
        }
    }

    private boolean validateGender() {
        if (gender.isEmpty()) {
            rgSexLabel.setError(getString(R.string.registerSexError));
            rgSexLabel.setError(getString(R.string.registerSexError));
            return false;
        } else {
            rgSexLabel.setError(null);
            rgSexLabel.setError(null);
            return true;
        }
    }

    private boolean validateDOB() {
        if (DOBStr.isEmpty()) {
            rgDOBLabel.setError(getString(R.string.registerDOBError));
            return false;
        } else {
            rgDOBLabel.setError(null);
            return true;
        }
    }

    @Override
    public void onClick(View v){
//        Intent i;
        switch(v.getId()){
            // Handles for DatePicker
            case R.id.registerDOBBtn:
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog dpd;

                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH)+1;
                int year = calendar.get(Calendar.YEAR);

                dpd = new DatePickerDialog(Registration.this, new DatePickerDialog.OnDateSetListener(){
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth){
                        DOBStr = dayOfMonth+" / "+(month+1)+" / "+year;
                        DOBTextField.setText(DOBStr);
                    }
                }, year, month, day);
                dpd.getDatePicker().setMaxDate(new Date().getTime());
                dpd.show();
                break;

            // Handles for Register Button
            case R.id.registerButton:
                //Get text fields
                String username = Objects.requireNonNull(rgUname.getEditText()).getText().toString().trim();
                String email = Objects.requireNonNull(rgEmail.getEditText()).getText().toString().trim();
                String password = Objects.requireNonNull(rgPword.getEditText()).getText().toString().trim();
                String confirmPassword = Objects.requireNonNull(rgCfmPword.getEditText()).getText().toString().trim();

                //Validate Inputs
                if (!validateUsername() | !validateEmail() | !validatePassword() | !validateConfirmPassword() | !validateGender() | !validateDOB()) {
                    return;
                }
                    //Show progress bar
                    pgbar.setVisibility(View.VISIBLE);
                    //Create user
                    fAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        UserObj user = new UserObj(username,email,password,gender,DOBStr);
                                        fDb.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(user)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(Registration.this, "Created new user successfully", Toast.LENGTH_SHORT).show();
                                                            pgbar.setVisibility(View.GONE); //Remove progress bar
                                                            i = new Intent(getApplicationContext(), Login.class);
                                                            startActivity(i);
                                                        } else {
                                                            Toast.makeText(Registration.this, "Failed to Register! Try Again!", Toast.LENGTH_SHORT).show();
                                                            pgbar.setVisibility(View.GONE); //Remove progress bar
                                                        }
                                                    }
                                                });
                                    } else {
                                        Toast.makeText(Registration.this, "Failed to Register! Try Again!", Toast.LENGTH_SHORT).show();
                                        pgbar.setVisibility(View.GONE); //Remove progress bar
                                    }
                                }
                            });
                break;
        }
    }
}

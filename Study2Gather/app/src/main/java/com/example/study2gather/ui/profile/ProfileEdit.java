package com.example.study2gather.ui.profile;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;
import android.content.DialogInterface;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.study2gather.R;
import com.example.study2gather.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

public class ProfileEdit extends AppCompatActivity implements View.OnClickListener{
    private ImageView profilePic;
    private TextInputEditText profileName, profileEmail, profileSchool, profileLocation;
    private FloatingActionButton btnEditProfile;
    private RadioButton rgSexMale, rgSexFemale;
    private TextView profileDOB;

    private FirebaseUser user;
    private DatabaseReference userRef;
    private StorageReference profilePicRef;

    private String uid, gender;
    private User userProfile;
    private Uri newimguri;

    private String userDOB;

    // For location detection
    private static final int REQUEST_CODE_PERMISSION = 2;
    String mPermission = Manifest.permission.ACCESS_FINE_LOCATION;
    // LocationTracker class
    LocationTracker gps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Launch the layout -> profile_edit.xml
        setContentView(R.layout.profile_edit);

        profilePic = findViewById(R.id.editProfilePic);
        profileName = findViewById(R.id.profileEditName);
        profileEmail = findViewById(R.id.profileEditEmail);
        rgSexMale = findViewById(R.id.profileEditSexMale);
        rgSexFemale = findViewById(R.id.profileEditSexFemale);
        profileDOB = findViewById(R.id.profileEditDOBTxt);
        profileSchool = findViewById(R.id.profileEditSchool);
        profileLocation = findViewById(R.id.profileEditLocation);

        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        userRef = FirebaseDatabase.getInstance().getReference("Users");
        profilePicRef = FirebaseStorage.getInstance().getReference("profileImages").child(uid+"_profile.jpg");

        //get existing user profile photo
        profilePicRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Picasso.get().load(task.getResult()).into(profilePic);
                }
                else {
                    //load temp image
                    profilePic.setImageResource(R.drawable.ic_profile_user_24dp);
                }
            }
        });

        //get userinfo
        userRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userProfile = snapshot.getValue(User.class);
                if (userProfile != null) {
                    profileName.setText(userProfile.getUsername());
                    profileEmail.setText(userProfile.getEmail());
                    userDOB = userProfile.getDob();
                    profileDOB.setText(userDOB);
                    profileSchool.setText(userProfile.getSchool());
                    profileLocation.setText(userProfile.getLocation());
                    gender = userProfile.getGender();
                    if (gender.equals("male")) rgSexMale.setChecked(true);
                    else rgSexFemale.setChecked(true);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileEdit.this,"Something Went Wrong",Toast.LENGTH_LONG).show();
            }
        });

    }

    // create an action bar button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // R.menu.mymenu is a reference to an xml file named mymenu.xml which should be inside your res/menu directory.
        // If you don't have res/menu, just create a directory named "menu" inside res
        getMenuInflater().inflate(R.menu.profile_edit_menu, menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_clear_white_24dp);
        return super.onCreateOptionsMenu(menu);
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //for the return and tick btn
        int id = item.getItemId();
        if (id == android.R.id.home ) {
            finish();
            return true;
        }else if (id == R.id.profileEditDoneBtn) {
            //Add validation here before calling update profile
            updateProfile();
        }
        return super.onOptionsItemSelected(item);
    }

    public void onRadioButtonClicked(View v){
        boolean checked = ((RadioButton) v).isChecked();
        switch(v.getId()){
            case R.id.profileEditSexMale:
                if(checked) gender = "male";
                break;
            case R.id.profileEditSexFemale:
                if(checked) gender = "female";
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null) {
            newimguri=data.getData();
            profilePic.setImageURI(newimguri);
        }
    }

    // Handling button clicks
    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            // Handles for DatePicker
            case R.id.profileEditDOBBtn:
                // DATE OF BIRTH EDIT HERE
                int day;
                int month;
                int year;

                if(userDOB == null){
                    Calendar calendar = Calendar.getInstance();

                    day = calendar.get(Calendar.DAY_OF_MONTH);
                    month = calendar.get(Calendar.MONTH) + 1;
                    year = calendar.get(Calendar.YEAR);
                }else{
                    final int firstPos = userDOB.indexOf("/");
                    final int lastPos = userDOB.lastIndexOf("/");
                    day = Integer.parseInt(userDOB.substring(0, firstPos).trim());
                    month = Integer.parseInt(userDOB.substring(firstPos+1, lastPos).trim()) - 1;
                    year = Integer.parseInt(userDOB.substring(lastPos+1).trim());
                }
                DatePickerDialog dpd = new DatePickerDialog(ProfileEdit.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String DOBstring = dayOfMonth + " / " + (month + 1) + " / " + year;
                        profileDOB.setText(DOBstring);
                    }
                }, year, month, day);
                dpd.getDatePicker().setMaxDate(new Date().getTime());
                dpd.show();
                break;
                // Handles for profile picture edit
            case R.id.profileEditPicBtn:
                Intent i = new Intent();
                i.setType("image/'");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(i,1);
                break;
                // Handles for detecting current location
            case R.id.profileDetectLocation:
                try {
                    if (ActivityCompat.checkSelfPermission(this, mPermission)
                            != PackageManager.PERMISSION_GRANTED) {

                        ActivityCompat.requestPermissions(this, new String[]{mPermission},
                                REQUEST_CODE_PERMISSION);
                        // If any permission above not allowed by user, this condition will
                        // execute every time, else your else part will work
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                gps = new LocationTracker(ProfileEdit.this);

                // check if GPS enabled
                if (gps.canGetLocation()) {

                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();

                    // \n is for new line
//                    Toast.makeText(getApplicationContext(), "Your Location is - \nLat: "
//                            + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();

                    String url = "https://api.bigdatacloud.net/data/reverse-geocode-client?latitude="+latitude+"&longitude="+longitude+"&localityLanguage=en";

                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                            (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                                @Override
                                public void onResponse(JSONObject response) {
                                   try{
                                        profileLocation.setText(response.getString("city")+", "+response.getString("countryName"));
                                    }catch(JSONException e){
                                        e.printStackTrace();
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    // TODO: Handle error

                                }
                            });

                    // Access the RequestQueue through your singleton class.
                    MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
                } else {
                    // can't get location
                    // GPS or Network is not enabled
                    // Ask user to enable GPS/network in settings
                    gps.showSettingsAlert();
                }


                break;
        }
    }

    private void uploadImageToFirebase() {
        profilePicRef.putFile(newimguri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(ProfileEdit.this, "Image Changed Successfully", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ProfileEdit.this, "Image Change Failed", Toast.LENGTH_SHORT).show();
            }
        });
        newimguri = null;
    }

    public void updateProfile() {
        String name = profileName.getText().toString().trim();
        String email = profileEmail.getText().toString().trim();
        String school = profileSchool.getText().toString().trim();
        String location = profileLocation.getText().toString().trim();
        String dob = profileDOB.getText().toString().trim();
        Boolean changedProfile = false;
        Boolean hasErr = false;
        String alertMsgs = "";


        if (!userProfile.getUsername().equals(name)) {
            if(name.length() < 4 || name.length() > 18){
                hasErr = true;
                profileName.setText(userProfile.getUsername());
                alertMsgs += "Name should be between 4 to 18 characters only\n";
            }else{
                userProfile.setUsername(name);
                changedProfile = true;
            }
        }
        if (!userProfile.getEmail().equals(email)) {
            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                hasErr = true;
                profileEmail.setText(userProfile.getEmail());
                alertMsgs += "The email is invalid!";
            }else{
                user.updateEmail(email).addOnSuccessListener(new OnSuccessListener<Void>(){
                    @Override
                    public void onSuccess(Void aVoid){
                        Toast.makeText(ProfileEdit.this, "Email has been changed", Toast.LENGTH_SHORT).show();
                        userProfile.setEmail(email);
                        userRef.child(uid).setValue(userProfile).addOnCompleteListener(new OnCompleteListener<Void>(){
                            @Override
                            public void onComplete(@NonNull Task<Void> task){
                                if(task.isSuccessful()){
                                    Toast.makeText(ProfileEdit.this, "Changed User Profile successfully", Toast.LENGTH_SHORT).show();
                                }else{
                                    Toast.makeText(ProfileEdit.this, "Failed to Change Profile! Try Again!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener(){
                    @Override
                    public void onFailure(@NonNull Exception e){
                        Toast.makeText(ProfileEdit.this, "Email was not changed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
        if (!school.isEmpty() && (userProfile.getSchool() == null || !userProfile.getSchool().equals(school))) {
            userProfile.setSchool(school);
            changedProfile = true;
        }
        if (!location.isEmpty() && (userProfile.getLocation() == null || !userProfile.getLocation().equals(location))) {
            userProfile.setLocation(location);
            changedProfile = true;
        }
        if (!userProfile.getGender().equals(gender)) {
            userProfile.setGender(gender);
            changedProfile = true;
        }
        if (!userProfile.getDob().equals(dob)) {
            userProfile.setDob(dob);
            changedProfile = true;
        }
        if (newimguri!=null) {
            uploadImageToFirebase();
        }

        // If error do this
        if(hasErr){
            AlertDialog.Builder builder = new AlertDialog.Builder(ProfileEdit.this).setTitle("Invalid inputs!").setMessage(alertMsgs+"\n\nValid inputs are still updated").setCancelable(false).setNeutralButton("OK", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int id){
                    dialog.cancel();
                }
            });
            builder.create().show();
        }
        if (changedProfile) { //update profile
            userRef.child(uid).setValue(userProfile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(ProfileEdit.this, "Changed User Profile successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ProfileEdit.this, "Failed to Change Profile! Try Again!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            changedProfile = false;
        }
    }
}

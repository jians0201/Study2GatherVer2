package com.example.study2gather.ui.profile;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.study2gather.R;
import com.example.study2gather.Registration;
import com.example.study2gather.UserObj;
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
    private UserObj userProfile;
    private Uri newimguri;

    private String userDOB;

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
                userProfile = snapshot.getValue(UserObj.class);
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
        return super.onCreateOptionsMenu(menu);
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //for the tick btn
        int id = item.getItemId();
        if (id == R.id.profileEditDoneBtn) {
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
            case R.id.profileEditPicBtn:
                Intent i = new Intent();
                i.setType("image/'");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(i,1);
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

        if (!userProfile.getUsername().equals(name)) {
            userProfile.setUsername(name);
            changedProfile = true;
        }
        if (!userProfile.getEmail().equals(email)) {
            user.updateEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(ProfileEdit.this, "Email has been changed", Toast.LENGTH_SHORT).show();
                    userProfile.setEmail(email);
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
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ProfileEdit.this, "Email was not changed",Toast.LENGTH_SHORT).show();
                    Log.d("FAILURE","FAILURE TO UPDATE EMAIL");
                }
            });
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

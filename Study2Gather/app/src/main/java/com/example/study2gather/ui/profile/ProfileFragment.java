package com.example.study2gather.ui.profile;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.study2gather.R;
import com.example.study2gather.UserObj;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class ProfileFragment extends Fragment {

    private ImageView profilePic;
    private TextView profileName, profileEmail, profileBirthday, profileSchool, profileLocation;
    //gender together with name

    private FirebaseUser user;
    private DatabaseReference ref;
    private StorageReference profilePicRef;

    private String uid;
    private UserObj userProfile;

    private ProfileViewModel profileViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);

        profilePic = root.findViewById(R.id.profilePic);
        profileName = root.findViewById(R.id.profileName);
        profileEmail = root.findViewById(R.id.profileEmail);
        profileBirthday = root.findViewById(R.id.profileBirthday);
        profileSchool = root.findViewById(R.id.profileSchool);
        profileLocation = root.findViewById(R.id.profileLocation);

        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        ref = FirebaseDatabase.getInstance().getReference("Users");
        profilePicRef = FirebaseStorage.getInstance().getReference("profileImages").child(uid+"_profile.jpg");

        //get userinfo
        ref.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userProfile = snapshot.getValue(UserObj.class);
//                Log.i("USER CHILDREN",Long.toString( snapshot.getChildrenCount()));
                if (userProfile != null) {
                    profileName.setText(userProfile.getUsername());
                    profileEmail.setText(userProfile.getEmail());
                    profileBirthday.setText(userProfile.getDob());
                    if (userProfile.getSchool() == null) {
                        profileSchool.setText("None");
                    } else {
                        profileSchool.setText(userProfile.getSchool());
                    }
                    if (userProfile.getLocation() == null) {
                        profileLocation.setText("None");
                    } else {
                        profileLocation.setText(userProfile.getLocation());
                    }
                    if (userProfile.getGender().equals("male")) {
                        profileName.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_male_white_12dp,0);
                    } else {
                        profileName.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_female_white_12dp,0);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(),"Something Went Wrong",Toast.LENGTH_LONG).show();
            }
        });

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



        return root;
    }
}
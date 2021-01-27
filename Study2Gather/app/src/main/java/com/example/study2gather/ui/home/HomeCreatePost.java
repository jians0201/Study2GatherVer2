package com.example.study2gather.ui.home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.study2gather.Post;
import com.example.study2gather.R;
import com.example.study2gather.ui.profile.ProfileEdit;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Date;

public class HomeCreatePost extends AppCompatActivity implements View.OnClickListener {
    private ImageButton iBtnBack, iBtnInsertImg;
    private Button btnCreatePost;
    private ImageView iVProfilePic, iVPostPic;
    private EditText eTPostText;

    private FirebaseUser user;
    private StorageReference profilePicRef, imagesRef;
    private DatabaseReference postsRef;

    private String uid;
    private long maxId;
    private Uri postimguri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_post_create);

        iBtnBack = findViewById(R.id.homePostCreateClearButton);
        iBtnInsertImg = findViewById(R.id.homePostCreateInsertImgButton);
        btnCreatePost = findViewById(R.id.homePostCreateButton);
        iVProfilePic = findViewById(R.id.homePostCreateProfilePic);
        iVPostPic = findViewById(R.id.homePostCreatePostPic);
        eTPostText = findViewById(R.id.editTextTextMultiLine);

        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        profilePicRef = FirebaseStorage.getInstance().getReference("profileImages").child(uid+"_profile.jpg");
        imagesRef = FirebaseStorage.getInstance().getReference("images");
        postsRef = FirebaseDatabase.getInstance().getReference("Posts");

        postsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) { if (snapshot.exists()) maxId = (snapshot.getChildrenCount()); }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        //get existing user profile photo
        profilePicRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Picasso.get().load(task.getResult()).into(iVProfilePic);
                }
                else {
                    //load temp image
                    iVProfilePic.setImageResource(R.drawable.ic_profile_user_24dp);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null) {
            postimguri=data.getData();
            iVPostPic.setImageURI(postimguri);
        }
    }

    private void createNewPost(String postText) {
        imagesRef.putFile(postimguri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(HomeCreatePost.this, "Post Image Uploaded Successfully", Toast.LENGTH_SHORT).show();
                Date date = new Date();
                Post post = new Post(uid, postText, 0,date.getTime());
                postsRef.child("post"+(maxId+1)).setValue(post);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(HomeCreatePost.this, "Post Image Upload Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.homePostCreateClearButton:
                finish();
                break;
            case R.id.homePostCreateButton:
                String postText = eTPostText.getText().toString().trim();
                if (iVPostPic.getDrawable()!=null && !postText.isEmpty()) {
//                    createNewPost(postText);
                    Toast.makeText(HomeCreatePost.this, "Creating Post", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.homePostCreatePostPic:
            case R.id.homePostCreateInsertImgButton:
                Intent i = new Intent();
                i.setType("image/'");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(i,1);
                break;
        }
    }
}
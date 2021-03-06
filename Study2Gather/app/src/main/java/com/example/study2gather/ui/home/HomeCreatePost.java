package com.example.study2gather.ui.home;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.study2gather.Post;
import com.example.study2gather.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.UUID;

public class HomeCreatePost extends AppCompatActivity implements View.OnClickListener {
    private ImageView iVProfilePic, iVPostPic;
    private EditText eTPostText;

    private FirebaseUser user;
    private StorageReference profilePicRef, imagesRef;
    private DatabaseReference postsRef;

    private String uid;
    private Uri postimguri;
    private Bitmap postimgbm;
    final int GALLERY_CODE = 1, CAMERA_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_post_create);

        iVProfilePic = findViewById(R.id.homePostCreateProfilePic);
        iVPostPic = findViewById(R.id.homePostCreatePostPic);
        eTPostText = findViewById(R.id.homePostCreateText);

        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        profilePicRef = FirebaseStorage.getInstance().getReference("profileImages").child(uid+"_profile.jpg");
        imagesRef = FirebaseStorage.getInstance().getReference("images");
        postsRef = FirebaseDatabase.getInstance().getReference("Posts");

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

        //focus the text view and show keyboard
        eTPostText.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(eTPostText, InputMethodManager.SHOW_IMPLICIT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==GALLERY_CODE && resultCode==RESULT_OK && data!=null && data.getData()!=null) {
            postimguri=data.getData();
            iVPostPic.setImageURI(postimguri);
        } else if (requestCode==CAMERA_CODE && resultCode==RESULT_OK) {
            postimgbm = (Bitmap) data.getExtras().get("data");
            iVPostPic.setImageBitmap(postimgbm);
            iVPostPic.setImageURI(postimguri);
        }
    }

    private void createNewPost(String postText) {
        final String random = UUID.randomUUID().toString();
        final String randomPostID = "post"+UUID.randomUUID().toString();
        if (postimguri != null) {
            imagesRef.child(random+".jpg").putFile(postimguri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(HomeCreatePost.this, "Post Image Uploaded Successfully", Toast.LENGTH_SHORT).show();
                    Date date = new Date();
                    Post post = new Post(uid, postText, 0, date.getTime(),random+".jpg",randomPostID);
                    postsRef.child(randomPostID).setValue(post);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(HomeCreatePost.this, "Post Image Upload Failed", Toast.LENGTH_SHORT).show();
                }
            });
        } else if (postimgbm != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            postimgbm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            imagesRef.child(random+".jpeg").putBytes(baos.toByteArray()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(HomeCreatePost.this, "Post Image Uploaded Successfully", Toast.LENGTH_SHORT).show();
                    Date date = new Date();
                    Post post = new Post(uid, postText, 0, date.getTime(),random+".jpeg",randomPostID);
                    postsRef.child(randomPostID).setValue(post);
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(HomeCreatePost.this, "Post Image Upload Failed", Toast.LENGTH_SHORT).show();
                }
            });
        }
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
                    Toast.makeText(HomeCreatePost.this, "Creating Post", Toast.LENGTH_SHORT).show();
                    createNewPost(postText);
                }
                break;
            case R.id.homePostCreateInsertImgButton:
                postimgbm = null;
                Intent gallery = new Intent();
                gallery.setType("image/'");
                gallery.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(gallery,GALLERY_CODE);
                break;
            case R.id.homePostCreateAddImgButtonCamera:
                postimguri = null;
                Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(camera, CAMERA_CODE);
                break;
        }
    }

}
package com.example.study2gather.ui.forum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.study2gather.ForumQuestion;
import com.example.study2gather.R;
import com.example.study2gather.ui.home.HomeCreatePost;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public class ForumCreateQuestion extends AppCompatActivity implements View.OnClickListener {
    private ImageView iVProfilePic;
    private EditText eTQnTitle, eTQnDesc;

    private FirebaseUser user;
    private StorageReference profilePicRef;
    private DatabaseReference forumRef;

    private String uid;

    public ForumCreateQuestion() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forum_quest_create);
        iVProfilePic = findViewById(R.id.forumQuestCreateProfilePic);
        eTQnTitle = findViewById(R.id.forumQuestCreateTitle);
        eTQnDesc = findViewById(R.id.forumQuestCreateDesc);

        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        profilePicRef = FirebaseStorage.getInstance().getReference("profileImages").child(uid+"_profile.jpg");
        forumRef = FirebaseDatabase.getInstance().getReference("Forum");

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
        eTQnTitle.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(eTQnTitle, InputMethodManager.SHOW_IMPLICIT);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.forumQuestCreateClearButton:
                finish();
                break;
            case R.id.forumQuestCreateButton:
                String qnTitle = eTQnTitle.getText().toString().trim();
                String qnDesc = eTQnDesc.getText().toString().trim();
                if (!qnTitle.isEmpty() && !qnDesc.isEmpty()) {
                    Toast.makeText(ForumCreateQuestion.this, "Creating Question", Toast.LENGTH_SHORT).show();
                    createNewForumQuestion(qnTitle, qnDesc);
                }
                break;
        }
    }

    private void createNewForumQuestion(String qnTitle, String qnDesc) {
        final String randomQnID = "qn"+UUID.randomUUID().toString();
        Date date = new Date();
        ForumQuestion qn = new ForumQuestion(qnTitle, qnDesc, uid, date.getTime(),0, randomQnID);
        forumRef.child(randomQnID).setValue(qn);
        finish();
    }

}
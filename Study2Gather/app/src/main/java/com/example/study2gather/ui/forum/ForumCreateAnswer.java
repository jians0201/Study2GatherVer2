package com.example.study2gather.ui.forum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.study2gather.ForumAnswer;
import com.example.study2gather.ForumQuestion;
import com.example.study2gather.R;
import com.example.study2gather.UserObj;
import com.google.android.gms.tasks.OnCompleteListener;
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

import java.util.Date;
import java.util.UUID;

public class ForumCreateAnswer extends AppCompatActivity implements View.OnClickListener {
    private ImageView iVProfilePic;
    private EditText eTAnsText;

    private FirebaseUser user;
    private StorageReference profilePicRef;
    private DatabaseReference answersRef, forumRef;

    private String uid, questionID;
    private int questionAnsCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forum_ans_create);
        iVProfilePic = findViewById(R.id.forumAnsCreateProfilePic);
        eTAnsText = findViewById(R.id.forumAnsCreateText);

        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        profilePicRef = FirebaseStorage.getInstance().getReference("profileImages").child(uid+"_profile.jpg");
        forumRef = FirebaseDatabase.getInstance().getReference("Forum");
        answersRef = FirebaseDatabase.getInstance().getReference("ForumAnswers");
//        username = getIntent().getStringExtra("username");
        questionID = getIntent().getStringExtra("questionID");
        questionAnsCount = getIntent().getIntExtra("questionAnsCount",0);

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

//        forumRef.child(questionID).child("ansCount").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) { qnAnsCount = snapshot.getValue(Integer.class); }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) { Toast.makeText(ForumCreateAnswer.this,"Something Went Wrong",Toast.LENGTH_LONG).show(); }
//        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.forumAnsCreateClearButton:
                finish();
                break;
            case R.id.forumAnsCreateButton:
                String ansText = eTAnsText.getText().toString().trim();
                if (!ansText.isEmpty()) {
                    Toast.makeText(ForumCreateAnswer.this, "Creating Answer", Toast.LENGTH_SHORT).show();
                    createNewForumAnswer(ansText);
                }
                break;
        }
    }

    private void createNewForumAnswer(String ansText) {
        //Create Answer
        final String randomAnsID = "ans"+ UUID.randomUUID().toString();
        Date date = new Date();
        ForumAnswer ans = new ForumAnswer(uid,ansText,questionID,randomAnsID,date.getTime());
        answersRef.child(randomAnsID).setValue(ans);
        //Add to ansCount of question
        forumRef.child(questionID).child("ansCount").setValue(questionAnsCount+1);
        finish();
    }
}
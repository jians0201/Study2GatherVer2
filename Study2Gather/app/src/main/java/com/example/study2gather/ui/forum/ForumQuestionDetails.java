package com.example.study2gather.ui.forum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.study2gather.ForumQuestion;
import com.example.study2gather.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

public class ForumQuestionDetails extends AppCompatActivity {
    private TextView tVQnTitle, tVQnDesc, tVQnAuthor, tVQnTimestamp;
    private ImageView iVQnAuthorPic;

    private DatabaseReference answersRef;
//    private FirebaseUser user;
    private StorageReference profilePicsRef;

    private ForumQuestion question;
    private HashMap<String, String> usersListWithName;
//    private ArrayList<ForumQuestionAnswer> mQns = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forum_quest_details);
        tVQnTitle = findViewById(R.id.forumQuestionDetailsQuest);
        tVQnDesc = findViewById(R.id.forumQuestionDetailsDet);
        tVQnAuthor = findViewById(R.id.forumQuestionAskUser);
        tVQnTimestamp = findViewById(R.id.forumQuestionDetailsAskTime);
        iVQnAuthorPic = findViewById(R.id.forumQuestionDetailsAskUserProfilePic);

        question = (ForumQuestion) getIntent().getSerializableExtra("question");
        usersListWithName = (HashMap<String, String>) getIntent().getSerializableExtra("users");
        profilePicsRef = FirebaseStorage.getInstance().getReference("profileImages");
//        user = FirebaseAuth.getInstance().getCurrentUser();
//        uid = user.getUid();
        answersRef = FirebaseDatabase.getInstance().getReference("ForumAnswers");
//        forumRef = FirebaseDatabase.getInstance().getReference("Forum");

        //get qn author profile pic
        profilePicsRef.child(question.getQuestionAuthorID()+"_profile.jpg").getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    question.setQnProfilePic(task.getResult());
                }
                question.setQuestionAuthor(usersListWithName.get(question.getQuestionAuthor()));
            }
        });

        tVQnTitle.setText(question.getQuestionTitle());
        tVQnDesc.setText(question.getQuestionDescription());
        tVQnAuthor.setText(question.getQuestionAuthor());
        Timestamp ts = new Timestamp(question.getTimestamp());
        tVQnTimestamp.setText(String.valueOf(ts));
    }
}
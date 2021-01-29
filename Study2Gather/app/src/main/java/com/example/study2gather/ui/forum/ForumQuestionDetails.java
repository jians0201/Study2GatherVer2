package com.example.study2gather.ui.forum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
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
import com.squareup.picasso.Picasso;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;

public class ForumQuestionDetails extends AppCompatActivity {
    private TextView tVQnTitle, tVQnDesc, tVQnAuthor, tVQnTimestamp, tVAnsCount;
    private ImageView iVQnAuthorPic;

    private DatabaseReference answersRef;
//    private FirebaseUser user;
    private StorageReference profilePicsRef;

    private ForumQuestion question;
//    private ArrayList<ForumQuestionAnswer> mAns = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        setContentView(R.layout.forum_quest_details);
        tVQnTitle = findViewById(R.id.forumQuestionDetailsQuest);
        tVQnDesc = findViewById(R.id.forumQuestionDetailsDet);
        tVQnAuthor = findViewById(R.id.forumQuestionAskUser);
        tVQnTimestamp = findViewById(R.id.forumQuestionDetailsAskTime);
        tVAnsCount = findViewById(R.id.forumCommentCount);
        iVQnAuthorPic = findViewById(R.id.forumQuestionDetailsAskUserProfilePic);

        question = (ForumQuestion) getIntent().getSerializableExtra("question");
        profilePicsRef = FirebaseStorage.getInstance().getReference("profileImages");
//        user = FirebaseAuth.getInstance().getCurrentUser();
//        uid = user.getUid();
        answersRef = FirebaseDatabase.getInstance().getReference("ForumAnswers");
//        forumRef = FirebaseDatabase.getInstance().getReference("Forum");

        if (question.getQnProfilePic() != null) {
            Picasso.get().load(question.getQnProfilePic()).into(iVQnAuthorPic);
        } else {
            iVQnAuthorPic.setImageResource(R.drawable.no_image);
        }

        tVQnTitle.setText(question.getQuestionTitle());
        tVQnDesc.setText(question.getQuestionDescription());
        tVQnAuthor.setText(question.getQuestionAuthor());
        Timestamp ts = new Timestamp(question.getTimestamp());
        tVQnTimestamp.setText(String.valueOf(ts));
        tVAnsCount.setText(String.valueOf(question.getAnsCount()));
    }

    // create an action bar button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
        return super.onCreateOptionsMenu(menu);
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) { //for the tick btn
        int id = item.getItemId();
        if (item.getItemId() == android.R.id.home ) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
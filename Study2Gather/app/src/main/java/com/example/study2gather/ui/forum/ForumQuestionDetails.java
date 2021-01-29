package com.example.study2gather.ui.forum;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.study2gather.ForumAnswer;
import com.example.study2gather.ForumQuestion;
import com.example.study2gather.R;
import com.example.study2gather.ui.home.HomeCreatePost;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public class ForumQuestionDetails extends AppCompatActivity implements View.OnClickListener  {
    private TextView tVQnTitle, tVQnDesc, tVQnAuthor, tVQnTimestamp, tVAnsCount;
    private ImageView iVQnAuthorPic;
    private FloatingActionButton btnNewAns;
    private RecyclerView forumAnswerRecyclerView;

    private DatabaseReference answersRef;
    private FirebaseUser user;
    private StorageReference profilePicsRef;

    private ForumQuestion question;
    private ArrayList<ForumAnswer> mAns = new ArrayList<>();
    private String uid, username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forum_quest_details);
        tVQnTitle = findViewById(R.id.forumQuestionDetailsQuest);
        tVQnDesc = findViewById(R.id.forumQuestionDetailsDet);
        tVQnAuthor = findViewById(R.id.forumQuestionAskUser);
        tVQnTimestamp = findViewById(R.id.forumQuestionDetailsAskTime);
        tVAnsCount = findViewById(R.id.forumCommentCount);
        iVQnAuthorPic = findViewById(R.id.forumQuestionDetailsAskUserProfilePic);
        btnNewAns = findViewById(R.id.forumQuestAnsFAB);

        question = (ForumQuestion) getIntent().getSerializableExtra("question");
        profilePicsRef = FirebaseStorage.getInstance().getReference("profileImages");
        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        answersRef = FirebaseDatabase.getInstance().getReference("ForumAnswers");
//        forumRef = FirebaseDatabase.getInstance().getReference("Forum");
        username = getIntent().getStringExtra("username");

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

        //get ans and pics
//        answersRef.orderByChild("timestamp").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                mAns.clear();
//                for (DataSnapshot ds : snapshot.getChildren()) {
//                    ForumAnswer ans = ds.getValue(ForumAnswer.class);
//                    //get user profile pic
//                    profilePicsRef.child(ans.getAnswerAuthorID()+"_profile.jpg").getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
//                        @Override
//                        public void onComplete(@NonNull Task<Uri> task) {
//                            if (task.isSuccessful()) {
//                                ans.setAnswerProfilePic(task.getResult());
//                            }
//                            mAns.add(ans);
//                            //only populate questions once all questions have been retrieved
//                            if (mAns.size() == snapshot.getChildrenCount()) setUIRef();
//                        }
//                    });
//                }
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {}
//        });

        //add ans button
        btnNewAns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ForumQuestionDetails.this, ForumCreateAnswer.class);
                i.putExtra("username", username);
                i.putExtra("questionID", question.getQuestionID());
                startActivity(i);
            }
        });

        Date date = new Date();
        final String randomAnsID = "ans"+ UUID.randomUUID().toString();
        mAns.add(new ForumAnswer(username, uid, "Its a^2 + b^2 = c^2, where c is the longest side of the right angled triangle.",question.getQuestionID(),randomAnsID,date.getTime()));
        mAns.add(new ForumAnswer(username, uid, "Its a^2 + b^2 = c^2, where c is the longest side of the right angled triangle.",question.getQuestionID(),randomAnsID,date.getTime()));
        mAns.add(new ForumAnswer(username, uid, "Its a^2 + b^2 = c^2, where c is the longest side of the right angled triangle.",question.getQuestionID(),randomAnsID,date.getTime()));
        setUIRef();
    }

    private void setUIRef() {
        //Reference of RecyclerView
        forumAnswerRecyclerView = findViewById(R.id.forumAnsList);
        //Linear Layout Manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ForumQuestionDetails.this , RecyclerView.VERTICAL, false);
        //Set Layout Manager to RecyclerView
        forumAnswerRecyclerView.setLayoutManager(linearLayoutManager);
//       //reverse posts so most recent on top
//       Collections.reverse(mQns); //not working
        //Create adapter
        ForumQuestionDetalsRecyclerItemArrayAdapter myRecyclerViewAdapter = new ForumQuestionDetalsRecyclerItemArrayAdapter(mAns, new ForumQuestionDetalsRecyclerItemArrayAdapter.MyRecyclerViewItemClickListener() {
            //Handling clicks
            @Override
            public void onItemClicked(ForumAnswer ans) {}
        });
        //Set adapter to RecyclerView
        forumAnswerRecyclerView.setAdapter(myRecyclerViewAdapter);
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
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void createNewForumAnswer() {
        Date date = new Date();
        final String randomAnsID = "ans"+ UUID.randomUUID().toString();
        ForumAnswer answer = new ForumAnswer(username, uid, "Its a^2 + b^2 = c^2, where c is the longest side of the right angled triangle.",question.getQuestionID(),randomAnsID,date.getTime());
        answersRef.child(randomAnsID).setValue(answer);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.forumQuestionDetailsBackBtn:
                finish();
                break;
        }
    }
}
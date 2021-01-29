package com.example.study2gather.ui.forum;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.study2gather.R;
import com.example.study2gather.ui.home.HomeCreatePost;

public class ForumCreateQuestion extends AppCompatActivity implements View.OnClickListener {
    private ImageView iVProfilePic;
    private EditText eTQnTitle, eTQnDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forum_quest_create);
        iVProfilePic = findViewById(R.id.forumQuestCreateProfilePic);
        eTQnTitle = findViewById(R.id.forumQuestCreateTitle);
//        eTQnDesc = findViewById(R.id.);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.forumQuestCreateClearButton:
                finish();
                break;
            case R.id.forumQuestCreateButton:
//                String postText = eTPostText.getText().toString().trim();
//                if (iVPostPic.getDrawable()!=null && !postText.isEmpty()) {
//                    Toast.makeText(HomeCreatePost.this, "Creating Post", Toast.LENGTH_SHORT).show();
//                    createNewPost(postText);
//                }
                break;
        }
    }
}
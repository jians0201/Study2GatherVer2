package com.example.study2gather.ui.courses;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.study2gather.Course;
import com.example.study2gather.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.ArrayList;

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class CoursesDetails extends AppCompatActivity {
    private ImageButton btnBack;
    private ImageView iVCourseImage;
    private TextView tVCourseName, tVCourseType, tVCourseLecturesNum, tVCourseDescription;
    private RecyclerView coursesWYLRecyclerView, coursesLectureTopicRecyclerView;
    private Button btnCoursesDetailsDropDown;
    private LinearLayout lLCoursesToggleSection;

    private Course course;
    private ArrayList<String> mCoursesWYL;
    private ArrayList<CourseLectureTopic> mCoursesLectureTopics = new ArrayList<CourseLectureTopic>();
    private CoursesLectureTopicRecyclerItemArrayAdapter myRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.courses_details);
        btnBack = findViewById(R.id.coursesDetailsBackBtn);
        iVCourseImage = findViewById(R.id.coursesDetailsImg);
        tVCourseName = findViewById(R.id.coursesDetailsName);
        tVCourseType = findViewById(R.id.coursesDetailsCourseCategory);
        tVCourseLecturesNum = findViewById(R.id.coursesDetailsCourseTotalLecture);
        tVCourseDescription = findViewById(R.id.coursesDetailsDesc);
        btnCoursesDetailsDropDown = findViewById(R.id.coursesDetailsDescDropDownBtn);
        lLCoursesToggleSection = findViewById(R.id.coursesDetailsToggleSection);

        course = (Course) getIntent().getSerializableExtra("course");
        if (course.getCoursePic() != null) {
            Picasso.get().load(course.getCoursePic()).into(iVCourseImage);
        } else {
            iVCourseImage.setImageResource(R.drawable.no_image);
        }
        tVCourseName.setText(course.getCourseName());
        tVCourseDescription.setText(course.getCourseDesc());
        tVCourseType.setText(course.getCourseType());
        tVCourseLecturesNum.setText(String.valueOf(course.getCourseLecturesNum())+" Lectures");

        mCoursesWYL = course.getCourseLearnTopics();
        setUIRefWYL();

        String tempLectureLink = course.getCourseLectureLinks().get(0);
        for (String topic : course.getCourseLectureTopics()) {
            mCoursesLectureTopics.add(new CourseLectureTopic(topic,tempLectureLink));
        }
        setUIRefLectureTopic();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnCoursesDetailsDropDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lLCoursesToggleSection.getVisibility() == View.VISIBLE) {
                    btnCoursesDetailsDropDown.setText(R.string.coursesCourseDetailDescDropDownBtnOn);
                    lLCoursesToggleSection.setVisibility(View.GONE);
                } else {
                    btnCoursesDetailsDropDown.setText(R.string.coursesCourseDetailDescDropDownBtnOff);
                    lLCoursesToggleSection.setVisibility(View.VISIBLE);
                }
            }
        });
    }


    private void setUIRefWYL() {
        //Reference of RecyclerView
        coursesWYLRecyclerView = findViewById(R.id.coursesDetailsWYLDetailsList);
        //Linear Layout Manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CoursesDetails.this, RecyclerView.VERTICAL, false);
        //Set Layout Manager to RecyclerView
        coursesWYLRecyclerView.setLayoutManager(linearLayoutManager);
        //Create adapter
        CoursesWYLRecyclerItemArrayAdapter myRecyclerViewAdapter = new CoursesWYLRecyclerItemArrayAdapter(mCoursesWYL, new CoursesWYLRecyclerItemArrayAdapter.MyRecyclerViewItemClickListener()
        {
            //Handling clicks
            @Override
            public void onItemClicked(String courseWYL) {}
        });

        //Set adapter to RecyclerView
        coursesWYLRecyclerView.setAdapter(myRecyclerViewAdapter);
    }

    private void setUIRefLectureTopic() {
        //Reference of RecyclerView
        coursesLectureTopicRecyclerView = findViewById(R.id.coursesDetailsLectureList);
        //Linear Layout Manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CoursesDetails.this, RecyclerView.VERTICAL, false);
        //Set Layout Manager to RecyclerView
        coursesLectureTopicRecyclerView.setLayoutManager(linearLayoutManager);
        //Create adapter
        myRecyclerViewAdapter = new CoursesLectureTopicRecyclerItemArrayAdapter(mCoursesLectureTopics, new CoursesLectureTopicRecyclerItemArrayAdapter.MyRecyclerViewItemClickListener()
        {
            //Handling clicks
            @Override
            public void onItemClicked(CourseLectureTopic courseLectureTopic) {
                StorageReference LectureSlidesPdf = FirebaseStorage.getInstance().getReference("courses").child(courseLectureTopic.getCourseLTLink());
                LectureSlidesPdf.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            downloadFile(CoursesDetails.this, "Lecture 1", ".pdf", DIRECTORY_DOWNLOADS, task.getResult().toString());
                        }
                    }
                });
            }
        });

        //Set adapter to RecyclerView
        coursesLectureTopicRecyclerView.setAdapter(myRecyclerViewAdapter);
    }

    private void downloadFile(Context context, String fileName, String fileExten, String destinationDir, String url) {
        DownloadManager dlManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, destinationDir, fileName+fileExten);

        dlManager.enqueue(request);
    }
}

package com.example.study2gather.ui.courses;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.study2gather.Course;
import com.example.study2gather.R;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import androidx.appcompat.app.AppCompatActivity;

public class CoursesDetails extends AppCompatActivity {
    private ImageView iVCourseImage;
    private TextView tVCourseName, tVCourseType, tVCourseLecturesNum, tVCourseDescription;

    private StorageReference profilePicsRef;

    private Course course;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.courses_details);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        iVCourseImage = findViewById(R.id.coursesDetailsImg);
        tVCourseName = findViewById(R.id.coursesDetailsName);
        tVCourseType = findViewById(R.id.coursesDetailsCourseCategory);
        tVCourseLecturesNum = findViewById(R.id.coursesDetailsCourseTotalLecture);
        tVCourseDescription = findViewById(R.id.coursesDetailsDesc);

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
    }
}

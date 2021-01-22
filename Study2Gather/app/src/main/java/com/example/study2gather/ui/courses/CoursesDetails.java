package com.example.study2gather.ui.courses;

import android.os.Bundle;
import android.widget.TextView;
import com.example.study2gather.R;
import androidx.appcompat.app.AppCompatActivity;

public class CoursesDetails extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.courses_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView textView = findViewById(R.id.coursesDetailsName);
        textView.setText(getIntent().getStringExtra("param"));
    }
}

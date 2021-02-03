package com.example.study2gather.ui.courses;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.study2gather.Course;
import com.example.study2gather.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.Serializable;
import java.util.ArrayList;

public class CoursesFragment extends Fragment {

    private RecyclerView coursesSubRecyclerView;
    private View root;

    private DatabaseReference coursesRef;
    private StorageReference coursesPicRef;

    private ArrayList<Course> mCourses = new ArrayList<Course>();
    private CoursesRecyclerItemArrayAdapter myRecyclerViewAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_courses, container, false);
        coursesSubRecyclerView = root.findViewById(R.id.coursesRecyclerView);
        coursesRef = FirebaseDatabase.getInstance().getReference("Courses");
        coursesPicRef = FirebaseStorage.getInstance().getReference("courses");
        setUIRef();

        //get all courses
        coursesRef.orderByChild("courseId").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mCourses.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Course course = ds.getValue(Course.class);
                    //get course pic
                    coursesPicRef.child(course.getCoursePicPath()).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                course.setCoursePic(task.getResult());
                            }
                            mCourses.add(course);
                            if (mCourses.size() == snapshot.getChildrenCount()) myRecyclerViewAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        return root;
    }

    private void setUIRef() {
        //Linear Layout Manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        //Set Layout Manager to RecyclerView
        coursesSubRecyclerView.setLayoutManager(linearLayoutManager);
        //Create adapter
        myRecyclerViewAdapter = new CoursesRecyclerItemArrayAdapter(mCourses, new CoursesRecyclerItemArrayAdapter.MyRecyclerViewItemClickListener()
        {
            //Handling clicks
            @Override
            public void onItemClicked(Course course)
            {
                Intent i = new Intent(getActivity(), CoursesDetails.class);
                i.putExtra("course", (Serializable) course);
                startActivity(i);
            }
        });

        //Set adapter to RecyclerView
        coursesSubRecyclerView.setAdapter(myRecyclerViewAdapter);
    }

}
package com.example.study2gather.ui.courses;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.study2gather.Chat;
import com.example.study2gather.Course;
import com.example.study2gather.ForumQuestion;
import com.example.study2gather.R;
import com.example.study2gather.User;
import com.example.study2gather.ui.messages.MessagesChat;
import com.example.study2gather.ui.messages.MessagesRecyclerItemArrayAdapter;
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class CoursesFragment extends Fragment {

    private RecyclerView coursesRecRecyclerView, coursesSubRecyclerView;
//    private FloatingActionButton btnAddCourse; //remove later
    private View root;

    private DatabaseReference coursesRef, usersRef;
    private FirebaseUser user;
    private StorageReference coursesPicRef;

    private ArrayList<Course> mCourses = new ArrayList<Course>();
    private String uid;
    private User userProfile;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_courses, container, false);
//        btnAddCourse = root.findViewById(R.id.coursesCreateFAB); //remove later
        coursesRef = FirebaseDatabase.getInstance().getReference("Courses");
        usersRef = FirebaseDatabase.getInstance().getReference("Users");
        coursesPicRef = FirebaseStorage.getInstance().getReference("courses");
        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        //get own info
        usersRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) { userProfile = snapshot.getValue(User.class); }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { Toast.makeText(getContext(),"Something Went Wrong",Toast.LENGTH_LONG).show(); }
        });

        //get all courses
        coursesRef.orderByChild("timestamp").addValueEventListener(new ValueEventListener() {
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
                            if (mCourses.size() == snapshot.getChildrenCount()) setUIRef();
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
        //Reference of RecyclerView
        coursesSubRecyclerView = root.findViewById(R.id.coursesRecyclerView);
        //Linear Layout Manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        //Set Layout Manager to RecyclerView
        coursesSubRecyclerView.setLayoutManager(linearLayoutManager);
        //Create adapter
        CoursesRecyclerItemArrayAdapter myRecyclerViewAdapter = new CoursesRecyclerItemArrayAdapter(mCourses, new CoursesRecyclerItemArrayAdapter.MyRecyclerViewItemClickListener()
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
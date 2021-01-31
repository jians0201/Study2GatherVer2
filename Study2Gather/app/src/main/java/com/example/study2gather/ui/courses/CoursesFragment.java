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

        //for testing
//        final String randomCourseId = "course"+ UUID.randomUUID().toString();
//        String courseDesc = "Learn Python Programming the Easy Way!<br /><br />If you want to learn how to write Python programs that can be used on Linux, Mac, and Unix operating systems, code python like a pro, solve real-world problems, or automate repetitive and complex tasks, click the subscribe button!";
//        ArrayList<String> courseLearnTopics = new ArrayList<String>();
//        courseLearnTopics.add("How to prepare your computer for programming in Python.");
//        courseLearnTopics.add("Suggested text editors and integrated development environments to use when coding in Python.");
//        courseLearnTopics.add("How to work with various data types including strings, lists, tuples, dictionaries, booleans, and more.");
//        courseLearnTopics.add("How to define and use functions.");
//        courseLearnTopics.add("Important built-in Python functions that you will use often.");
//        courseLearnTopics.add("What modules are, when you should use them, and how to create your own.");
//        courseLearnTopics.add("How to organize your Python programs - Learn what goes where.");
//        ArrayList<String> courseLectureTopics = new ArrayList<String>();
//        courseLectureTopics.add("Lecture 1 - What is computation?");
//        courseLectureTopics.add("Lecture 2 - What is computation?");
//        courseLectureTopics.add("Lecture 3 - What is computation?");
//        courseLectureTopics.add("Lecture 4 - What is computation?");
//        courseLectureTopics.add("Lecture 5 - What is computation?");
//        courseLectureTopics.add("Lecture 6 - What is computation?");
//        courseLectureTopics.add("Lecture 7 - What is computation?");
//        courseLectureTopics.add("Lecture 8 - What is computation?");
//        ArrayList<String> courseLectureLinks = new ArrayList<String>();
//        courseLectureLinks.add("MIT6_0001F16_Lect1.pdf");
//        Course course = new Course(randomCourseId, "Introduction to Python Programming","Programming Language",courseDesc,8,courseLearnTopics,courseLectureTopics,courseLectureLinks);
//        course.setCoursePicPath("1_RJMxLdTHqVBSijKmOO5MAg.jpeg");
//        mCourses.add(course);
//        mCourses.add(course);
//        mCourses.add(course);
//        mCourses.add(course);
//        setUIRef();
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

//    private void setUIRefRec() {
//        //Reference of RecyclerView
//        coursesRecRecyclerView = root.findViewById(R.id.courses_rec_list);
//        //Linear Layout Manager
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
//        //Set Layout Manager to RecyclerView
//        coursesRecRecyclerView.setLayoutManager(linearLayoutManager);
//        //Create adapter
////        MessagesRecyclerItemArrayAdapter myRecyclerViewAdapter = new MessagesRecyclerItemArrayAdapter(mChats, new MessagesRecyclerItemArrayAdapter.MyRecyclerViewItemClickListener()
////        {
////            //Handling clicks
////            @Override
////            public void onItemClicked(Chat chat)
////            {
//////                Intent i = new Intent(getActivity(), MessagesChat.class);
//////                startActivity(i);
////            }
////        });
////
////        //Set adapter to RecyclerView
////        coursesRecRecyclerView.setAdapter(myRecyclerViewAdapter);
//    }


}
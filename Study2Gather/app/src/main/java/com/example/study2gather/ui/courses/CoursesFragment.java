package com.example.study2gather.ui.courses;

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
import androidx.viewpager.widget.ViewPager;

import com.example.study2gather.R;
import com.example.study2gather.ui.courses.CoursesViewModel;
import com.example.study2gather.ui.messages.MessagesRecyclerItem;
import com.example.study2gather.ui.messages.MessagesRecyclerItemArrayAdapter;

import java.util.ArrayList;
import java.util.List;

public class CoursesFragment extends Fragment {

    private CoursesViewModel coursesViewModel;
    private RecyclerView coursesRecRecyclerView;
//    private ArrayList<CoursesViewModel> mCourses;
    private View root;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        coursesViewModel = new ViewModelProvider(this).get(CoursesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_courses, container, false);



//        bindCoursesData();
//        setUIRef();

        return root;
    }

//    private void setUIRef() {
//        //Reference of RecyclerView
//        coursesRecRecyclerView = root.findViewById(R.id.courses_rec_list);
//        //Linear Layout Manager
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
//        //Set Layout Manager to RecyclerView
//        coursesRecRecyclerView.setLayoutManager(linearLayoutManager);
//
//        //Create adapter
//        CoursesRecyclerItemArrayAdapter myRecyclerViewAdapter = new CoursesRecyclerItemArrayAdapter(mCourses, new CoursesRecyclerItemArrayAdapter.MyRecyclerViewItemClickListener() {
//            @Override
//            public void onItemClicked(CoursesViewModel courses) {
//
//            }
//        });
//
//        //Set adapter to RecyclerView
//        coursesRecRecyclerView.setAdapter(myRecyclerViewAdapter);
//    }

//    private void bindCoursesData()
//    {
//        mCourses.add(new CoursesViewModel(R.drawable.tanjiro, "Brochure", "Brochure is an informative paper document (often also used for advertising) that can be folded into a template"));
//        mCourses.add(new CoursesViewModel(R.drawable.tanjiro, "Sticker", "Sticker is a type of label: a piece of printed paper, plastic, vinyl, or other material with pressure sensitive adhesive on one side"));
//        mCourses.add(new CoursesViewModel(R.drawable.tanjiro, "Poster", "Poster is any piece of printed paper designed to be attached to a wall or vertical surface."));
//        mCourses.add(new CoursesViewModel(R.drawable.tanjiro, "Namecard", "Business cards are cards bearing business information about a company or individual."));
//    }
}
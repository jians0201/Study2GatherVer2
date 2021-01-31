package com.example.study2gather.ui.courses;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.study2gather.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class CoursesLectureTopicRecyclerItemArrayAdapter extends RecyclerView.Adapter<CoursesLectureTopicRecyclerItemArrayAdapter.MyViewHolder> {

    private ArrayList<CourseLectureTopic> mCourseLectureTopics;
    private CoursesLectureTopicRecyclerItemArrayAdapter.MyRecyclerViewItemClickListener mItemClickListener;

    public CoursesLectureTopicRecyclerItemArrayAdapter(ArrayList<CourseLectureTopic> courseLectureTopic, CoursesLectureTopicRecyclerItemArrayAdapter.MyRecyclerViewItemClickListener itemClickListener) {
        this.mCourseLectureTopics = courseLectureTopic;
        this.mItemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public CoursesLectureTopicRecyclerItemArrayAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inflate RecyclerView row
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.courses_details_lecture_list, parent, false);

        //Create View Holder
        final CoursesLectureTopicRecyclerItemArrayAdapter.MyViewHolder myViewHolder = new CoursesLectureTopicRecyclerItemArrayAdapter.MyViewHolder(view);

        //Item Clicks
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClickListener.onItemClicked(mCourseLectureTopics.get(myViewHolder.getLayoutPosition()));
            }
        });

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CoursesLectureTopicRecyclerItemArrayAdapter.MyViewHolder holder, int position) {
        //Set Course Lecture Topic
        holder.courseLectureTopic.setText("Lecture "+String.valueOf(position+1)+" - "+mCourseLectureTopics.get(position).getCourseLTtitle());

        //Set onClickListener for Download Button
//        holder.coursesLectureTopicDownloadLink.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
    }



    @Override
    public int getItemCount() {
        return mCourseLectureTopics.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //RecyclerView View Holder
    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView courseLectureTopic;
        private ImageButton coursesLectureTopicDownloadLink;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            courseLectureTopic = itemView.findViewById(R.id.coursesDetailsLecture); //change ID
            coursesLectureTopicDownloadLink = itemView.findViewById(R.id.coursesDetailsDownloadButton);
        }
    }

    //RecyclerView Click Listener
    public interface MyRecyclerViewItemClickListener {
        void onItemClicked(CourseLectureTopic courseLectureTopic);
    }
}
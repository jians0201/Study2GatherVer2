package com.example.study2gather.ui.courses;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.study2gather.Course;
import com.example.study2gather.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CoursesRecyclerItemArrayAdapter extends RecyclerView.Adapter<CoursesRecyclerItemArrayAdapter.MyViewHolder> {

    private ArrayList<Course> mCourses;
    private MyRecyclerViewItemClickListener mItemClickListener;

    public CoursesRecyclerItemArrayAdapter(ArrayList<Course> courses, MyRecyclerViewItemClickListener itemClickListener) {
        this.mCourses = courses;
        this.mItemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inflate RecyclerView row
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.courses_rec_list, parent, false);

        //Create View Holder
        final MyViewHolder myViewHolder = new MyViewHolder(view);

        //Item Clicks
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClickListener.onItemClicked(mCourses.get(myViewHolder.getLayoutPosition()));
            }
        });

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //Set Course Image
        if (mCourses.get(position).getCoursePic() != null) {
            Picasso.get().load(mCourses.get(position).getCoursePic()).into(holder.courseImage);
        } else {
            holder.courseImage.setImageResource(R.drawable.no_image);
        }

        //Set Course Name
        holder.courseName.setText(mCourses.get(position).getCourseName());

        //Set Course Type
        holder.courseType.setText(mCourses.get(position).getCourseType());

        //Set Course Lectures Num
        holder.courseLecturesNum.setText(String.valueOf(mCourses.get(position).getCourseLecturesNum()+" Lectures"));
    }

    @Override
    public int getItemCount() {
        return mCourses.size();
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
        private ImageView courseImage;
        private TextView courseName, courseType, courseLecturesNum;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            courseImage = itemView.findViewById(R.id.coursesRecImg);
            courseName = itemView.findViewById(R.id.coursesRecTitle);
            courseType = itemView.findViewById(R.id.coursesRecCourseCategory);
            courseLecturesNum = itemView.findViewById(R.id.coursesRecCourseTotalLecture);
        }
    }

    //RecyclerView Click Listener
    public interface MyRecyclerViewItemClickListener {
        void onItemClicked(Course course);
    }
}



package com.example.study2gather.ui.courses;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.study2gather.Course;
import com.example.study2gather.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CoursesWYLRecyclerItemArrayAdapter extends RecyclerView.Adapter<CoursesWYLRecyclerItemArrayAdapter.MyViewHolder> {

    private ArrayList<String> mCourseWYL;
    private CoursesWYLRecyclerItemArrayAdapter.MyRecyclerViewItemClickListener mItemClickListener;

    public CoursesWYLRecyclerItemArrayAdapter(ArrayList<String> coursesWYL, CoursesWYLRecyclerItemArrayAdapter.MyRecyclerViewItemClickListener itemClickListener) {
        this.mCourseWYL = coursesWYL;
        this.mItemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public CoursesWYLRecyclerItemArrayAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inflate RecyclerView row
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.courses_details_wyl_list, parent, false);

        //Create View Holder
        final CoursesWYLRecyclerItemArrayAdapter.MyViewHolder myViewHolder = new CoursesWYLRecyclerItemArrayAdapter.MyViewHolder(view);

        //Item Clicks
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClickListener.onItemClicked(mCourseWYL.get(myViewHolder.getLayoutPosition()));
            }
        });

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CoursesWYLRecyclerItemArrayAdapter.MyViewHolder holder, int position) {
        //Set Course WYL
        holder.courseWYLTopic.setText(mCourseWYL.get(position));
    }

    @Override
    public int getItemCount() {
        return mCourseWYL.size();
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
        private TextView courseWYLTopic;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            courseWYLTopic = itemView.findViewById(R.id.coursesWYLTextView);
        }
    }

    //RecyclerView Click Listener
    public interface MyRecyclerViewItemClickListener {
        void onItemClicked(String courseWYL);
    }
}
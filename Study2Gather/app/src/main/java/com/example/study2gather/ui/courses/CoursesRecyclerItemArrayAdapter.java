package com.example.study2gather.ui.courses;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.study2gather.R;

import java.util.ArrayList;

public class CoursesRecyclerItemArrayAdapter extends RecyclerView.Adapter<CoursesRecyclerItemArrayAdapter.MyViewHolder> {

    private ArrayList<CoursesViewModel> mCourses;
    private MyRecyclerViewItemClickListener mItemClickListener;

    public CoursesRecyclerItemArrayAdapter(ArrayList<CoursesViewModel> courses, MyRecyclerViewItemClickListener itemClickListener) {
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
        holder.coursesRecImg.setImageResource(R.drawable.tanjiro);

        //Set Country Name
        holder.coursesRecTitle.setText(mCourses.get(position).getTitle());

        //Set Currency
        String desc = "Currency: " + mCourses.get(position).getDesc();
        holder.coursesRecDesc.setText(desc);

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
        private ImageView coursesRecImg;
        private TextView coursesRecTitle;
        private TextView coursesRecDesc;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            coursesRecImg = itemView.findViewById(R.id.coursesRecImg);
            coursesRecTitle = itemView.findViewById(R.id.coursesRecTitle);
            coursesRecDesc = itemView.findViewById(R.id.coursesRecCourseCategory);
        }
    }

    //RecyclerView Click Listener
    public interface MyRecyclerViewItemClickListener {
        void onItemClicked(CoursesViewModel courses);
    }
}



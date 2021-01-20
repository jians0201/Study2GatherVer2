package com.example.study2gather.ui.home;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.study2gather.Post;
import com.example.study2gather.R;
import com.squareup.picasso.Picasso;

import java.sql.Timestamp;
import java.util.ArrayList;

public class HomeRecylerItemArrayAdapter extends RecyclerView.Adapter<HomeRecylerItemArrayAdapter.MyViewHolder> {

    private ArrayList<Post> mPosts;
    private MyRecyclerViewItemClickListener mItemClickListener;

    public HomeRecylerItemArrayAdapter(ArrayList<Post> posts, MyRecyclerViewItemClickListener itemClickListener) {
        this.mPosts = posts;
        this.mItemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inflate RecyclerView row
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_post_list, parent, false);

        //Create View Holder
        final MyViewHolder myViewHolder = new MyViewHolder(view);

        //Item Clicks
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClickListener.onItemClicked(mPosts.get(myViewHolder.getLayoutPosition()));
            }
        });

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        //Set Post User Profile Picture
//        holder.imageViewPostUserPic.setImageResource(mPosts.get(position).getPostUserPic());
        if (mPosts.get(position).getPostProfilePic() != null) {
            Picasso.get().load(mPosts.get(position).getPostProfilePic()).into(holder.imageViewPostUserPic);
        } else {
            holder.imageViewPostPic.setImageResource(R.drawable.no_image);
        }

        //Set Post Username
        holder.textViewPostUsername.setText(mPosts.get(position).getPostAuthor());

        //Set Post Time
//        String time = mPosts.get(position).getPostTime() + " hours ago";
        Timestamp ts = new Timestamp(mPosts.get(position).getTimestamp());
//        holder.textViewPostTime.setText(time);
        holder.textViewPostTime.setText(String.valueOf(ts));

        //Set Post Caption
        holder.textViewPostCaption.setText(mPosts.get(position).getPostCaption());

        //Set Post Picture
//        holder.imageViewPostPic.setImageResource(mPosts.get(position).getPostPic());
        if (mPosts.get(position).getPostPic() != null) {
            Picasso.get().load(mPosts.get(position).getPostPic()).into(holder.imageViewPostPic);
        } else {
            holder.imageViewPostPic.setImageResource(R.drawable.no_image);
        }


        //Set Post Like Count
        String likeCount = "";
        long postLikeCount =  mPosts.get(position).getPostLikeCount();
        if (postLikeCount < 1000) {
            likeCount = String.valueOf(postLikeCount);
        } else if (postLikeCount < 1000000) {
            likeCount = String.format("%.1f", Float.valueOf(postLikeCount/1000))+"K";
        } else if (postLikeCount < 1000000000) {
            likeCount = String.format("%.1f", Float.valueOf(postLikeCount/1000000))+"M";
        } else {
            likeCount = String.format("%.1f", Float.valueOf(postLikeCount/1000000000))+"B";
        }
            holder.textViewPostLikeCount.setText(likeCount);
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
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
        private ImageView imageViewPostUserPic;
        private TextView textViewPostUsername;
        private TextView textViewPostTime;
        private TextView textViewPostCaption;
        private ImageView imageViewPostPic;
        private TextView textViewPostLikeCount;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewPostUserPic = itemView.findViewById(R.id.homePostProfilePic);
            textViewPostUsername = itemView.findViewById(R.id.homePostUsername);
            textViewPostTime = itemView.findViewById(R.id.homePostTime);
            textViewPostCaption = itemView.findViewById(R.id.homePostCaption);
            imageViewPostPic = itemView.findViewById(R.id.homePostPic);
            textViewPostLikeCount = itemView.findViewById(R.id.homePostLikeCount);
        }
    }

    //RecyclerView Click Listener
    public interface MyRecyclerViewItemClickListener {
        void onItemClicked(Post post);
    }
}

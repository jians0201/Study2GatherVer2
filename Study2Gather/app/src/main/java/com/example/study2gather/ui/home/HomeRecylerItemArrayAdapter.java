package com.example.study2gather.ui.home;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.study2gather.Post;
import com.example.study2gather.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.sql.Timestamp;
import java.util.ArrayList;

public class HomeRecylerItemArrayAdapter extends RecyclerView.Adapter<HomeRecylerItemArrayAdapter.MyViewHolder> {

    private ArrayList<Post> mPosts;
    private MyRecyclerViewItemClickListener mItemClickListener;
    private String likeCountStr, uid;

    public HomeRecylerItemArrayAdapter(String uid, ArrayList<Post> posts, MyRecyclerViewItemClickListener itemClickListener) {
        this.mPosts = posts;
        this.mItemClickListener = itemClickListener;
        this.uid = uid;
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
        likeCountStr = getLikeCountString(mPosts.get(position).getPostLikeCount());
        if(Integer.parseInt(likeCountStr)>1) {
            likeCountStr += " likes";
            holder.textViewPostLikeCount.setText(likeCountStr);
        }else if(Integer.parseInt(likeCountStr)==1) {
            likeCountStr += " like";
            holder.textViewPostLikeCount.setText(likeCountStr);
        }

        holder.toggleButtonLikeBtn.setChecked(mPosts.get(position).isLiked());

        holder.toggleButtonLikeBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                DatabaseReference postsRef = FirebaseDatabase.getInstance().getReference("Posts");
                DatabaseReference likesRef = FirebaseDatabase.getInstance().getReference("Likes");
                Post post = mPosts.get(position);
                if (isChecked) {
                    post.likePost();
                    likesRef.child(post.getPostID()).child(uid).setValue(true);
                } else {
                    post.unlikePost();
                    likesRef.child(post.getPostID()).child(uid).removeValue();
                }
                post.setLiked(isChecked);
//                holder.toggleButtonLikeBtn.setChecked(isChecked);
                postsRef.child(post.getPostID()).child("postLikeCount").setValue(post.getPostLikeCount());
                likeCountStr = getLikeCountString(post.getPostLikeCount());
                if(Integer.parseInt(likeCountStr)>1) {
                    likeCountStr += " likes";
                    holder.textViewPostLikeCount.setText(likeCountStr);
                }else if(Integer.parseInt(likeCountStr)==1) {
                    likeCountStr += " like";
                    holder.textViewPostLikeCount.setText(likeCountStr);
                }else if(Integer.parseInt(likeCountStr)==0) {
                    holder.textViewPostLikeCount.setText("");
                }
            }
        });
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
        private ImageView imageViewPostUserPic, imageViewPostPic;
        private TextView textViewPostUsername, textViewPostTime, textViewPostCaption, textViewPostLikeCount;
        private ToggleButton toggleButtonLikeBtn;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewPostUserPic = itemView.findViewById(R.id.homePostProfilePic);
            textViewPostUsername = itemView.findViewById(R.id.homePostUsername);
            textViewPostTime = itemView.findViewById(R.id.homePostTime);
            textViewPostCaption = itemView.findViewById(R.id.homePostCaption);
            imageViewPostPic = itemView.findViewById(R.id.homePostPic);
            textViewPostLikeCount = itemView.findViewById(R.id.homePostLikeCount);
            toggleButtonLikeBtn = itemView.findViewById(R.id.homePostLikeButton);
        }
    }

    //RecyclerView Click Listener
    public interface MyRecyclerViewItemClickListener {
        void onItemClicked(Post post);
    }

    public String getLikeCountString(long likeCount) {
        String likeCountStr = "";
        if (likeCount < 1000) {
            likeCountStr = String.valueOf(likeCount);
        } else if (likeCount < 1000000) {
            likeCountStr = String.format("%.1f", Float.valueOf(likeCount/1000))+"K";
        } else if (likeCount < 1000000000) {
            likeCountStr = String.format("%.1f", Float.valueOf(likeCount/1000000))+"M";
        } else {
            likeCountStr = String.format("%.1f", Float.valueOf(likeCount/1000000000))+"B";
        }
        return likeCountStr;
    }
}

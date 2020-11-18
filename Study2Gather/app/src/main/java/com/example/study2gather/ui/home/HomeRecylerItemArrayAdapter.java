package com.example.study2gather.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.study2gather.R;

import java.util.ArrayList;

public class HomeRecylerItemArrayAdapter extends RecyclerView.Adapter<HomeRecylerItemArrayAdapter.MyViewHolder> {

    private ArrayList<HomeRecyclerItem> mPosts;
    private MyRecyclerViewItemClickListener mItemClickListener;

    public HomeRecylerItemArrayAdapter(ArrayList<HomeRecyclerItem> posts, MyRecyclerViewItemClickListener itemClickListener) {
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
        //Set Country Flag
        holder.imageViewPostUserPic.setImageResource(mPosts.get(position).getPostUserPic());

        //Set Country Name
        holder.textViewPostUsername.setText(mPosts.get(position).getPostUsername());

        //Set Currency
        holder.textViewPostTime.setText(mPosts.get(position).getPostTime());

        holder.textViewPostCaption.setText(mPosts.get(position).getPostCaption());

        holder.textViewPostPic.setImageResource(mPosts.get(position).getPostPic());
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
        private ImageView textViewPostPic;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewPostUserPic = itemView.findViewById(R.id.postProfilePic);
            textViewPostUsername = itemView.findViewById(R.id.homePostUsername);
            textViewPostTime = itemView.findViewById(R.id.homePostTime);
            textViewPostCaption = itemView.findViewById(R.id.homePostCaption);
            textViewPostPic = itemView.findViewById(R.id.homePostPic);
        }
    }

    //RecyclerView Click Listener
    public interface MyRecyclerViewItemClickListener {
        void onItemClicked(HomeRecyclerItem post);
    }
}

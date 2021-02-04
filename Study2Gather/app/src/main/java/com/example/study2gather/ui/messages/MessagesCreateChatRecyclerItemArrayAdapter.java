package com.example.study2gather.ui.messages;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.study2gather.R;
import com.example.study2gather.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MessagesCreateChatRecyclerItemArrayAdapter extends RecyclerView.Adapter<MessagesCreateChatRecyclerItemArrayAdapter.MyViewHolder> {

    private ArrayList<User> mUsers;
    private MessagesCreateChatRecyclerItemArrayAdapter.MyRecyclerViewItemClickListener mItemClickListener;

    public MessagesCreateChatRecyclerItemArrayAdapter(ArrayList<User> users, MessagesCreateChatRecyclerItemArrayAdapter.MyRecyclerViewItemClickListener itemClickListener) {
        this.mUsers = users;
        this.mItemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public MessagesCreateChatRecyclerItemArrayAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inflate RecyclerView row
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.messages_list, parent, false);

        //Create View Holder
        final MessagesCreateChatRecyclerItemArrayAdapter.MyViewHolder myViewHolder = new MessagesCreateChatRecyclerItemArrayAdapter.MyViewHolder(view);

        //Item Clicks
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClickListener.onItemClicked(mUsers.get(myViewHolder.getLayoutPosition()));
            }
        });

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MessagesCreateChatRecyclerItemArrayAdapter.MyViewHolder holder, int position) {
        //Set Username
        holder.chatTarget.setText(mUsers.get(position).getUsername());

        //Set User Profile Pic
        if (mUsers.get(position).getProfilePic() != null) {
            Picasso.get().load(mUsers.get(position).getProfilePic()).into(holder.profilePic);
        } else {
            holder.profilePic.setImageResource(R.drawable.ic_profile_user_24dp);
        }
//        Picasso.get().load(mUsers.get(position).getProfilePic()).into(holder.profilePic);
    }

    @Override
    public int getItemCount()
    {
        return mUsers.size();
    }

    @Override
    public int getItemViewType(int position)
    {
        return position;
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    //RecyclerView View Holder
    class MyViewHolder extends RecyclerView.ViewHolder
    {
        private TextView chatTarget;
        private ImageView profilePic;

        MyViewHolder(@NonNull View itemView)
        {
            super(itemView);
            chatTarget = itemView.findViewById(R.id.messagesUser);
            profilePic = itemView.findViewById(R.id.messagesUserProfilePic);
        }
    }

    //RecyclerView Click Listener
    public interface MyRecyclerViewItemClickListener    {
        void onItemClicked(User user);
    }
}

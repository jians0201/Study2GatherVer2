package com.example.study2gather.ui.messages;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.study2gather.Chat;
import com.example.study2gather.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MessagesRecyclerItemArrayAdapter extends RecyclerView.Adapter<MessagesRecyclerItemArrayAdapter.MyViewHolder> {

    private ArrayList<Chat> mChats;
    private MyRecyclerViewItemClickListener mItemClickListener;

    public MessagesRecyclerItemArrayAdapter(ArrayList<Chat> chats, MyRecyclerViewItemClickListener itemClickListener) {
        this.mChats = chats;
        this.mItemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inflate RecyclerView row
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.messages_list, parent, false);

        //Create View Holder
        final MyViewHolder myViewHolder = new MyViewHolder(view);

        //Item Clicks
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClickListener.onItemClicked(mChats.get(myViewHolder.getLayoutPosition()));
            }
        });

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //Set Chat Title
        holder.chatTarget.setText(mChats.get(position).getChatTitle());

        //Set Latest Message
        holder.latestMessage.setText(mChats.get(position).getLastMsg());

        //Set Chat Pic
        if (mChats.get(position).getChatPic() != null) {
            Picasso.get().load(mChats.get(position).getChatPic()).into(holder.profilePic);
        } else {
            holder.profilePic.setImageResource(R.drawable.ic_profile_user_24dp);
        }
    }

    @Override
    public int getItemCount()
    {
        return mChats.size();
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
        private TextView chatTarget, latestMessage;
        private ImageView profilePic;

        MyViewHolder(@NonNull View itemView)
        {
            super(itemView);
            chatTarget = itemView.findViewById(R.id.messagesUser);
            latestMessage = itemView.findViewById(R.id.messagesLatest);
            profilePic = itemView.findViewById(R.id.messagesUserProfilePic);
        }
    }

    //RecyclerView Click Listener
    public interface MyRecyclerViewItemClickListener    {
        void onItemClicked(Chat message);
    }

}

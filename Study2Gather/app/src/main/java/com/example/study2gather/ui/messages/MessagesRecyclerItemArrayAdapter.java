package com.example.study2gather.ui.messages;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.study2gather.R;

import java.util.ArrayList;

public class MessagesRecyclerItemArrayAdapter extends RecyclerView.Adapter<MessagesRecyclerItemArrayAdapter.MyViewHolder> {

    private ArrayList<MessagesRecyclerItem> mChats;
    private MyRecyclerViewItemClickListener mItemClickListener;

    public MessagesRecyclerItemArrayAdapter(ArrayList<MessagesRecyclerItem> countries, MyRecyclerViewItemClickListener itemClickListener) {
        this.mChats = countries;
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
        holder.messagesSender.setText(mChats.get(position).getChatTitle());

        //Set Latest Message
        holder.messagesBrief.setText("");

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
        private TextView messagesSender, messagesBrief;
        private ImageView profilePic;

        MyViewHolder(@NonNull View itemView)
        {
            super(itemView);
            messagesSender = itemView.findViewById(R.id.messagesSender);
            messagesBrief = itemView.findViewById(R.id.messagesBrief);
            profilePic = itemView.findViewById(R.id.profilePic);
        }
    }

    //RecyclerView Click Listener
    public interface MyRecyclerViewItemClickListener    {
        void onItemClicked(MessagesRecyclerItem message);
    }

}

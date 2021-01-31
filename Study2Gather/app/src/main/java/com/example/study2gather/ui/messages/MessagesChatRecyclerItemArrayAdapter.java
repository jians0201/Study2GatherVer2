package com.example.study2gather.ui.messages;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.study2gather.Message;
import com.example.study2gather.R;

import java.sql.Timestamp;
import java.util.ArrayList;

public class MessagesChatRecyclerItemArrayAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Message> mMsgs;
    private MessagesChatRecyclerItemArrayAdapter.MyRecyclerViewItemClickListener mItemClickListener;
    private static int TYPE_SENTMESSAGE = 1;
    private static int TYPE_RECIEVEDMESSAGE = 2;
    private String uid;

    public MessagesChatRecyclerItemArrayAdapter(String uid, ArrayList<Message> messages, MessagesChatRecyclerItemArrayAdapter.MyRecyclerViewItemClickListener itemClickListener) {
        this.mMsgs = messages;
        this.mItemClickListener = itemClickListener;
        this.uid = uid; //my uid parsed in from activity
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inflate RecyclerView row
        View view;
        if (viewType == TYPE_SENTMESSAGE) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.messages_send, parent, false);
            return new SentMessageViewHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.messages_receive, parent, false);
            return new RecievedMessageViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_SENTMESSAGE) {
            ((SentMessageViewHolder) holder).setMessageDetails(mMsgs.get(position));
        } else if (getItemViewType(position) == TYPE_RECIEVEDMESSAGE) {
            ((RecievedMessageViewHolder) holder).setMessageDetails(mMsgs.get(position));
        }
    }

    @Override
    public int getItemCount()
    {
        return mMsgs.size();
    }

    @Override
    public int getItemViewType(int position)
    {
        if (mMsgs.get(position).getMsgSender().equals(uid)) {//if sender is == to my uid use msg_send else use msg_receive
            return TYPE_SENTMESSAGE;
        } else {
            return TYPE_RECIEVEDMESSAGE;
        }
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    //Custom View Holders
    class SentMessageViewHolder extends RecyclerView.ViewHolder {
        private TextView sentMsgContent, sentMsgTimestamp;

        public SentMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            sentMsgContent = itemView.findViewById(R.id.messageSentContent);
            sentMsgTimestamp = itemView.findViewById(R.id.messageTimeSent);
        }

        public void setMessageDetails(Message message) {
            sentMsgContent.setText(message.getMsgContent());
            Timestamp ts = new Timestamp(message.getTimestamp());
            sentMsgTimestamp.setText(String.valueOf(ts));
        }
    }
    class RecievedMessageViewHolder extends RecyclerView.ViewHolder {
        private TextView recievedMsgContent, recievedMsgTimestamp;

        public RecievedMessageViewHolder(@NonNull View itemView) {
            super(itemView);
            recievedMsgContent = itemView.findViewById(R.id.messageReceivedContent);
            recievedMsgTimestamp = itemView.findViewById(R.id.messageTimeReceived);
        }

        public void setMessageDetails(Message message) {
            recievedMsgContent.setText(message.getMsgContent());
            Timestamp ts = new Timestamp(message.getTimestamp());
            recievedMsgTimestamp.setText(String.valueOf(ts));
        }
    }

    //RecyclerView Click Listener
    public interface MyRecyclerViewItemClickListener    {
        void onItemClicked(Message message);
    }
}

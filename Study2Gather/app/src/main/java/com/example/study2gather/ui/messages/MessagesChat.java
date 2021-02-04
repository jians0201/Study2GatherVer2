package com.example.study2gather.ui.messages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.study2gather.Chat;
import com.example.study2gather.Message;
import com.example.study2gather.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class MessagesChat extends AppCompatActivity implements View.OnClickListener {
    private TextView tVChatTitle;
    private ImageView iVChatProfilePic;
    private RecyclerView messagesChatRecyclerView;
    private EditText eTMessageContent;
    private LinearLayoutManager mLayoutManager;
    private RecyclerView messagesMessageList;

    private DatabaseReference messagesRef, chatRef;
    private FirebaseUser user;

    private Chat chat;
    private String uid;
    private ArrayList<Message> mMessages = new ArrayList<>();
    private MessagesChatRecyclerItemArrayAdapter myRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messages_chat);
        tVChatTitle = findViewById(R.id.messagesChatUser);
        iVChatProfilePic = findViewById(R.id.messagesChatProfilePic);
        eTMessageContent = findViewById(R.id.messageChatMessageContent);
        messagesMessageList = findViewById(R.id.messagesMessageList);
        messagesChatRecyclerView = findViewById(R.id.messagesMessageList);

        chat = (Chat) getIntent().getSerializableExtra("chat");
        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        messagesRef = FirebaseDatabase.getInstance().getReference("Messages").child(chat.getChatId());
        chatRef = FirebaseDatabase.getInstance().getReference("Chats").child(chat.getChatId());
        setUIRef();

        tVChatTitle.setText(chat.getChatTitle());
        if (chat.getChatPic() != null) {
            Picasso.get().load(chat.getChatPic()).into(iVChatProfilePic);
        } else {
            iVChatProfilePic.setImageResource(R.drawable.no_image);
        }

        //get messages
        messagesRef.orderByChild("timestamp").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mMessages.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Message msg = ds.getValue(Message.class);
                    mMessages.add(msg);
                    //only populate questions once all questions have been retrieved
                    if (mMessages.size() == snapshot.getChildrenCount()) myRecyclerViewAdapter.notifyDataSetChanged();
                    mLayoutManager = new LinearLayoutManager(getParent());
                    mLayoutManager.setStackFromEnd(true);
                    messagesMessageList.setLayoutManager(mLayoutManager);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        eTMessageContent.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    eTMessageContent.setHint(R.string.messageChatMessageContentHintOnFocus);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(eTMessageContent, InputMethodManager.SHOW_IMPLICIT);
                } else {
                    eTMessageContent.setHint(R.string.messageChatMessageContentHint);
                }
            }
        });
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.messagesChatBackButton:
                finish();
                break;
            case R.id.messageChatSendBtn:
                String msgContent = eTMessageContent.getText().toString().trim();
                if (!msgContent.isEmpty()) {
                    createMessage(msgContent);
                    eTMessageContent.setText("");
                }
                break;
        }
    }

    private void createMessage(String msgContent) {
        final String randomMsgId = "msg"+ UUID.randomUUID().toString();
        Date date = new Date();
        Message msg = new Message(uid, msgContent, randomMsgId, chat.getChatId(), date.getTime());
        messagesRef.child(randomMsgId).setValue(msg);
        chatRef.child("lastMsg").setValue(msgContent);
    }

    private void setUIRef() {
        //Linear Layout Manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MessagesChat.this , RecyclerView.VERTICAL, false);
        //Set Layout Manager to RecyclerView
        messagesChatRecyclerView.setLayoutManager(linearLayoutManager);
//       //reverse posts so most recent on top
//       Collections.reverse(mQns); //not working
        //Create adapter
        myRecyclerViewAdapter = new MessagesChatRecyclerItemArrayAdapter(uid, mMessages, new MessagesChatRecyclerItemArrayAdapter.MyRecyclerViewItemClickListener() {
            //Handling clicks
            @Override
            public void onItemClicked(Message message) {}
        });
        //Set adapter to RecyclerView
        messagesChatRecyclerView.setAdapter(myRecyclerViewAdapter);
    }
}
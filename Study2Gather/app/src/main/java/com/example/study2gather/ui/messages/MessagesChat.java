package com.example.study2gather.ui.messages;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
    private LinearLayout messageChatTopLayout;

    private DatabaseReference messagesRef, chatRef;
    private FirebaseUser user;

    private Chat chat;
    private String uid;
    private ArrayList<Message> mMessages = new ArrayList<>();
    private MessagesChatRecyclerItemArrayAdapter myRecyclerViewAdapter;
    private Activity homeActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messages_chat);
        tVChatTitle = findViewById(R.id.messagesChatUser);
        iVChatProfilePic = findViewById(R.id.messagesChatProfilePic);
        eTMessageContent = findViewById(R.id.messageChatMessageContent);
        messagesChatRecyclerView = findViewById(R.id.messagesMessageList);
        messageChatTopLayout = findViewById(R.id.messageChatTopLayout);
        homeActivity = this;

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
            iVChatProfilePic.setImageResource(R.drawable.ic_profile_user_24dp);
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
                    messagesChatRecyclerView.setLayoutManager(mLayoutManager);
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

        messageChatTopLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(homeActivity);
            }
        });

        final GestureDetector detector = new GestureDetector(getApplicationContext(), new ClickListener());
        messagesChatRecyclerView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(detector.onTouchEvent(event)) {
                    hideKeyboard(homeActivity);
                    return true;
                }
                return false;
            }
        }
        );
    }





    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.messagesChatBackButton:
                finish();
                break;
            case R.id.messageChatSendBtn:
                String msgContent = eTMessageContent.getText().toString().trim();
                eTMessageContent.requestFocus();
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
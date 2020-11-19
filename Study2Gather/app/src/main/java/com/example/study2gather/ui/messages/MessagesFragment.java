package com.example.study2gather.ui.messages;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.example.study2gather.Chat;
import com.example.study2gather.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MessagesFragment extends Fragment {

    private MessagesViewModel messagesViewModel;
    private FloatingActionButton btnNewMessage;
    private RecyclerView messagesRecyclerView;

    private DatabaseReference chatsRef;
    private FirebaseUser user;

    private long maxId;
    private String uid;
    private ArrayList<MessagesRecyclerItem> mChats;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        messagesViewModel = new ViewModelProvider(this).get(MessagesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_messages, container, false);
        btnNewMessage = root.findViewById(R.id.fab);
        chatsRef = FirebaseDatabase.getInstance().getReference("Chats");
        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        chatsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) { if (snapshot.exists()) maxId = (snapshot.getChildrenCount()); }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        btnNewMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewChat();
            }
        });

        return root;
    }

    public void createNewChat() {
        String chatName = "chat"+(maxId+1);
        chatsRef.child(chatName).child(uid).setValue(true);
        chatsRef.child(chatName).child("Z1OryxPJSUcv4KxvHKsbcuWZ7443").setValue(true);
    }
}
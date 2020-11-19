package com.example.study2gather.ui.messages;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.study2gather.Chat;
import com.example.study2gather.Post;
import com.example.study2gather.R;
import com.example.study2gather.UserObj;
import com.example.study2gather.ui.home.HomeRecylerItemArrayAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;

public class MessagesFragment extends Fragment {

    private MessagesViewModel messagesViewModel;
    private FloatingActionButton btnNewMessage;
    private RecyclerView messagesRecyclerView;
    private View root;

    private DatabaseReference chatsRef, usersRef;
    private FirebaseUser user;
    private StorageReference profilePicsRef;

    private long maxId;
    private String uid;
    private ArrayList<MessagesRecyclerItem> mChats;
    private UserObj userProfile;
    private HashMap<String, String> usersListWithName;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        messagesViewModel = new ViewModelProvider(this).get(MessagesViewModel.class);
        View root = inflater.inflate(R.layout.fragment_messages, container, false);
        btnNewMessage = root.findViewById(R.id.fab);
        usersListWithName = new HashMap<String, String>();
        chatsRef = FirebaseDatabase.getInstance().getReference("Chats");
        profilePicsRef = FirebaseStorage.getInstance().getReference("profileImages");
        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

        chatsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) { if (snapshot.exists()) maxId = (snapshot.getChildrenCount()); }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        //get own info
        usersRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) { userProfile = snapshot.getValue(UserObj.class); }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { Toast.makeText(getContext(),"Something Went Wrong",Toast.LENGTH_LONG).show(); }
        });

        //get all users info
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()) { usersListWithName.put(ds.getKey(),ds.child("username").getValue(String.class)); }
            }
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

    private void setUIRef() {
        //Reference of RecyclerView
        messagesRecyclerView = root.findViewById(R.id.homePostList);
        //Linear Layout Manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        //Set Layout Manager to RecyclerView
        messagesRecyclerView.setLayoutManager(linearLayoutManager);
        //reverse posts so most recent on top
//        Collections.reverse(mPosts);

        //Create adapter
        MessagesRecyclerItemArrayAdapter myRecyclerViewAdapter = new MessagesRecyclerItemArrayAdapter(mChats, new MessagesRecyclerItemArrayAdapter.MyRecyclerViewItemClickListener()
        {
            //Handling clicks
            @Override
            public void onItemClicked(MessagesRecyclerItem message)
            {
                Toast.makeText(getContext(), message.getChatTitle(), Toast.LENGTH_SHORT).show();
            }
        });

        //Set adapter to RecyclerView
        messagesRecyclerView.setAdapter(myRecyclerViewAdapter);
    }


    public void createNewChat() {
        String chatName = "chat"+(maxId+1);
        chatsRef.child(chatName).child(uid).setValue(true);
        chatsRef.child(chatName).child("Z1OryxPJSUcv4KxvHKsbcuWZ7443").setValue(true);
    }
}
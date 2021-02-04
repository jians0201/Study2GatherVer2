package com.example.study2gather.ui.messages;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.study2gather.Chat;
import com.example.study2gather.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class MessagesFragment extends Fragment {

    private FloatingActionButton btnNewChat;
    private RecyclerView messagesRecyclerView;
    private View root;

    private DatabaseReference chatsRef, usersRef;
    private FirebaseUser user;
    private StorageReference profilePicsRef;

    private String uid;
    private ArrayList<Chat> mChats = new ArrayList<Chat>();
    private ArrayList<String> otherMembersInChat, usersWithExistingChat = new ArrayList<String>();
    private HashMap<String, String> usersListWithName = new HashMap<String, String>();
    private MessagesRecyclerItemArrayAdapter myRecyclerViewAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_messages, container, false);
        btnNewChat = root.findViewById(R.id.messagesChatCreateFAB);
        messagesRecyclerView = root.findViewById(R.id.messagesList);
        chatsRef = FirebaseDatabase.getInstance().getReference("Chats");
        usersRef = FirebaseDatabase.getInstance().getReference("Users");
        profilePicsRef = FirebaseStorage.getInstance().getReference("profileImages");
        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        setUIRef();

        //get all users info
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()) { usersListWithName.put(ds.getKey(),ds.child("username").getValue(String.class)); }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        //get all chats
        chatsRef.orderByChild("membersList/"+uid).equalTo(true).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mChats.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Chat chat = ds.getValue(Chat.class);
                    otherMembersInChat = chat.getOtherMembers(uid);
                    //get chat pic
                    if (otherMembersInChat.size() == 1) {
                        usersWithExistingChat.add(otherMembersInChat.get(0));
                        chat.setChatTitle(usersListWithName.get(otherMembersInChat.get(0)));
                        profilePicsRef.child(otherMembersInChat.get(0)+"_profile.jpg").getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful()) {
                                    chat.setChatPic(task.getResult());
                                }
                                mChats.add(chat);
                                if (mChats.size() == snapshot.getChildrenCount()) myRecyclerViewAdapter.notifyDataSetChanged();
                            }
                        });
                    } //else get chat pic for group chat (not implemented)
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        btnNewChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), MessagesCreateChat.class);
                i.putExtra("usersWithExistingChat",usersWithExistingChat);
                startActivity(i);
            }
        });

        return root;
    }

    private void setUIRef() {
        //Linear Layout Manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        //Set Layout Manager to RecyclerView
        messagesRecyclerView.setLayoutManager(linearLayoutManager);
        //Create adapter
        myRecyclerViewAdapter = new MessagesRecyclerItemArrayAdapter(mChats, new MessagesRecyclerItemArrayAdapter.MyRecyclerViewItemClickListener()
        {
            //Handling clicks
            @Override
            public void onItemClicked(Chat chat)
            {
                Intent i = new Intent(getActivity(), MessagesChat.class);
                i.putExtra("chat", (Serializable) chat);
                startActivity(i);
            }
        });

        //Set adapter to RecyclerView
        messagesRecyclerView.setAdapter(myRecyclerViewAdapter);
    }
}
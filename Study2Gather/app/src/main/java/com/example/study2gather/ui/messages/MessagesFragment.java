package com.example.study2gather.ui.messages;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.study2gather.Chat;
import com.example.study2gather.R;
import com.example.study2gather.UserObj;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class MessagesFragment extends Fragment {
    private MessagesViewModel messagesViewModel;
    private FloatingActionButton btnNewMessage;
    private RecyclerView messagesRecyclerView;
    private View root;

    private DatabaseReference chatsRef, usersRef;
    private FirebaseUser user;
    private StorageReference profilePicsRef;

    private String uid;
    private ArrayList<Chat> mChats;
    private ArrayList<String> otherMembersInChat, usersWithExistingChat;
    private UserObj userProfile;
    private HashMap<String, String> usersListWithName;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        messagesViewModel = new ViewModelProvider(this).get(MessagesViewModel.class);
        root = inflater.inflate(R.layout.fragment_messages, container, false);
        btnNewMessage = root.findViewById(R.id.messagesChatCreateFAB);
        usersListWithName = new HashMap<String, String>();
        usersWithExistingChat = new ArrayList<String>();
        mChats = new ArrayList<Chat>();
        chatsRef = FirebaseDatabase.getInstance().getReference("Chats");
        usersRef = FirebaseDatabase.getInstance().getReference("Users");
        profilePicsRef = FirebaseStorage.getInstance().getReference("profileImages");
        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();

//        chatsRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) { if (snapshot.exists()) maxId = (snapshot.getChildrenCount()); }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {}
//        });

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

        //get all chats
        chatsRef.orderByChild("membersList/"+uid).equalTo(true).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mChats.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Chat chat = ds.getValue(Chat.class);
                    otherMembersInChat = chat.getOtherMembers(uid);
                    //get chat pic (pfp of other user for normal 2 person chat
                    if (otherMembersInChat.size() == 1) {
                        usersWithExistingChat.add(otherMembersInChat.get(0));
                        profilePicsRef.child(otherMembersInChat.get(0)+"_profile.jpg").getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful()) {
                                    chat.setChatPic(task.getResult());
                                }
                                mChats.add(chat);
                                if (mChats.size() == snapshot.getChildrenCount()) setUIRef();
                            }
                        });
                    } else {}//chat pic for group chat
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        btnNewMessage.setOnClickListener(new View.OnClickListener() {
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
        //Reference of RecyclerView
        messagesRecyclerView = root.findViewById(R.id.messagesList);
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
            public void onItemClicked(Chat message)
            {
                Toast.makeText(getContext(), message.getChatTitle(), Toast.LENGTH_SHORT).show();
            }
        });

        //Set adapter to RecyclerView
        messagesRecyclerView.setAdapter(myRecyclerViewAdapter);
    }
}
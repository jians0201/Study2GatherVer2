package com.example.study2gather.ui.messages;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
import com.google.android.gms.tasks.OnSuccessListener;
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
    private ArrayList<String> chatIdList;
    private UserObj userProfile;
    private HashMap<String, String> usersListWithName;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        messagesViewModel = new ViewModelProvider(this).get(MessagesViewModel.class);
        root = inflater.inflate(R.layout.fragment_messages, container, false);
        btnNewMessage = root.findViewById(R.id.fab);
        usersListWithName = new HashMap<String, String>();
        chatIdList = new ArrayList<String>();
        mChats = new ArrayList<MessagesRecyclerItem>();
        chatsRef = FirebaseDatabase.getInstance().getReference("Chats");
        usersRef = FirebaseDatabase.getInstance().getReference("Users");
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
                //get all chats
                chatsRef.orderByChild(uid).equalTo(true).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {

                            mChats.clear();
                            for (DataSnapshot child : snapshot.getChildren()) {
                                MessagesRecyclerItem msgRItem = new MessagesRecyclerItem();
                                chatIdList.add(child.getKey()); //fill chat ids for messaging later
                                //If Title exists means its a group chat
                                if (child.child("title").exists()) {
        //                            mChats.add(child.child("title").getValue().toString());
                                    msgRItem.setChatTitle(child.child("title").getValue().toString());
                                }
                                //If Title does not exist means its a normal chat
                                else {
                                    for (DataSnapshot c : child.getChildren()) {
                                        if (!c.getKey().equals(uid)) {
                                            String id = c.getKey();
                                            msgRItem.setChatTitle(usersListWithName.get(id));
                                            //get chat pic
                                            profilePicsRef.child(id+"_profile.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) { //only works if profile pic was found
                                                    msgRItem.setChatPic(uri);
                                                    mChats.add(msgRItem);
                                                    if (mChats.size() == snapshot.getChildrenCount()) setUIRef();
                                                }
                                            });
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });
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

    private void bindCountriesData()
    {
        mChats.add(new MessagesRecyclerItem("Canada", "BB"));
        mChats.add(new MessagesRecyclerItem("Norway","Norwegian Krone"));
        mChats.add(new MessagesRecyclerItem("Norway","Norwegian Krone"));
        mChats.add(new MessagesRecyclerItem("Norway","Norwegian Krone"));
        mChats.add(new MessagesRecyclerItem("Norway","Norwegian Krone"));
        mChats.add(new MessagesRecyclerItem("Norway","Norwegian Krone"));
        mChats.add(new MessagesRecyclerItem("Norway","Norwegian Krone"));
        mChats.add(new MessagesRecyclerItem("Norway","Norwegian Krone"));

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
package com.example.study2gather.ui.messages;

import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.study2gather.Chat;
import com.example.study2gather.R;
import com.example.study2gather.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import java.util.UUID;

public class MessagesCreateChat extends AppCompatActivity {
    private RecyclerView newChatUsersRecyclerView;
    private TextView tVSelectedUser;

    private DatabaseReference chatsRef, usersRef;
    private FirebaseUser user;
    private StorageReference profilePicsRef;

    private String uid;
    private ArrayList<User> mUsers = new ArrayList<User>();
    private ArrayList<String> usersWithExistingChat;
    private User selectedUser;
    private MessagesCreateChatRecyclerItemArrayAdapter myRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messages_chat_create);
        tVSelectedUser = findViewById(R.id.messagesChatCreateSelectedUser);
        newChatUsersRecyclerView = findViewById(R.id.messagesChatCreateUserList);

        chatsRef = FirebaseDatabase.getInstance().getReference("Chats");
        usersRef = FirebaseDatabase.getInstance().getReference("Users");
        profilePicsRef = FirebaseStorage.getInstance().getReference("profileImages");
        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        usersWithExistingChat = getIntent().getStringArrayListExtra("usersWithExistingChat");
        setUIRef();

        //get all users info
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mUsers.clear();
                for(DataSnapshot ds : snapshot.getChildren()) {
                    if (!ds.getKey().equals(uid) && !usersWithExistingChat.contains(ds.getKey())) {
                        User user = ds.getValue(User.class);
                        user.setUserID(ds.getKey());
                        //get user profile pic
                            profilePicsRef.child(ds.getKey()+"_profile.jpg").getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if (task.isSuccessful()) {
                                        user.setProfilePic(task.getResult());
                                    }
                                    mUsers.add(user);
                                    //only populate questions once all questions have been retrieved
                                    if (mUsers.size() == snapshot.getChildrenCount()-usersWithExistingChat.size()-1) myRecyclerViewAdapter.notifyDataSetChanged();
                                }
                            });

                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

    }

    // create an action bar button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.messages_create_chat_menu, menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_clear_white_24dp);
        return super.onCreateOptionsMenu(menu);
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        } else if (id == R.id.messagesChatCreateBtn && selectedUser!=null) {
            createNewChat(); //check if target user was selected
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUIRef() {
        //Linear Layout Manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MessagesCreateChat.this, RecyclerView.VERTICAL, false);
        //Set Layout Manager to RecyclerView
        newChatUsersRecyclerView.setLayoutManager(linearLayoutManager);
        //reverse posts so most recent on top
//        Collections.reverse(mPosts);
        //Create adapter
        myRecyclerViewAdapter = new MessagesCreateChatRecyclerItemArrayAdapter(mUsers, new MessagesCreateChatRecyclerItemArrayAdapter.MyRecyclerViewItemClickListener()
        {
            //Handling clicks
            @Override
            public void onItemClicked(User user)
            {
                tVSelectedUser.setVisibility(View.VISIBLE);
                tVSelectedUser.setText(user.getUsername());
                selectedUser = user;
            }
        });

        //Set adapter to RecyclerView
        newChatUsersRecyclerView.setAdapter(myRecyclerViewAdapter);
    }

    public void createNewChat() {
        Toast.makeText(MessagesCreateChat.this,"Creating New Chat", Toast.LENGTH_SHORT).show();
        final String randomChatId = "chat"+ UUID.randomUUID().toString();
        HashMap<String, Boolean> membersList = new HashMap<String, Boolean>();
        membersList.put(uid,true);
        membersList.put(selectedUser.getUserID(),true);
        Chat chat = new Chat(selectedUser.getUsername(),randomChatId, membersList);
        chatsRef.child(randomChatId).setValue(chat);
        finish();
    }
}

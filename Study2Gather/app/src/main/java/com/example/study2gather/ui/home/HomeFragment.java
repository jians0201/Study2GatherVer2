package com.example.study2gather.ui.home;

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

import com.example.study2gather.Post;
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

import java.util.ArrayList;
import java.util.HashMap;

public class HomeFragment extends Fragment {

    private FloatingActionButton btnNewPost;
    private RecyclerView homeRecyclerView;
    private View root;

    private DatabaseReference postsRef, usersRef, likesRef;
    private FirebaseUser user;
    private StorageReference imagesRef, profilePicsRef;

    private ArrayList<Post> mPosts = new ArrayList<>();
    private String uid;
    private HashMap<String, String> usersListWithName = new HashMap<String, String>();
    private ArrayList<String> likedPosts = new ArrayList<String>();
    private HomeRecylerItemArrayAdapter myRecyclerViewAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_home, container, false);
        btnNewPost = root.findViewById(R.id.homePostCreateFAB);
        homeRecyclerView = root.findViewById(R.id.homePostList);
        imagesRef = FirebaseStorage.getInstance().getReference("images");
        profilePicsRef = FirebaseStorage.getInstance().getReference("profileImages");
        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        usersRef = FirebaseDatabase.getInstance().getReference("Users");
        postsRef = FirebaseDatabase.getInstance().getReference("Posts");
        likesRef = FirebaseDatabase.getInstance().getReference("Likes");
        //set Empty Adapter
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

        //get all liked posts by user
        likesRef.orderByChild(uid).equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                likedPosts.clear();
                for (DataSnapshot ds : snapshot.getChildren()) { likedPosts.add(ds.getKey()); }
                //get posts and pics
                postsRef.orderByChild("timestamp").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        mPosts.clear();
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            Post p = ds.getValue(Post.class);
                            //get post pic
                            imagesRef.child(p.getPostPicPath()).getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    if (task.isSuccessful()) {
                                        p.setPostPic(task.getResult());
                                    }
                                    //get user profile pic
                                    profilePicsRef.child(p.getPostAuthorID()+"_profile.jpg").getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Uri> task) {
                                            if (task.isSuccessful()) {
                                                p.setPostProfilePic(task.getResult());
                                            }
                                            p.setPostAuthor(usersListWithName.get(p.getPostAuthorID()));
                                            //check if post is liked by user
                                            if (likedPosts.contains(p.getPostID())) p.setLiked(true);
                                            mPosts.add(p);
                                            //only populate posts once all posts have been retrieved
                                            if (mPosts.size() == snapshot.getChildrenCount()) myRecyclerViewAdapter.notifyDataSetChanged();
                                        }
                                    });
                                }
                            });
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        //add post btn
        btnNewPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), HomeCreatePost.class);
                startActivity(i);
            }
        });

        return root;
    }

    private void setUIRef() {
        //Linear Layout Manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        //Set Layout Manager to RecyclerView
        homeRecyclerView.setLayoutManager(linearLayoutManager);
        //Create adapter
        myRecyclerViewAdapter = new HomeRecylerItemArrayAdapter(uid, mPosts, new HomeRecylerItemArrayAdapter.MyRecyclerViewItemClickListener() {
            //Handling clicks
            @Override
            public void onItemClicked(Post post) {}
        });
        //Set adapter to RecyclerView
        homeRecyclerView.setAdapter(myRecyclerViewAdapter);
    }
}
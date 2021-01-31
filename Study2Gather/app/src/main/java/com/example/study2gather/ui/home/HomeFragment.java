package com.example.study2gather.ui.home;

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

import com.example.study2gather.Post;
import com.example.study2gather.R;
import com.example.study2gather.User;
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

import java.lang.reflect.Array;
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
    private User userProfile;
    private HashMap<String, String> usersListWithName = new HashMap<String, String>();
    private ArrayList<String> likedPosts = new ArrayList<String>();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_home, container, false);
        btnNewPost = root.findViewById(R.id.homePostCreateFAB);
        imagesRef = FirebaseStorage.getInstance().getReference("images");
        profilePicsRef = FirebaseStorage.getInstance().getReference("profileImages");
        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        usersRef = FirebaseDatabase.getInstance().getReference("Users");
        postsRef = FirebaseDatabase.getInstance().getReference("Posts");
        likesRef = FirebaseDatabase.getInstance().getReference("Likes");

        //get own info
        usersRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) { userProfile = snapshot.getValue(User.class); }
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

        //get all liked posts by user
        likesRef.orderByChild(uid).equalTo(true).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                likedPosts.clear();
                for (DataSnapshot ds : snapshot.getChildren()) { likedPosts.add(ds.getKey()); }
                //get posts and pics
                postsRef.orderByChild("timestamp").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        mPosts.clear();
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            Post p = ds.getValue(Post.class);
                            //get post pic
                            StorageReference postPic = FirebaseStorage.getInstance().getReference("images").child(p.getPostPicPath());
                            postPic.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
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
                                            if (mPosts.size() == snapshot.getChildrenCount()) setUIRef();
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
//                i.putExtra("username",userProfile.getUsername());
                startActivity(i);
            }
        });

        return root;
    }

    private void setUIRef() {
        //Reference of RecyclerView
        homeRecyclerView = root.findViewById(R.id.homePostList);
        //Linear Layout Manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        //Set Layout Manager to RecyclerView
        homeRecyclerView.setLayoutManager(linearLayoutManager);
        //reverse posts so most recent on top
//        Collections.reverse(mPosts); //not working
        //Create adapter
        HomeRecylerItemArrayAdapter myRecyclerViewAdapter = new HomeRecylerItemArrayAdapter(uid, mPosts, new HomeRecylerItemArrayAdapter.MyRecyclerViewItemClickListener() {
            //Handling clicks
            @Override
            public void onItemClicked(Post post) {
//                Toast.makeText(getContext(), post.getPostCaption(), Toast.LENGTH_SHORT).show();
            }
        });
        //Set adapter to RecyclerView
        homeRecyclerView.setAdapter(myRecyclerViewAdapter);
    }

}
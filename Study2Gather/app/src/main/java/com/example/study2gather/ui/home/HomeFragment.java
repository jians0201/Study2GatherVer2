package com.example.study2gather.ui.home;

import android.net.Uri;
import android.os.Bundle;
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
import com.example.study2gather.UserObj;
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
import java.util.Date;
import java.util.HashMap;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FloatingActionButton btnNewPost;
    private RecyclerView mRecyclerView;
    private View root;

    private DatabaseReference postsRef, userRef;
    private FirebaseUser user;
    private StorageReference imagesRef, profilePicsRef;

    private ArrayList<Post> mPosts = new ArrayList<>();
    private Date date;
    private long maxId;
    private String uid;
    private UserObj userProfile;
    private HashMap<String, String> usersListWithName;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        root = inflater.inflate(R.layout.fragment_home, container, false);
        btnNewPost = root.findViewById(R.id.fab);
        imagesRef = FirebaseStorage.getInstance().getReference("images");
        profilePicsRef = FirebaseStorage.getInstance().getReference("profileImages");
        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        userRef = FirebaseDatabase.getInstance().getReference("Users");
        postsRef = FirebaseDatabase.getInstance().getReference("Posts");
        usersListWithName = new HashMap<String, String>();

        //get own info
        userRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) { userProfile = snapshot.getValue(UserObj.class); }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { Toast.makeText(getContext(),"Something Went Wrong",Toast.LENGTH_LONG).show(); }
        });

        postsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) { if (snapshot.exists()) maxId = (snapshot.getChildrenCount()); }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        //get all users info
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()) { usersListWithName.put(ds.getKey(),ds.child("username").getValue(String.class)); }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

//        //get posts and pics
        postsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mPosts.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    Post p = ds.getValue(Post.class);
                    //get post pic
                    StorageReference profilePic = FirebaseStorage.getInstance().getReference("images").child(ds.getKey()+".jpg");
                    profilePic.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            p.setPostPic(uri);
                            //get user profile pic
                            profilePicsRef.child(p.getPostAuthor()+"_profile.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    p.setPostProfilePic(uri);
                                    p.setPostAuthor(usersListWithName.get(p.getPostAuthor()));
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

        //add post btn
        btnNewPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                date = new Date();
                createNewPost(new Post(uid, "i love demon slayer", 45664,date.getTime()));
            }
        });

        return root;
    }

    private void setUIRef() {
        //Reference of RecyclerView
        mRecyclerView = root.findViewById(R.id.homePostList);
        //Linear Layout Manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        //Set Layout Manager to RecyclerView
        mRecyclerView.setLayoutManager(linearLayoutManager);

        //Create adapter
        HomeRecylerItemArrayAdapter myRecyclerViewAdapter = new HomeRecylerItemArrayAdapter(mPosts, new HomeRecylerItemArrayAdapter.MyRecyclerViewItemClickListener()
        {
            //Handling clicks
            @Override
            public void onItemClicked(Post post)
            {
                Toast.makeText(getContext(), post.getPostAuthor(), Toast.LENGTH_SHORT).show();
            }
        });

        //Set adapter to RecyclerView
        mRecyclerView.setAdapter(myRecyclerViewAdapter);
    }

    private void createNewPost(Post post) {
        postsRef.child("post"+(maxId+1)).setValue(post);
    }
}
package com.example.study2gather.ui.forum;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.study2gather.ForumQuestion;
import com.example.study2gather.Post;
import com.example.study2gather.R;
import com.example.study2gather.UserObj;
import com.example.study2gather.ui.home.HomeCreatePost;
import com.example.study2gather.ui.home.HomeRecylerItemArrayAdapter;
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
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public class ForumFragment extends Fragment {

    private ForumViewModel forumViewModel;
    private FloatingActionButton btnNewQn;
    private RecyclerView forumRecyclerView;
    private View root;

    private DatabaseReference forumRef, usersRef;
    private FirebaseUser user;
    private StorageReference profilePicsRef;

    private ArrayList<ForumQuestion> mQns = new ArrayList<>();
    private String uid;
    private UserObj userProfile;
//    private HashMap<String, String> usersListWithName;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        forumViewModel = new ViewModelProvider(this).get(ForumViewModel.class);
        root = inflater.inflate(R.layout.fragment_forum, container, false);
        btnNewQn = root.findViewById(R.id.fab);
        profilePicsRef = FirebaseStorage.getInstance().getReference("profileImages");
        user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
        usersRef = FirebaseDatabase.getInstance().getReference("Users");
        forumRef = FirebaseDatabase.getInstance().getReference("Forum");
//        usersListWithName = new HashMap<String, String>();

        //get own info
        usersRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) { userProfile = snapshot.getValue(UserObj.class); }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { Toast.makeText(getContext(),"Something Went Wrong",Toast.LENGTH_LONG).show(); }
        });

        //get all users info
//        usersRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for(DataSnapshot ds : snapshot.getChildren()) { usersListWithName.put(ds.getKey(),ds.child("username").getValue(String.class)); }
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {}
//        });

        //get posts and pics
        forumRef.orderByChild("timestamp").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mQns.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    ForumQuestion qn = ds.getValue(ForumQuestion.class);
                    //get user profile pic
                    profilePicsRef.child(qn.getQuestionAuthorID()+"_profile.jpg").getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                qn.setQnProfilePic(task.getResult());
                            }
                            mQns.add(qn);
                            //only populate questions once all questions have been retrieved
                            if (mQns.size() == snapshot.getChildrenCount()) setUIRef();
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        //add post btn
        btnNewQn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewQn();
//                Intent i = new Intent(getActivity(), HomeCreatePost.class);
//                startActivity(i);
            }
        });

        return root;
    }

    private void setUIRef() {
        //Reference of RecyclerView
        forumRecyclerView = root.findViewById(R.id.forumQuestList);
        //Linear Layout Manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        //Set Layout Manager to RecyclerView
        forumRecyclerView.setLayoutManager(linearLayoutManager);
//       //reverse posts so most recent on top
//       Collections.reverse(mQns); //not working
        //Create adapter
        ForumRecyclerItemArrayAdapter myRecyclerViewAdapter = new ForumRecyclerItemArrayAdapter(mQns, new ForumRecyclerItemArrayAdapter.MyRecyclerViewItemClickListener() {
            //Handling clicks
            @Override
            public void onItemClicked(ForumQuestion qn) {
//                Intent i = new Intent(getActivity(), ForumQuestionDetails.class);
//                i.putExtra("users",usersListWithName);
//                i.putExtra("question",qn);
//                startActivity(i);
            }
        });
        //Set adapter to RecyclerView
        forumRecyclerView.setAdapter(myRecyclerViewAdapter);
    }

    private void createNewQn() {
        final String randomQnID = "QN"+UUID.randomUUID().toString();
        Date date = new Date();
        ForumQuestion qn = new ForumQuestion("What is Love?", "I wanna know know know know WHAT IS LOVE", userProfile.getUsername(),uid, date.getTime(),0,randomQnID);
        forumRef.child(randomQnID).setValue(qn);
    }



}
package com.example.study2gather.ui.forum;

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

import com.example.study2gather.ForumQuestion;
import com.example.study2gather.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

public class ForumFragment extends Fragment {

    private FloatingActionButton btnNewQn;
    private RecyclerView forumRecyclerView;
    private View root;

    private DatabaseReference forumRef, usersRef;
    private StorageReference profilePicsRef;

    private ArrayList<ForumQuestion> mQns = new ArrayList<>();
    private HashMap<String, String> usersListWithName = new HashMap<String, String>();
    private ForumRecyclerItemArrayAdapter myRecyclerViewAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_forum, container, false);
        btnNewQn = root.findViewById(R.id.forumQuestCreateFAB);
        forumRecyclerView = root.findViewById(R.id.forumQuestList);
        profilePicsRef = FirebaseStorage.getInstance().getReference("profileImages");
        usersRef = FirebaseDatabase.getInstance().getReference("Users");
        forumRef = FirebaseDatabase.getInstance().getReference("Forum");
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

        //get qns and pics
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
                            qn.setQuestionAuthor(usersListWithName.get(qn.getQuestionAuthorID()));
                            mQns.add(qn);
                            //only populate questions once all questions have been retrieved
                            if (mQns.size() == snapshot.getChildrenCount()) myRecyclerViewAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });

        //add question btn
        btnNewQn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ForumCreateQuestion.class);
                i.putExtra("usersListWithName", usersListWithName);
                startActivity(i);
            }
        });

        return root;
    }

    private void setUIRef() {
        //Linear Layout Manager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        //Set Layout Manager to RecyclerView
        forumRecyclerView.setLayoutManager(linearLayoutManager);
        //Create adapter
        myRecyclerViewAdapter = new ForumRecyclerItemArrayAdapter(mQns, new ForumRecyclerItemArrayAdapter.MyRecyclerViewItemClickListener() {
            //Handling clicks
            @Override
            public void onItemClicked(ForumQuestion qn) {
                Intent i = new Intent(getActivity(), ForumQuestionDetails.class);
                i.putExtra("question", (Serializable) qn);
                i.putExtra("usersListWithName", usersListWithName);
                startActivity(i);
            }
        });
        //Set adapter to RecyclerView
        forumRecyclerView.setAdapter(myRecyclerViewAdapter);
    }

}
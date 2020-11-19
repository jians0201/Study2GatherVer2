package com.example.study2gather.ui.home;

import android.os.Bundle;
import android.util.Log;
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

import com.example.study2gather.MainActivity;
import com.example.study2gather.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FloatingActionButton btn;
    private RecyclerView mRecyclerView;
    private ArrayList<HomeRecyclerItem> mPosts = new ArrayList<>();
    private View root;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        root = inflater.inflate(R.layout.fragment_home, container, false);
        btn = root.findViewById(R.id.fab);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("G","F");
            }
        });
        bindPostsData();
        setUIRef();
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
            public void onItemClicked(HomeRecyclerItem post)
            {
                Toast.makeText(getContext(), post.getPostUsername(), Toast.LENGTH_SHORT).show();
            }
        });

        //Set adapter to RecyclerView
        mRecyclerView.setAdapter(myRecyclerViewAdapter);
    }

    private void bindPostsData() {
        mPosts.add(new HomeRecyclerItem(R.drawable.tanjiro, "Tanjiro",  "6", "OwO #HiHi", R.drawable.ic_post_pic, 987654321));
        mPosts.add(new HomeRecyclerItem(R.drawable.tanjiro, "Tanjiro",  "6", "OwO #HiHi", R.drawable.ic_post_pic, 987));
        mPosts.add(new HomeRecyclerItem(R.drawable.tanjiro, "Tanjiro",  "6", "OwO #HiHi", R.drawable.ic_post_pic, 98765));
        mPosts.add(new HomeRecyclerItem(R.drawable.tanjiro, "Tanjiro",  "6", "OwO #HiHi", R.drawable.ic_post_pic, 987654321));
        mPosts.add(new HomeRecyclerItem(R.drawable.tanjiro, "Tanjiro",  "6", "OwO #HiHi", R.drawable.ic_post_pic, 987654321));
        mPosts.add(new HomeRecyclerItem(R.drawable.tanjiro, "Tanjiro",  "6", "OwO #HiHi", R.drawable.ic_post_pic, 987654321));
        mPosts.add(new HomeRecyclerItem(R.drawable.tanjiro, "Tanjiro",  "6", "OwO #HiHi", R.drawable.ic_post_pic, 987654321));
    }
}
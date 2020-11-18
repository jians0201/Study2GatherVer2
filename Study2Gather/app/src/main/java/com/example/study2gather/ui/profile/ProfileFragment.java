package com.example.study2gather.ui.profile;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.study2gather.R;

public class ProfileFragment extends Fragment implements View.OnClickListener{

    private ProfileViewModel profileViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        Button btn = (Button) root.findViewById(R.id.buttonss);
        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.
            }
        });
        return root;
    }

   // @Override
//        public void onClick(View v) {
//            switch (v.getId()) {
//                case R.id.buttonss:
//                    Log.d("testest", "Wtf");
//                    break;
//            }
//        }
}
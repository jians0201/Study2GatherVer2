package com.example.study2gather;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.study2gather.ui.messages.MessagesFragment;
import com.example.study2gather.ui.profile.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

<<<<<<< HEAD:Study2Gather/app/src/main/java/com/example/study2gather/Home.java
public class Home extends AppCompatActivity implements View.OnClickListener{
=======
public class MainActivity extends AppCompatActivity{
>>>>>>> 798ce768c88050904a14f03cf4c879d7726756d6:Study2Gather/app/src/main/java/com/example/study2gather/MainActivity.java

    private BottomNavigationView navView;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_forum, R.id.navigation_courses, R.id.navigation_messages, R.id.navigation_profile)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
    }
<<<<<<< HEAD:Study2Gather/app/src/main/java/com/example/study2gather/Home.java

     @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonss:
                Log.d("testest", "Wtf");
                break;
        }
    }
=======
>>>>>>> 798ce768c88050904a14f03cf4c879d7726756d6:Study2Gather/app/src/main/java/com/example/study2gather/MainActivity.java
}

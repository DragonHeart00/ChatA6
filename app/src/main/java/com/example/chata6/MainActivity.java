package com.example.chata6;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.chata6.SignIn.StartActivity;
import com.example.chata6.controller.ViewPagerAdapter;
import com.example.chata6.fragment.AllUserFragment;
import com.example.chata6.fragment.ChatsFragment;
import com.example.chata6.fragment.ProfileFragment;
import com.example.chata6.fragment.UserFragment;
import com.example.chata6.model.User;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private CircleImageView profile_image;
    private TextView name;
    private FirebaseUser firebaseUser;
    private DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        profile_image = findViewById(R.id.profile_image);
        name = findViewById(R.id.user_name_id);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        mToolbar= findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");


        //to show user data in main page inside TabLayout
        showUserData();


        TabLayout tabLayout = findViewById(R.id.main_tabs);
        ViewPager viewPager = findViewById(R.id.main_tab_pager);
        ViewPagerAdapter mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());


        mViewPagerAdapter.addFragment(new ChatsFragment(), "Chat");
        mViewPagerAdapter.addFragment(new UserFragment(), "Friends");
        mViewPagerAdapter.addFragment(new ProfileFragment(), "Profile");
        mViewPagerAdapter.addFragment(new AllUserFragment(), "All User");

        viewPager.setAdapter(mViewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_baseline_chat_24);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_baseline_people_24);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_baseline_account_box_24);
        tabLayout.getTabAt(3).setIcon(R.drawable.ic_baseline_people_24);

    }

    private void showUserData(){
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                name.setText(user.getName());

                if (user.getImageURL().equals("default")){
                    profile_image.setImageResource(R.drawable.pr);
                }else {
                    Glide.with(getApplicationContext()).load(user.getImageURL()).into(profile_image);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.menu, menu);

        return true;
    }

    private void sendToStart() {
        Intent startIntent = new Intent(MainActivity.this, StartActivity.class);
        startActivity(startIntent);
        finish();
    }




    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()){
            case  R.id.main_logout_btn:
            FirebaseAuth.getInstance().signOut();
            sendToStart();
            return true;
        }


        return false;
    }

}
package com.example.chata6;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.chata6.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountActivity extends AppCompatActivity {
    private TextView mProfileName;
    private CircleImageView mProfileImage;
    private DatabaseReference mUsersDatabase;
    private FirebaseUser mCurrent_user;
    private Button mProfileSendReqBtn, mDeclineBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        String user_id = getIntent().getStringExtra("user_id");
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
        mCurrent_user = FirebaseAuth.getInstance().getCurrentUser();

        mProfileName = findViewById(R.id.profile_display_name);
        mProfileImage = findViewById(R.id.profile_avatar);
        mProfileSendReqBtn = findViewById(R.id.profile_btnSendFriendRequest);
        mDeclineBtn= findViewById(R.id.profile_btnDeclineRequest);



        mUsersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                mProfileName.setText(user.getName());

                if (user.getImageURL().equals("default")){
                    mProfileImage.setImageResource(R.drawable.pr);
                }else {
                    Glide.with(getApplicationContext()).load(user.getImageURL()).into(mProfileImage);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }
}
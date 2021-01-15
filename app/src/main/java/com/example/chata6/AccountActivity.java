package com.example.chata6;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.chata6.model.ChatList;
import com.example.chata6.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountActivity extends AppCompatActivity {
    private TextView userName;
    private CircleImageView userImage;
    private FirebaseUser mCurrent_user;
    private Button sendReqBtn, mDeclineBtn;


    //show user info name and image
    private DatabaseReference mUsersDatabase;



    //request to join
    private String current_state;
    private DatabaseReference join_event_request;



    //chat list
    private List<ChatList> chatLists;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        String user_id = getIntent().getStringExtra("user_id");
        mUsersDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);
        mCurrent_user = FirebaseAuth.getInstance().getCurrentUser();

        userName = findViewById(R.id.profile_display_name);
        userImage = findViewById(R.id.profile_avatar);
        sendReqBtn = findViewById(R.id.profile_btnSendFriendRequest);
        mDeclineBtn= findViewById(R.id.profile_btnDeclineRequest);


        current_state = "not_join";
        join_event_request = FirebaseDatabase.getInstance().getReference().child("wish_to_join");


        //chat list
        chatLists = new ArrayList<>();

        // to show user info name and image
        mUsersDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                userName.setText(user.getName());

                if (user.getImageURL().equals("default")){
                    userImage.setImageResource(R.drawable.pr);
                }else {
                    Glide.with(getApplicationContext()).load(user.getImageURL()).into(userImage);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        //send request button
        sendReqBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //user_01 sent request to user_02: status: sent
                if (current_state.equals("not_join")){
                    join_event_request
                            .child(mCurrent_user.getUid())
                            .child(user_id)
                            .child("request_type")
                            .setValue("sent")
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {


                            //user_02 received request from user_01: status: received
                            if (task.isSuccessful()){
                                join_event_request
                                        .child(user_id)
                                        .child(mCurrent_user.getUid())
                                        .child("request_type")
                                        .setValue("received").addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){

                                            //TODO: added user_01 to chat list: hi user_02 i want to join your event
                                            //by adding user to chat list
                                            final DatabaseReference chatRef = FirebaseDatabase
                                                    .getInstance()
                                                    .getReference("ChatList")
                                                    .child(user_id)
                                                    .child(mCurrent_user.getUid());

                                            chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    chatRef.child("id").setValue(mCurrent_user.getUid());
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });



                                            Toast.makeText(AccountActivity.this,"Request sent Successfully",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });



                            }else {
                                Toast.makeText(AccountActivity.this,"Failed Sending request",Toast.LENGTH_SHORT).show();
                            }

                        }
                    });


                }
            }
        });




    }
}
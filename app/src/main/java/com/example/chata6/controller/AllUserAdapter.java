package com.example.chata6.controller;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chata6.AccountActivity;
import com.example.chata6.R;
import com.example.chata6.model.User;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.List;


public class AllUserAdapter extends RecyclerView.Adapter<AllUserAdapter.ViewHolder>{

    private Context mContext;
    private List<User> users;


    public AllUserAdapter(Context mContext, List<User> users) {
        this.mContext = mContext;
        this.users = users;
    }

    @NonNull
    @Override
    public AllUserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.users_list, parent, false);
        return new AllUserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllUserAdapter.ViewHolder holder, int position) {
        User user = users.get(position);
        holder.textName.setText(user.getName());

        if (user.getImageURL().equals("default")){
            holder.profileImageView.setImageResource(R.drawable.pr);
        }else {
            Glide.with(mContext).load(user.getImageURL()).into(holder.profileImageView);
        }



        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Users");
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {


                                        Intent profileIntent = new Intent(mContext, AccountActivity.class);
                                        profileIntent.putExtra("user_id", user.getId());
                                        mContext.startActivity(profileIntent);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        // handle error
                    }
                });








            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public TextView textName;
        public ImageView profileImageView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            textName= itemView.findViewById(R.id.user_single_name);
            profileImageView = itemView.findViewById(R.id.user_single_image);


        }
    }




}

package com.example.chata6.controller;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chata6.R;
import com.example.chata6.message.MessageActivity;
import com.example.chata6.model.User;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{

    private Context mContext;
    private List<User> users;

    public UserAdapter(Context mContext, List<User> users) {
        this.mContext = mContext;
        this.users = users;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.users_list, parent, false);
        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        User user = users.get(position);
        holder.textName.setText(user.getName());

        if (user.getImageURL().equals("default")){
            holder.profileImageView.setImageResource(R.drawable.pr);
        }else {
            Glide.with(mContext).load(user.getImageURL()).into(holder.profileImageView);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, MessageActivity.class);
                intent.putExtra("userid", user.getId());
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return users.size();
    }


    public  class  ViewHolder extends RecyclerView.ViewHolder{

        public TextView textName;
        public CircleImageView profileImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textName= itemView.findViewById(R.id.user_single_name);
            profileImageView = itemView.findViewById(R.id.user_single_image);
        }
    }


}

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
import com.example.chata6.model.Chat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder>{

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    private FirebaseUser firebaseUser;

    private Context mContext;
    private List<Chat> chats;
    private  String image_url;


    public MessageAdapter(Context mContext, List<Chat> chats, String image_url) {
        this.mContext = mContext;
        this.chats = chats;
        this.image_url = image_url;
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT){
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right, parent, false);
            return new MessageAdapter.ViewHolder(view);
        }else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left, parent, false);
            return new MessageAdapter.ViewHolder(view);
        }


    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {

            Chat chat = chats.get(position);
            holder.show_message.setText(chat.getMessage());

            if (image_url.equals("default")){
                holder.profileImageView.setImageResource(R.drawable.pr);
            }else {
                Glide.with(mContext).load(image_url).into(holder.profileImageView);
            }

            if (position == chats.size()-1){
                if (chat.isIs_seen()){
                    holder.text_seen.setText("Seen");
                } else {
                    holder.text_seen.setText("Delivered");
                }
            }else {
                holder.text_seen.setVisibility(View.GONE);
            }


    }

    @Override
    public int getItemCount() {
        return chats.size();
    }


    public  class  ViewHolder extends RecyclerView.ViewHolder{

        public TextView show_message;
        public CircleImageView profileImageView;
        public TextView text_seen;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            show_message= itemView.findViewById(R.id.show_message);
            profileImageView = itemView.findViewById(R.id.image_profile);
            text_seen = itemView.findViewById(R.id.txt_seen);

        }
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (chats.get(position).getSender().equals(firebaseUser.getUid())){
            return MSG_TYPE_RIGHT;

        }else {
            return MSG_TYPE_LEFT;
        }
    }
}

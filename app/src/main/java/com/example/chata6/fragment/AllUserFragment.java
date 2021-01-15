package com.example.chata6.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.chata6.R;
import com.example.chata6.controller.AllUserAdapter;
import com.example.chata6.controller.UserAdapter;
import com.example.chata6.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class AllUserFragment extends Fragment {

    private RecyclerView recyclerView;
    private DatabaseReference mUserDatabase;
    private List<User> userList;
    private AllUserAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_all_user, container, false);


        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users");

        recyclerView = view.findViewById(R.id.all_users_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        userList = new ArrayList<>();

        readUsers();


        return  view;
    }

    private void readUsers() {


        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                    userList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        User user = dataSnapshot.getValue(User.class);

                        assert user != null;
                        assert firebaseUser != null;


                        if (!user.getId().equals(firebaseUser.getUid())) {
                            userList.add(user);

                        }

                    }
                    adapter = new AllUserAdapter(getContext(), userList);
                   recyclerView.setAdapter(adapter);



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


}
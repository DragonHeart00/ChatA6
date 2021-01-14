package com.example.chata6.SignIn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.chata6.MainActivity;
import com.example.chata6.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {


    private TextInputLayout mName, mEmail, mPassword;
    private Button mCreate;
    private Toolbar mToolbar;
    //Progress Dialog
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mName = findViewById(R.id.reg_name);
        mEmail = findViewById(R.id.reg_email);
        mPassword = findViewById(R.id.reg_password);
        mCreate = findViewById(R.id.create_account_id);
        mToolbar= findViewById(R.id.register_toolbar);


        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Create Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();

        mCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = mName.getEditText().getText().toString();
                String email = mEmail.getEditText().getText().toString();
                String password = mPassword.getEditText().getText().toString();

                if (!TextUtils.isEmpty(name) || !TextUtils.isEmpty(email) || !TextUtils.isEmpty(password) ){
                    progressDialog.setTitle("Registering User");
                    progressDialog.setMessage("Please Wait while we create your account !");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    register_user(name,email,password);
                }
            }
        });



    }



    private void register_user(String name, String email, String password) {

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                 if (task.isSuccessful()) {
                     FirebaseUser firebaseUser = mAuth.getCurrentUser();
                     assert firebaseUser != null;
                     String userId = firebaseUser.getUid();


                     reference = FirebaseDatabase.getInstance().getReference("Users").child(userId);

                     HashMap <String , String > hashMap = new HashMap<>();
                     hashMap.put("id", userId);
                     hashMap.put("name", name);
                     hashMap.put("imageURL", "default");


                     reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                         @Override
                         public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    progressDialog.dismiss();
                                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                }
                         }
                     });


                 }else {
                      progressDialog.hide();
                     Toast.makeText(RegisterActivity.this, "Sign up failed", Toast.LENGTH_LONG).show();
                 }
            }
        });

    }
}
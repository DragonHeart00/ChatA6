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

public class LoginActivity extends AppCompatActivity {

    ProgressDialog progressDialog ;
    FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        progressDialog = new ProgressDialog(this);

        initViews();

        mAuth = FirebaseAuth.getInstance();


    }


    private void loginUser(String email, String password) {

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){
                    progressDialog.dismiss();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    //important
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();


                }else {
                    progressDialog.hide();
                    Toast.makeText(getApplicationContext(),"Cannot Sign in, Please check the form and try again ",Toast.LENGTH_SHORT).show();

                }

            }
        });
    }



    private void initViews(){
      Toolbar mToolbar = findViewById(R.id.login_toolbar);
      Button btn_login = findViewById(R.id.login_id_button);
      TextInputLayout mLoginPassword =findViewById(R.id.log_password);
      TextInputLayout  mLoginEmail = findViewById(R.id.log_email);
      setSupportActionBar(mToolbar);
      getSupportActionBar().setTitle("Login Account");
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);


      btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mLoginEmail.getEditText().getText().toString();
                String password = mLoginPassword.getEditText().getText().toString();
                if (!TextUtils.isEmpty(email) || !TextUtils.isEmpty(password) ){

                    progressDialog.setTitle("Logging in");
                    progressDialog.setMessage("Please Wait while we check your credentials !");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    loginUser(email, password);

                }

            }
        });

    }



}
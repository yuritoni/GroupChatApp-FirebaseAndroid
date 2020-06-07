package com.example.groupchat_app_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    TextInputEditText textEmail,TextPassword,textName;
    ProgressBar progressBar;
    DatabaseReference reference;
    FirebaseAuth auth;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        textEmail =(TextInputEditText)findViewById(R.id.email_and_register);
        TextPassword =(TextInputEditText)findViewById(R.id.password_and_register);
        textName =(TextInputEditText)findViewById(R.id.name_and_register);
        progressBar =(ProgressBar) findViewById(R.id.progressBarRegister);
        auth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference().child("Users");

    }

     public void registerUser(View v){

        progressBar.setVisibility(View.VISIBLE);
        final String email = textEmail.getText().toString();
        final String password = TextPassword.getText().toString();
        final String name = textName.getText().toString();

        if(!email.equals("")&&!password.equals("")&&!name.equals("")){
            auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        FirebaseUser firebaseuser =auth.getCurrentUser();
                        Users user= new Users();
                        user.setName(name);
                        user.setEmail(email);
                        reference.child(firebaseuser.getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(getApplicationContext(),"berhasil registrasi",Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                    Intent i =new Intent(RegisterActivity.this,GroupChatActivity.class);
                                    startActivity(i);
                                }
                                else {
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(getApplicationContext()," registrasi tidak berhasil",Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
                }
            });
        }

    }

    public void toLogin(View v){
        Intent i = new Intent(RegisterActivity.this,MainActivity.class);
        startActivity(i);

    }
}



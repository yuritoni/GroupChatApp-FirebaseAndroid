package com.example.groupchat_app_android;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ThrowOnExtraProperties;

public class MainActivity extends AppCompatActivity {

    TextInputEditText textEmail,textPassword;
    ProgressBar progressBar;
    FirebaseAuth firebaseAuth;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth =FirebaseAuth.getInstance();
        if(firebaseAuth.getCurrentUser()!=null){
            Intent intent=new Intent(MainActivity.this,GroupChatActivity.class);
            startActivity(intent);
        }
        else {
            setContentView(R.layout.activity_main);
            textEmail =(TextInputEditText)findViewById(R.id.email_and_login);
            textPassword =(TextInputEditText)findViewById(R.id.password_and_login);
            progressBar =(ProgressBar) findViewById(R.id.progressBarLogin);
            reference= FirebaseDatabase.getInstance().getReference().child("Users");
        }
    }

    public void loginUser(View v){
        progressBar.setVisibility(View.VISIBLE);
        String email=textEmail.getText().toString();
        String password=textPassword.getText().toString();
        if(!email.equals("")&&!password.equals("")){
            firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(),"Berhasil",Toast.LENGTH_SHORT).show();
                        Intent ii =new Intent(MainActivity.this,GroupChatActivity.class);
                        startActivity(ii);
                    }else {
                        Toast.makeText(getApplicationContext(),"Salah Email atau Password. Silahkan Coba Lagi",Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);
                    }
                }
            });
        }



    }

    public void forgotPassword(View v){

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        LinearLayout  container = new LinearLayout(MainActivity.this);
        container.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);


        param.setMargins(50,0,0,100);

        final EditText input = new EditText(MainActivity.this);
        input.setLayoutParams(param);
        input.setGravity(Gravity.TOP|Gravity.START);
        input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        input.setLines(1);
        input.setMaxLines(1);
        container.addView(input);

        builder.setMessage("Masukan Email Terdaftar?");
        builder.setTitle("Lupa Password");
        builder.setView(container);

        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                String email_entered=input.getText().toString();
                firebaseAuth.sendPasswordResetEmail(email_entered).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            dialog.dismiss();
                            Toast.makeText(getApplicationContext(),"Email Dikirim, Silahkan periksa email anda", Toast.LENGTH_LONG).show();


                        }
                    }
                });
            }
        });
    }


    public void toRegister(View v){
        Intent i = new Intent(MainActivity.this,RegisterActivity.class);
        startActivity(i);

    }
}

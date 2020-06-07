package com.example.groupchat_app_android;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class GroupChatActivity extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth auth;
    FirebaseDatabase database;
    DatabaseReference messagedb;
    MessageAdapter messageAdapter;
    Users users;
    List<Message> messageList;

    RecyclerView rvMessage;
    EditText etMessage;
    ImageButton imageButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);
        init();
    }

    private void init() {

        auth =FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        users = new Users();

        rvMessage = (RecyclerView) findViewById(R.id.revMessage);
        etMessage = (EditText) findViewById(R.id.etmessage);
        imageButton = (ImageButton) findViewById(R.id.btnsend);
        imageButton.setOnClickListener(this);
        messageList = new ArrayList<>();




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()== R.id.menuLogout){
            auth.signOut();
            finish();
            startActivity(new Intent(GroupChatActivity.this,MainActivity.class));


        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onClick(View v) {
        if(!TextUtils.isEmpty(etMessage.getText().toString())){
            Message  message = new Message(etMessage.getText().toString(),users.getName());
            etMessage.setText("");
            messagedb.push().setValue(message);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        final FirebaseUser currentUser = auth.getCurrentUser();
        assert currentUser != null;
        users.setUid(currentUser.getUid());
        users.setEmail(currentUser.getEmail());

        database.getReference("Users").child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                users =dataSnapshot.getValue(Users.class);
                users.setUid(currentUser.getUid());
                AllMethode.name = users.getName();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        messagedb = database.getReference("messages");
        messagedb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Message message = dataSnapshot.getValue(Message.class);
                message.setKey(dataSnapshot.getKey());
                messageList.add(message);
                displayMessage(messageList);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                Message message = dataSnapshot.getValue(Message.class);
                message.setKey(dataSnapshot.getKey());


                List<Message>  newMessage = new ArrayList<Message>();

                for(Message m: messageList){
                    if(m.getKey().equals(message.getKey())){
                        newMessage.add(message);
                    }
                    else{
                        newMessage.add(m);

                    }


                }

                messageList = newMessage;
                displayMessage(messageList);


            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                Message message = dataSnapshot.getValue(Message.class);
                message.setKey(dataSnapshot.getKey());


                List<Message>  newMessage = new ArrayList<Message>();

                for(Message m: messageList){
                    if(m.getKey().equals(message.getKey())){

                        newMessage.add(m);

                    }


                }

                messageList = newMessage;
                displayMessage(messageList);

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        messageList = new ArrayList<>();

    }

    private void displayMessage(List<Message> messageList) {
        rvMessage.setLayoutManager(new LinearLayoutManager(GroupChatActivity.this));
        messageAdapter =  new MessageAdapter(GroupChatActivity.this,messagedb,messageList);
        rvMessage.setAdapter(messageAdapter);
    }
}

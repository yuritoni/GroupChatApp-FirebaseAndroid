package com.example.groupchat_app_android;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageAdapterViewHolder> {

    Context ct;
    DatabaseReference messagedb;
    List<Message> messages;

    public MessageAdapter(Context ct, DatabaseReference messagedb, List<Message> messages) {
        this.ct = ct;
        this.messagedb = messagedb;
        this.messages = messages;
    }

    @NonNull
    @Override
    public MessageAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ct).inflate(R.layout.new_message,parent,false);

        return new MessageAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapterViewHolder holder, int position) {

        Message message = messages.get(position);
        if(message.getName().equals(AllMethode.name)){
            holder.tvTitle.setText("You: "+message.getMessage());
            holder.tvTitle.setGravity(Gravity.START);
            holder.li.setBackgroundColor(Color.parseColor("#EF9E73"));

        }else {
            holder.tvTitle.setText(message.getName()+": "+message.getMessage());
            holder.delete.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class MessageAdapterViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        ImageButton delete;
LinearLayout li;

        public MessageAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
            delete = (ImageButton) itemView.findViewById(R.id.delete);
            li = (LinearLayout) itemView.findViewById(R.id.llMessage);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    messagedb.child(messages.get(getLayoutPosition()).getKey()).removeValue();
                }
            });
        }
    }
}

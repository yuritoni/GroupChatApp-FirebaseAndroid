package com.example.groupchat_app_android;

import androidx.annotation.NonNull;

class Message {
    private String message;

    public Message(String toString, String name) {
        this.message= toString;
        this.name= name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    private String name;
    private String key;

    @NonNull
    @Override
    public String toString() {
        return  "Message{"+"message='"+message+'\''+
                ", name='"+name+'\''+
                ", key='"+key+'\''+
                '}';
    }
}

package com.example.pramadani.projekti_v21;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ChatMessage {
    private String message;
    private String username;
    private String chatID;
    private String time;

    public ChatMessage() {
    }

    public ChatMessage(String message, String username, String chatID) {
        this.message = message;
        this.username = username;
        this.chatID = chatID;
        this.time = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(Calendar.getInstance().getTime());
    }

    public ChatMessage(String message, String username, String chatID, String time) {
        this.message = message;
        this.username= username;
        this.chatID = chatID;
        this.time = time;
    }

    public String getMessage() {
        return message;
    }


    public String getTime() {
        return time;
    }



    public String getChatID() {
        return chatID;
    }

    public String getUsername() {
        return username;
    }


}

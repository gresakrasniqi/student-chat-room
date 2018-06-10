package com.example.pramadani.projekti_v21;

import java.util.ArrayList;
import java.util.Date;

public class ChatRoom {

    private String chatID;
    private String chatName;
    private String className;
    private String created;
    private String creator;
    private String faculty;
    private ArrayList<String> users;

    public ChatRoom() {

    }

    public ChatRoom(String chatName, String className, String created, String creator, String faculty, ArrayList<String> users) {
        this.chatName = chatName;
        this.className = className;
        this.created = created;
        this.creator = creator;
        this.faculty = faculty;
        this.users = users;
    }

    public String getChatID() {
        return chatID;
    }

    public void setChatID(String chatID) {
        this.chatID = chatID;
    }

    public String getChatName() {
        return chatName;
    }

    public void setChatName(String chatName) {
        this.chatName = chatName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public ArrayList<String> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<String> users) {
        this.users = users;
    }
}

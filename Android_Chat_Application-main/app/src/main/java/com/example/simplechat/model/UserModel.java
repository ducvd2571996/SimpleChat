package com.example.simplechat.model;

import com.google.firebase.Timestamp;

import java.util.List;

public class UserModel {
    private String phone;
    private String username;
    private String intro;
    private Timestamp createdTimestamp;
    private String userId;
    private String fcmToken;
    private List<String> friendIds;

    public UserModel() {
    }

    public UserModel(String phone, String username, Timestamp createdTimestamp,String userId) {
        this.phone = phone;
        this.username = username;
        this.createdTimestamp = createdTimestamp;
        this.userId = userId;
        this.intro = intro;
    }

    public UserModel(String phone, String username, Timestamp createdTimestamp,String userId, String intro, List<String> friendIds) {
        this.phone = phone;
        this.username = username;
        this.createdTimestamp = createdTimestamp;
        this.userId = userId;
        this.intro = intro;
        this.friendIds = friendIds;
    }

    public UserModel(String phone, String username, Timestamp createdTimestamp,String userId, String intro) {
        this.phone = phone;
        this.username = username;
        this.createdTimestamp = createdTimestamp;
        this.userId = userId;
        this.intro = intro;
    }
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Timestamp getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(Timestamp createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public List<String> getFriendIds() {
        return friendIds;
    }

    public void setFriendIds(List<String> friendIds) {
        this.friendIds = friendIds;
    }
}

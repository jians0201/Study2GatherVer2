package com.example.study2gather;

import java.util.ArrayList;

public class UserObj {
    private String username, email, password, gender, dob, school, location;
    private ArrayList<String> likedPosts;

    public UserObj() {}

    public UserObj(String username, String email, String password, String gender, String dob) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.dob = dob;
    }

    public UserObj(String username, String email, String password, String gender, String dob, String school, String location) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.dob = dob;
        this.school = school;
        this.location = location;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getSchool() { return school; }

    public void setSchool(String school) { this.school = school; }

    public String getLocation() { return location; }

    public void setLocation(String location) { this.location = location; }

    public ArrayList<String> getLikedPosts() {
        return likedPosts;
    }

    public void setLikedPosts(ArrayList<String> likedPosts) {
        this.likedPosts = likedPosts;
    }

    public void addLikedPost(String postID) {
        this.likedPosts.add(postID);
    }

    public void removeLikedPost(String postID) {
        this.likedPosts.remove(new String(postID));
    }
}

package com.example.study2gather;

import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

public class Chat {
    private String chatTitle, lastMsg, chatId;
    private Uri chatPic;
//    private ArrayList<String> membersList;
    private HashMap<String, Boolean> membersList;

    public Chat() {}

    public Chat(String chatTitle) {
        this.chatTitle = chatTitle;
    }

    //for recycler view
    public Chat(String chatTitle, String lastMsg) {
        this.chatTitle = chatTitle;
        this.lastMsg = lastMsg;
    }

    //for create chat page
//    public Chat(String chatTitle, String chatId, ArrayList<String> membersList) {
//        this.chatTitle = chatTitle;
//        this.chatId = chatId;
//        this.membersList = membersList;
//    }
    public Chat(String chatTitle, String chatId, HashMap<String, Boolean> membersList) {
        this.chatTitle = chatTitle;
        this.chatId = chatId;
        this.membersList = membersList;
    }

    public String getChatTitle() { return chatTitle; }

    public void setChatTitle(String chatTitle) { this.chatTitle = chatTitle; }

    public String getLastMsg() { return lastMsg; }

    public void setLastMsg(String lastMsg) { this.lastMsg = lastMsg; }

    public Uri getChatPic() { return chatPic; }

    public void setChatPic(Uri chatPic) { this.chatPic = chatPic; }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

//    public ArrayList<String> getMembersList() {
//        return membersList;
//    }
//
//    public void setMembersList(ArrayList<String> membersList) {
//        this.membersList = membersList;
//    }

    public HashMap<String, Boolean> getMembersList() {
        return membersList;
    }

    public void setMembersList(HashMap<String, Boolean> membersList) {
        this.membersList = membersList;
    }

    public ArrayList<String> getOtherMembers(String uid) {
        ArrayList<String> otherMembers = new ArrayList<String>();
        for (String key: membersList.keySet()) {
            if (!key.equals(uid)) { otherMembers.add(key); }
        }
        return otherMembers;
    }
}

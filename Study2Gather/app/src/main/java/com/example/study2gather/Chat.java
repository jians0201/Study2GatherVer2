package com.example.study2gather;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Chat implements Serializable, Parcelable {
    private String chatTitle, lastMsg, chatId;
    private Uri chatPic;
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
    public Chat(String chatTitle, String chatId, HashMap<String, Boolean> membersList) {
        this.chatTitle = chatTitle;
        this.chatId = chatId;
        this.membersList = membersList;
    }

    protected Chat(Parcel in) {
        chatTitle = in.readString();
        lastMsg = in.readString();
        chatId = in.readString();
        chatPic = in.readParcelable(Uri.class.getClassLoader());
    }

    public static final Creator<Chat> CREATOR = new Creator<Chat>() {
        @Override
        public Chat createFromParcel(Parcel in) {
            return new Chat(in);
        }

        @Override
        public Chat[] newArray(int size) {
            return new Chat[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(chatTitle);
        dest.writeString(lastMsg);
        dest.writeString(chatId);
        dest.writeParcelable(chatPic, flags);
    }
}

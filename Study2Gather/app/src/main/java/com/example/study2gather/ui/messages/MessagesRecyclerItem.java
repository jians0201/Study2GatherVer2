package com.example.study2gather.ui.messages;

import android.net.Uri;

public class MessagesRecyclerItem {
    String chatTitle, lastMsg;
    Uri chatPic;

    public MessagesRecyclerItem() {}

    public MessagesRecyclerItem(String chatTitle) {
        this.chatTitle = chatTitle;
    }

    public MessagesRecyclerItem(String chatTitle, String lastMsg) {
        this.chatTitle = chatTitle;
        this.lastMsg = lastMsg;
    }

    public String getChatTitle() { return chatTitle; }

    public void setChatTitle(String chatTitle) { this.chatTitle = chatTitle; }

    public String getLastMsg() { return lastMsg; }

    public void setLastMsg(String lastMsg) { this.lastMsg = lastMsg; }

    public Uri getChatPic() { return chatPic; }

    public void setChatPic(Uri chatPic) { this.chatPic = chatPic; }
}

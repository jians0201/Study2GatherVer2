package com.example.study2gather;

public class Message {
    String msgSender, msgContent, msgId, chatId;
    long timestamp;

    public Message() {}

    public Message(String msgSender, String msgContent, String msgId, String chatId, long timestamp) {
        this.msgSender = msgSender;
        this.msgContent = msgContent;
        this.msgId = msgId;
        this.chatId = chatId;
        this.timestamp = timestamp;
    }

    public String getMsgSender() {
        return msgSender;
    }

    public void setMsgSender(String msgSender) {
        this.msgSender = msgSender;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}

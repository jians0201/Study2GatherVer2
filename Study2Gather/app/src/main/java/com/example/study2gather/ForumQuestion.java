package com.example.study2gather;

import android.net.Uri;

public class ForumQuestion {
    private String questionTitle, questionID;
    private long timestamp;
    private String questionAuthor;
    private int ansCount;
    private Uri qnProfilePic;

    public ForumQuestion() {}

    public ForumQuestion(String questionTitle, String questionAuthor, long timestamp, int ansCount) {
        this.questionTitle = questionTitle;
        this.questionAuthor = questionAuthor;
        this.timestamp = timestamp;
        this.ansCount = ansCount;
    }

    public ForumQuestion(String questionTitle, String questionAuthor, long timestamp, int ansCount, String questionID) {
        this.questionTitle = questionTitle;
        this.questionAuthor = questionAuthor;
        this.timestamp = timestamp;
        this.ansCount = ansCount;
        this.questionID = questionID;
    }

    public String getQuestionTitle() {
        return questionTitle;
    }

    public void setQuestionTitle(String questionTitle) {
        this.questionTitle = questionTitle;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getQuestionAuthor() {
        return questionAuthor;
    }

    public void setQuestionAuthor(String questionAuthor) {
        this.questionAuthor = questionAuthor;
    }

    public int getAnsCount() {
        return ansCount;
    }

    public void setAnsCount(int ansCount) {
        this.ansCount = ansCount;
    }

    public Uri getQnProfilePic() {
        return qnProfilePic;
    }

    public void setQnProfilePic(Uri qnProfilePic) {
        this.qnProfilePic = qnProfilePic;
    }

    public String getQuestionID() {
        return questionID;
    }

    public void setQuestionID(String questionID) {
        this.questionID = questionID;
    }
}

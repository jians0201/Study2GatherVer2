package com.example.study2gather;

import android.net.Uri;

import java.io.Serializable;

public class ForumQuestion implements Serializable {
    private String questionTitle, questionID, questionAuthor, questionDescription, questionAuthorID;
    private long timestamp;
    private int ansCount;
    private Uri qnProfilePic;

    public ForumQuestion() {}

    public ForumQuestion(String questionTitle, String questionDescription, String questionAuthor, String questionAuthorID, long timestamp, int ansCount, String questionID) {
        this.questionTitle = questionTitle;
        this.questionAuthor = questionAuthor;
        this.questionAuthorID = questionAuthorID;
        this.timestamp = timestamp;
        this.ansCount = ansCount;
        this.questionID = questionID;
        this.questionDescription = questionDescription;
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

    public String getQuestionDescription() {
        return questionDescription;
    }

    public void setQuestionDescription(String questionDescription) {
        this.questionDescription = questionDescription;
    }

    public String getQuestionAuthorID() {
        return questionAuthorID;
    }

    public void setQuestionAuthorID(String questionAuthorID) {
        this.questionAuthorID = questionAuthorID;
    }
}

package com.example.study2gather;

import android.net.Uri;

public class ForumAnswer {
    private Uri answerProfilePic;
    private String questionID, answerID, answerAuthor, answerAuthorID, answerText;
    private long timestamp;

    public ForumAnswer() {}

    public ForumAnswer(String answerAuthor, String answerAuthorID, String answerText, String questionID, String answerID, long timestamp) {
        this.answerAuthor = answerAuthor;
        this.answerAuthorID = answerAuthorID;
        this.answerText = answerText;
        this.questionID = questionID;
        this.answerID = answerID;
        this.timestamp = timestamp;
    }

    public Uri getAnswerProfilePic() {
        return answerProfilePic;
    }

    public void setAnswerProfilePic(Uri answerProfilePic) {
        this.answerProfilePic = answerProfilePic;
    }

    public String getQuestionID() {
        return questionID;
    }

    public void setQuestionID(String questionID) {
        this.questionID = questionID;
    }

    public String getAnswerID() {
        return answerID;
    }

    public void setAnswerID(String answerID) {
        this.answerID = answerID;
    }

    public String getAnswerAuthor() {
        return answerAuthor;
    }

    public void setAnswerAuthor(String answerAuthor) {
        this.answerAuthor = answerAuthor;
    }

    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getAnswerAuthorID() {
        return answerAuthorID;
    }

    public void setAnswerAuthorID(String answerAuthorID) {
        this.answerAuthorID = answerAuthorID;
    }
}


package com.example.study2gather;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class ForumQuestion implements Serializable, Parcelable {
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

    protected ForumQuestion(Parcel in) {
        questionTitle = in.readString();
        questionID = in.readString();
        questionAuthor = in.readString();
        questionDescription = in.readString();
        questionAuthorID = in.readString();
        timestamp = in.readLong();
        ansCount = in.readInt();
        qnProfilePic = in.readParcelable(Uri.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(questionTitle);
        dest.writeString(questionID);
        dest.writeString(questionAuthor);
        dest.writeString(questionDescription);
        dest.writeString(questionAuthorID);
        dest.writeLong(timestamp);
        dest.writeInt(ansCount);
        dest.writeParcelable(qnProfilePic, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ForumQuestion> CREATOR = new Creator<ForumQuestion>() {
        @Override
        public ForumQuestion createFromParcel(Parcel in) {
            return new ForumQuestion(in);
        }

        @Override
        public ForumQuestion[] newArray(int size) {
            return new ForumQuestion[size];
        }
    };

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

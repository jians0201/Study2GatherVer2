package com.example.study2gather;

import android.net.Uri;

public class Post {
    private Uri postProfilePic;
    private String postAuthor;
    private String postCaption;
    private Uri postPic;
    private long postLikeCount;
    private long timestamp;

    public Post() {}

    public Post(String postAuthor, String postCaption, long postLikeCount, long timestamp) {
        this.postAuthor = postAuthor;
        this.postCaption = postCaption;
        this.timestamp = timestamp;
        this.postLikeCount = postLikeCount;
    }


    public String getPostCaption()
    {
        return postCaption;
    }

    public long getPostLikeCount() { return postLikeCount; }

    public void setPostCaption(String postCaption) { this.postCaption = postCaption; }

    public void setPostLikeCount(long postLikeCount) { this.postLikeCount = postLikeCount; }

    public long getTimestamp() { return timestamp; }

    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    public Uri getPostProfilePic() { return postProfilePic; }

    public void setPostProfilePic(Uri postProfilePic) { this.postProfilePic = postProfilePic; }

    public String getPostAuthor() { return postAuthor; }

    public void setPostAuthor(String postAuthor) { this.postAuthor = postAuthor; }

    public Uri getPostPic() { return postPic; }

    public void setPostPic(Uri postPic) { this.postPic = postPic; }
}

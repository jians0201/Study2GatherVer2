package com.example.study2gather;

import android.net.Uri;

public class Post {
    private Uri postProfilePic, postPic;
    private String postAuthor, postAuthorID, postCaption, postPicPath, postID;
    private long postLikeCount, timestamp;
    private boolean liked = false;

    public Post() {}

    public Post(String postAuthorID, String postCaption, long postLikeCount, long timestamp, String postPicPath, String postID) {
        this.postCaption = postCaption;
        this.timestamp = timestamp;
        this.postLikeCount = postLikeCount;
        this.postPicPath = postPicPath;
        this.postID = postID;
        this.postAuthorID = postAuthorID;
    }

    public String getPostCaption() { return postCaption; }

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

    public String getPostPicPath() { return postPicPath; }

    public void setPostPicPath(String postPicPath) { this.postPicPath = postPicPath; }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public String getPostAuthorID() {
        return postAuthorID;
    }

    public void setPostAuthorID(String postAuthorID) {
        this.postAuthorID = postAuthorID;
    }

    public void likePost() {
        this.postLikeCount += 1;
    }

    public void unlikePost() {
        if (this.postLikeCount>0) this.postLikeCount -= 1;

    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }
}

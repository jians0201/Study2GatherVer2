package com.example.study2gather.ui.home;

public class Post {
    private int postUserPic;
    private String postUsername;
    private String postTime;
    private String postCaption;
    private int postPic;
    private String postLikeCount;

    public Post(int postUserPic, String postUsername, String postTime, String postCaption, int postPic, double postLikeCount) {
        this.postUserPic = postUserPic;
        this.postUsername = postUsername;
        this.postTime = postTime;
        this.postCaption = postCaption;
        this.postPic = postPic;
        this.postLikeCount = (postLikeCount /1000 < 1) ? String.format("%.0f",postLikeCount) : (postLikeCount/1000000 < 1) ? String.format("%.1f", postLikeCount/1000) + "k" :String.format("%.1f", postLikeCount/1000000) + "M" ;
    }

    public int getPostUserPic() { return postUserPic; }

    public String getPostUsername()
    {
        return postUsername;
    }

    public String getPostTime()
    {
        return postTime;
    }

    public String getPostCaption()
    {
        return postCaption;
    }

    public int getPostPic()
    {
        return postPic;
    }

    public String getPostLikeCount() { return postLikeCount; }
}

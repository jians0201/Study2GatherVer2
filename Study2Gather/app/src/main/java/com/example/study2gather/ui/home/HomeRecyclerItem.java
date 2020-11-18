package com.example.study2gather.ui.home;

public class HomeRecyclerItem {
    private int postUserPic;
    private String postUsername;
    private String postTime;
    private String postCaption;
    private int postPic;

    public HomeRecyclerItem(int postUserPic, String postUsername, String postTime, String postCaption, int postPic) {
        this.postUserPic = postUserPic;
        this.postUsername = postUsername;
        this.postTime = postTime;
        this.postCaption = postCaption;
        this.postPic = postPic;
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
}

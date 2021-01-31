package com.example.study2gather.ui.courses;

public class CourseLectureTopic {
    private String courseLTtitle;
    private String courseLTLink;

    public CourseLectureTopic() {}

    public CourseLectureTopic(String courseLTtitle, String courseLTLink) {
        this.courseLTtitle = courseLTtitle;
        this.courseLTLink = courseLTLink;
    }

    public String getCourseLTtitle() {
        return courseLTtitle;
    }

    public void setCourseLTtitle(String courseLTtitle) {
        this.courseLTtitle = courseLTtitle;
    }

    public String getCourseLTLink() {
        return courseLTLink;
    }

    public void setCourseLTLink(String courseLTLink) {
        this.courseLTLink = courseLTLink;
    }
}

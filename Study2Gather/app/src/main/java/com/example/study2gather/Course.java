package com.example.study2gather;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

public class Course implements Serializable, Parcelable {
    private String courseName, courseType, courseDesc, courseId, coursePicPath;
    private int courseLecturesNum;
    private Uri coursePic;
    private ArrayList<String> courseLearnTopics, courseLectureTopics, courseLectureLinks;

    public Course() {}

    public Course(String courseId, String courseName, String courseType, String courseDesc, int courseLecturesNum, ArrayList<String> courseLearnTopics, ArrayList<String> courseLectureTopics, ArrayList<String> courseLectureLinks) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.courseType = courseType;
        this.courseDesc = courseDesc;
        this.courseLecturesNum = courseLecturesNum;
        this.courseLearnTopics = courseLearnTopics;
        this.courseLectureTopics = courseLectureTopics;
        this.courseLectureLinks = courseLectureLinks;
    }

    protected Course(Parcel in) {
        courseName = in.readString();
        courseType = in.readString();
        courseDesc = in.readString();
        courseId = in.readString();
        coursePicPath = in.readString();
        courseLecturesNum = in.readInt();
        coursePic = in.readParcelable(Uri.class.getClassLoader());
        courseLearnTopics = in.createStringArrayList();
        courseLectureTopics = in.createStringArrayList();
        courseLectureLinks = in.createStringArrayList();
    }

    public static final Creator<Course> CREATOR = new Creator<Course>() {
        @Override
        public Course createFromParcel(Parcel in) {
            return new Course(in);
        }

        @Override
        public Course[] newArray(int size) {
            return new Course[size];
        }
    };

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseType() {
        return courseType;
    }

    public void setCourseType(String courseType) {
        this.courseType = courseType;
    }

    public String getCourseDesc() {
        return courseDesc;
    }

    public void setCourseDesc(String courseDesc) {
        this.courseDesc = courseDesc;
    }

    public int getCourseLecturesNum() {
        return courseLecturesNum;
    }

    public void setCourseLecturesNum(int courseLecturesNum) {
        this.courseLecturesNum = courseLecturesNum;
    }

    public Uri getCoursePic() {
        return coursePic;
    }

    public void setCoursePic(Uri coursePic) {
        this.coursePic = coursePic;
    }

    public ArrayList<String> getCourseLearnTopics() {
        return courseLearnTopics;
    }

    public void setCourseLearnTopics(ArrayList<String> courseLearnTopics) {
        this.courseLearnTopics = courseLearnTopics;
    }

    public ArrayList<String> getCourseLectureTopics() {
        return courseLectureTopics;
    }

    public void setCourseLectureTopics(ArrayList<String> courseLectureTopics) {
        this.courseLectureTopics = courseLectureTopics;
    }

    public ArrayList<String> getCourseLectureLinks() {
        return courseLectureLinks;
    }

    public void setCourseLectureLinks(ArrayList<String> courseLectureLinks) {
        this.courseLectureLinks = courseLectureLinks;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCoursePicPath() {
        return coursePicPath;
    }

    public void setCoursePicPath(String coursePicPath) {
        this.coursePicPath = coursePicPath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(courseName);
        dest.writeString(courseType);
        dest.writeString(courseDesc);
        dest.writeString(courseId);
        dest.writeString(coursePicPath);
        dest.writeInt(courseLecturesNum);
        dest.writeParcelable(coursePic, flags);
        dest.writeStringList(courseLearnTopics);
        dest.writeStringList(courseLectureTopics);
        dest.writeStringList(courseLectureLinks);
    }
}

package com.example.bgelfius.c196_wgu;

public class Assessment {
    private int assessmentID;
    private Long courseID;
    private String assessmentType;
    private String assessmentName;
    private String assessmentDate;

    public Assessment() {

    }
    public Assessment(Long courseID, String assessmentType, String assessmentName, String assessmentDate) {
        this.courseID = courseID;
        this.assessmentType = assessmentType;
        this.assessmentName = assessmentName;
        this.assessmentDate = assessmentDate;
    }

    public int getAssessmentID() {
        return assessmentID;
    }

    public void setAssessmentID(int assessmentID) {
        this.assessmentID = assessmentID;
    }

    public Long getCourseID() {
        return courseID;
    }

    public void setCourseID(Long courseID) {
        this.courseID = courseID;
    }

    public String getAssessmentType() {
        return assessmentType;
    }

    public void setAssessmentType(String assessmentType) {
        this.assessmentType = assessmentType;
    }

    public String getAssessmentName() {
        return assessmentName;
    }

    public void setAssessmentName(String assessmentName) {
        this.assessmentName = assessmentName;
    }

    public String getAssessmentDate() {
        return assessmentDate;
    }

    public void setAssessmentDate(String assessmentDate) {
        this.assessmentDate = assessmentDate;
    }

    @Override
    public String toString() {
        return "Assessment{" +
                "assessmentType='" + assessmentType + '\'' +
                ", assessmentName='" + assessmentName + '\'' +
                ", assessmentDate='" + assessmentDate + '\'' +
                '}';
    }
}

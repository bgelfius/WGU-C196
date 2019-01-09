package com.example.bgelfius.c196_wgu;

public class Alert {
    private int alertID ;
    private String alertType;
    private String alertDate;
    private Long courseID;

    public Alert() {


    }

    public Alert(String alertType, String alertDate, Long courseID) {
        this.alertType = alertType;
        this.alertDate = alertDate;
        this.courseID = courseID;
    }

    public int getAlertID() {
        return alertID;
    }

    public void setAlertID(int alertID) {
        this.alertID = alertID;
    }

    public String getAlertType() {
        return alertType;
    }

    public void setAlertType(String alertType) {
        this.alertType = alertType;
    }

    public String getAlertDate() {
        return alertDate;
    }

    public void setAlertDate(String alertDate) {
        this.alertDate = alertDate;
    }

    public Long getCourseID() {
        return courseID;
    }

    public void setCourseID(Long courseID) {
        this.courseID = courseID;
    }

    @Override
    public String toString() {
        return "Alert{" +
                "alertType='" + alertType + '\'' +
                '}';
    }
}

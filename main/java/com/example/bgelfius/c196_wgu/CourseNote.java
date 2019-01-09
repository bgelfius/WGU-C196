package com.example.bgelfius.c196_wgu;

public class CourseNote {
    private Long courseNoteID;
    private String courseNoteNote;
    private Long courseID;

    public CourseNote() {

    }
    public CourseNote(String courseNoteNote, Long courseID) {
        this.courseNoteNote = courseNoteNote;
        this.courseID = courseID;
    }

    public Long getCourseNoteID() {
        return courseNoteID;
    }

    public void setCourseNoteID(Long courseNoteID) {
        this.courseNoteID = courseNoteID;
    }

    public String getCourseNoteNote() {
        return courseNoteNote;
    }

    public void setCourseNoteNote(String courseNoteNote) {
        this.courseNoteNote = courseNoteNote;
    }

    public Long getCourseID() {
        return courseID;
    }

    public void setCourseID(Long courseID) {
        this.courseID = courseID;
    }

    @Override
    public String toString() {
        return "CourseNote{" +
                "courseNoteNote='" + courseNoteNote + '\'' +
                '}';
    }
}

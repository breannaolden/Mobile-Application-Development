package com.example.mobileapp.Entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "instructor_table")
public class InstructorEntity {
    @PrimaryKey
    private int instructorId;
    private String instructorName;
    private String instructorPhone;
    private String instructorEmail;
    private int instructorCourseId;

    public InstructorEntity(int instructorId, String instructorName, String instructorPhone,
                            String instructorEmail, int instructorCourseId) {
        this.instructorId = instructorId;
        this.instructorName = instructorName;
        this.instructorPhone = instructorPhone;
        this.instructorEmail = instructorEmail;
        this.instructorCourseId = instructorCourseId;
    }

    public int getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(int instructorId) {
        this.instructorId = instructorId;
    }

    public String getInstructorName() {
        return instructorName;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    public String getInstructorPhone() {
        return instructorPhone;
    }

    public void setInstructorPhone(String instructorPhone) {
        this.instructorPhone = instructorPhone;
    }

    public String getInstructorEmail() {
        return instructorEmail;
    }

    public void setInstructorEmail(String instructorEmail) {
        this.instructorEmail = instructorEmail;
    }

    public int getInstructorCourseId() {
        return instructorCourseId;
    }

    public void setInstructorCourseId(int instructorCourseId) {
        this.instructorCourseId = instructorCourseId;
    }

    @Override
    public String toString() {
        return "InstructorEntity{" +
                "instructorId=" + instructorId +
                ", instructorName='" + instructorName + '\'' +
                ", instructorPhone='" + instructorPhone + '\'' +
                ", instructorEmail='" + instructorEmail + '\'' +
                ", instructorCourseId=" + instructorCourseId +
                '}';
    }
}

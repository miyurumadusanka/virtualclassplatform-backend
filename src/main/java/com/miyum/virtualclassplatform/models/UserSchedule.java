package com.miyum.virtualclassplatform.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "user_schedule")
public class UserSchedule {
    
    @Id
    private String id;

    @DBRef
    @Field(name = "student")
    private User student;

    @DBRef
    @Field(name = "schedule")
    private Schedule schedule;

    @DBRef
    @Field(name = "student_profile")
    private UserProfile studentProfile;

    @Field(name = "attendance")
    private boolean attendanceStatus;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public boolean isAttendanceStatus() {
        return attendanceStatus;
    }

    public void setAttendanceStatus(boolean attendanceStatus) {
        this.attendanceStatus = attendanceStatus;
    }

    public UserProfile getStudentProfile() {
        return studentProfile;
    }

    public void setStudentProfile(UserProfile studentProfile) {
        this.studentProfile = studentProfile;
    }
    
}

package com.miyum.virtualclassplatform.payload.response;

public class ProfileWithAttendance {
    
    private String id;
    private String fullName;
    private String address;
    private String phoneNumber;
    private String grade;
    private String userId;
    private String userCode;
    private boolean isStudent;
    private boolean attendance;
    
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public String getGrade() {
        return grade;
    }
    public void setGrade(String grade) {
        this.grade = grade;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getUserCode() {
        return userCode;
    }
    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }
    public boolean isStudent() {
        return isStudent;
    }
    public void setStudent(boolean isStudent) {
        this.isStudent = isStudent;
    }
    public boolean isAttendance() {
        return attendance;
    }
    public void setAttendance(boolean attendance) {
        this.attendance = attendance;
    }
    
}

package com.miyum.virtualclassplatform.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "profiles")
public class UserProfile {
    
    @Id
    private String id;

    @Field(name = "full_name")
    private String fullName;
    
    @Field(name = "address")
    private String address;

    @Field(name = "phone_no")
    private String phoneNumber;

    @Field(name = "grade")
    private String grade;

    @Field(name = "user_id")
    private String userId;

    @Field(name = "user_code")
    private String userCode;

    @Field(name = "is_student")
    private boolean isStudent;

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
    
}

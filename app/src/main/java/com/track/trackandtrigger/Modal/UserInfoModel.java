package com.track.trackandtrigger.Modal;

public class UserInfoModel {
    public String firstName = "";
    public String lastName = "";
    public String userName = "";
    public String phoneNumber = "";
    public String profession = "";
    public boolean isEmailVerified = false;

    public UserInfoModel(String firstName, String lastName, String userName, String phoneNumber, String profession, boolean isEmailVerified) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.profession = profession;
        this.isEmailVerified = isEmailVerified;
    }

    public UserInfoModel() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public boolean isEmailVerified() {
        return isEmailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        isEmailVerified = emailVerified;
    }
}

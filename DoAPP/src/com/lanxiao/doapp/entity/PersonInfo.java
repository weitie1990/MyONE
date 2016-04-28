package com.lanxiao.doapp.entity;

import com.lidroid.xutils.db.annotation.Id;

/**
 * Created by Thinkpad on 2016/2/16.
 */
public class PersonInfo {
    @Id
    private int id;
    private String userid;
    private String nickname;
    private String headUrl;

    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    private String City;

    public String getTag1() {
        return tag1;
    }

    public void setTag1(String tag1) {
        this.tag1 = tag1;
    }

    private String tag1;
    private String State;
    private String Country;
    private String PhoneNumber;
    private String MailAddress;
    private String CellPhoneNumber;
    private String JobTitle;
    private String CertificateID;
    private String Sex;
    private String BornDate;
    private String CompanyName;
    private String CompanyID;
    private String Department;
    private String DepartmentID;

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getDepartmentID() {
        return DepartmentID;
    }

    public void setDepartmentID(String departmentID) {
        DepartmentID = departmentID;
    }

    public String getDepartment() {
        return Department;
    }

    public void setDepartment(String department) {
        Department = department;
    }

    public String getCompanyID() {
        return CompanyID;
    }

    public void setCompanyID(String companyID) {
        CompanyID = companyID;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String companyName) {
        CompanyName = companyName;
    }

    public String getBornDate() {
        return BornDate;
    }

    public void setBornDate(String bornDate) {
        BornDate = bornDate;
    }

    public String getSex() {
        return Sex;
    }

    public void setSex(String sex) {
        Sex = sex;
    }

    public String getCertificateID() {
        return CertificateID;
    }

    public void setCertificateID(String certificateID) {
        CertificateID = certificateID;
    }

    public String getJobTitle() {
        return JobTitle;
    }

    public void setJobTitle(String jobTitle) {
        JobTitle = jobTitle;
    }

    public String getCellPhoneNumber() {
        return CellPhoneNumber;
    }

    public void setCellPhoneNumber(String cellPhoneNumber) {
        CellPhoneNumber = cellPhoneNumber;
    }

    public String getMailAddress() {
        return MailAddress;
    }

    public void setMailAddress(String mailAddress) {
        MailAddress = mailAddress;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    private String LastName;


}

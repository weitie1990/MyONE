package com.lanxiao.doapp.entity;

/**
 * Created by Thinkpad on 2016/2/17.
 */
public class UnitInfo {
    private String id;
    private String ShortName;
    private String City;
    private String State;
    private String Country;
    private String PhoneNumber;
    private String Industry;
    private String IndustryTag1;
    private String District;
    private String StreetAddress;
    private String Zip;
    private String FAXNumber;
    private String WebSite;
    private String CompanyName;

    public String getShortName() {
        return ShortName;
    }

    public void setShortName(String shortName) {
        ShortName = shortName;
    }

    public String getCity() {
        return City;
    }

    public void setCity(String city) {
        City = city;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getIndustry() {
        return Industry;
    }

    public void setIndustry(String industry) {
        Industry = industry;
    }

    public String getIndustryTag1() {
        return IndustryTag1;
    }

    public void setIndustryTag1(String industryTag1) {
        IndustryTag1 = industryTag1;
    }

    public String getDistrict() {
        return District;
    }

    public void setDistrict(String district) {
        District = district;
    }

    public String getStreetAddress() {
        return StreetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        StreetAddress = streetAddress;
    }

    public String getFAXNumber() {
        return FAXNumber;
    }

    public void setFAXNumber(String FAXNumber) {
        this.FAXNumber = FAXNumber;
    }

    public String getZip() {
        return Zip;
    }

    public void setZip(String zip) {
        Zip = zip;
    }

    public String getWebSite() {
        return WebSite;
    }

    public void setWebSite(String webSite) {
        WebSite = webSite;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String companyName) {
        CompanyName = companyName;
    }
}

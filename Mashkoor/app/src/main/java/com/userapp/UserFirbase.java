package com.userapp;

import android.location.Location;
import android.net.Uri;

public class UserFirbase {


    Uri downloadUrl;
    String location;
    String currentDateTime;
    String key;
    String idString;
    String nameIDString;
    String ppIDString;
    String lat;
    String lngg;
    String dL;
    String token;

    public UserFirbase() {
    }

    public UserFirbase(String token, String dL, String currentDateTime, Uri downloadUrl,
                       String key,
                       String idString, String nameIDString, String ppIDString, String lat, String lngg, String location)

    {
        this.token = token;
        this.dL = dL;
        this.location = location;
        this.downloadUrl = downloadUrl;
        this.currentDateTime = currentDateTime;
        this.key = key;
        this.idString = idString;
        this.nameIDString = nameIDString;
        this.ppIDString = ppIDString;
        this.lat = lat;
        this.lngg = lngg;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setDownloadUrl(Uri downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getCurrentDateTime() {
        return currentDateTime;
    }

    public void setCurrentDateTime(String currentDateTime) {
        this.currentDateTime = currentDateTime;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getIdString() {
        return idString;
    }

    public void setIdString(String idString) {
        this.idString = idString;
    }

    public String getNameIDString() {
        return nameIDString;
    }

    public void setNameIDString(String nameIDString) {
        this.nameIDString = nameIDString;
    }

    public String getPpIDString() {
        return ppIDString;
    }

    public void setPpIDString(String ppIDString) {
        this.ppIDString = ppIDString;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLngg() {
        return lngg;
    }

    public void setLngg(String lngg) {
        this.lngg = lngg;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Uri getDownloadUrl() {
        return downloadUrl;
    }

    public String getdL() {
        return dL;
    }

    public void setdL(String dL) {
        this.dL = dL;
    }
}

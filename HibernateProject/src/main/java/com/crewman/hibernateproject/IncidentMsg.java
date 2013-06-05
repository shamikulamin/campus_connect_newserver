package com.crewman.hibernateproject;
// Generated May 22, 2013 3:03:03 PM by Hibernate Tools 3.2.1.GA


import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * IncidentMsg generated by hbm2java
 */
public class IncidentMsg  implements java.io.Serializable {


     private Integer incidentId;
     private String msgTitle;
     private String msgDescription;
     private Date reportingTime;
     private String latlong;
     private Set<IncidentPicture> incidentPictures = new HashSet<IncidentPicture>(0);
     private int noOfImages;

    public IncidentMsg() {
    }

	
    public IncidentMsg(Date reportingTime) {
        this.reportingTime = reportingTime;
    }
    public IncidentMsg(String msgTitle, String msgDescription, Date reportingTime, String latlong, Set<IncidentPicture> incidentPictures) {
       this.msgTitle = msgTitle;
       this.msgDescription = msgDescription;
       this.reportingTime = reportingTime;
       this.latlong = latlong;
       this.incidentPictures = incidentPictures;
    }
    public void setNoOfImages(int numImages){
        this.noOfImages = numImages;
    }
    public int getNoOfImages(){
        return this.noOfImages;
    }
    public Integer getIncidentId() {
        return this.incidentId;
    }
    
    public void setIncidentId(Integer incidentId) {
        this.incidentId = incidentId;
    }
    public String getMsgTitle() {
        return this.msgTitle;
    }
    
    public void setMsgTitle(String msgTitle) {
        this.msgTitle = msgTitle;
    }
    public String getMsgDescription() {
        return this.msgDescription;
    }
    
    public void setMsgDescription(String msgDescription) {
        this.msgDescription = msgDescription;
    }
    public Date getReportingTime() {
        return this.reportingTime;
    }
    
    public void setReportingTime(Date reportingTime) {
        this.reportingTime = reportingTime;
    }
    public String getLatlong() {
        return this.latlong;
    }
    
    public void setLatlong(String latlong) {
        this.latlong = latlong;
    }
    public Set<IncidentPicture> getIncidentPictures() {
        return this.incidentPictures;
    }
    
    public void setIncidentPictures(Set<IncidentPicture> incidentPictures) {
        this.incidentPictures = incidentPictures;
    }
    @Override
    public String toString(){
       return msgTitle + ", " + msgDescription;
    }


}


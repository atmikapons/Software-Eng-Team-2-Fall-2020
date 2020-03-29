package com.example.user.zippypark;

import java.sql.Date;
import java.sql.Time;

/**
 * Created by user on 3/21/2020.
 */

public class Reservation {
    Date date;
    Time startTime;
    Time endTime;
    int barcode;
    int assignedSpot;
    double charge;
    int resID;

    public Reservation(Date date, Time startTime, Time endTime, int barcode,
                       int assignedSpot, double charge, int resID) {
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.barcode = barcode;
        this.assignedSpot = assignedSpot;
        this.charge = charge;
        this.resID = resID;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }

    public int getBarcode() {
        return barcode;
    }

    public void setBarcode(int barcode) {
        this.barcode = barcode;
    }

    public int getAssignedSpot() {
        return assignedSpot;
    }

    public void setAssignedSpot(int assignedSpot) {
        this.assignedSpot = assignedSpot;
    }

    public double getCharge() {
        return charge;
    }

    public void setCharge(double charge) {
        this.charge = charge;
    }

    public int getResID() {
        return resID;
    }

    public void setResID(int resID) {
        this.resID = resID;
    }
}

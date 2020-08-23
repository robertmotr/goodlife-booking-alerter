package com.robert.src;

public class Booking {

    private String startAt;

    private String endAt;

    private String totalTime;

    private int spotsAvailable;

    private int arrayIndex;

    public Booking(String startAt, String endAt, int spotsAvailable, int arrayIndex) {

        this.startAt = startAt;
        this.endAt = endAt;
        this.spotsAvailable = spotsAvailable;

        this.totalTime = startAt + "-" + endAt;
        this.arrayIndex = arrayIndex;

    }

    public String getStartAt() {
        return startAt;
    }

    public String getEndAt() {
        return endAt;
    }

    public String getTotalTime() {
        return totalTime;
    }

    public int getSpotsAvailable() { return spotsAvailable; }

    public int getArrayIndex() {return arrayIndex;}

}

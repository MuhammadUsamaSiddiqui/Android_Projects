package com.example.muhammadusama.parking_booking_system;

public class BookingDetails {

    private String Date;
    private String Start;
    private String End;
    private String UserID;
    private String PushKey;
    private String Area;
    private String Slot;

public BookingDetails(){

}

public BookingDetails(String date, String start, String end,String userID,String pushKey,String area, String slot){

    Date = date;
    Start= start;
    End = end;
    UserID = userID;
    PushKey = pushKey;
    Area = area;
    Slot = slot;
}


    public void setDate(String date) {
        Date = date;
    }

    public void setStart(String start) {
        Start = start;
    }

    public void setEnd(String end) {
        End = end;
    }

    public void setUserID(String userID) {
        UserID = userID;
    }

    public void setPushKey(String pushKey) {
        PushKey = pushKey;
    }

    public void setArea(String area) {
        Area = area;
    }

    public void setSlot(String slot) {
        Slot = slot;
    }

    public String getDate() {
        return Date;
    }

    public String getStart() {
        return Start;
    }

    public String getEnd() {
        return End;
    }

    public String getUserID() {
        return UserID;
    }

    public String getPushKey() {
        return PushKey;
    }

    public String getArea() {
        return Area;
    }

    public String getSlot() {
        return Slot;
    }
}

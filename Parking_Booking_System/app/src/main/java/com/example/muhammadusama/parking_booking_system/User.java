package com.example.muhammadusama.parking_booking_system;

public class User {

    private String Name;
    private String Email;
    private String Phone;

    public User(){

    }

    public User(String name, String email, String phone){

        Name = name;
        Email= email;
        Phone = phone;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getName() {
        return Name;
    }

    public String getPhone() {
        return Phone;
    }


}

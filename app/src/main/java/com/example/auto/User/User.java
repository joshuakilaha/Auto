package com.example.auto.User;

public class User {

    String name,email,id,phone;

   public User(){

   }

   public User(String name, String email, String id, String phone){

       this.name = name;
       this.email = email;
       this.id = id;
       this.phone = phone;

   }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}

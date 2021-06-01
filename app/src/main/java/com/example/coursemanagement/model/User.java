package com.example.coursemanagement.model;

public class User
{
    public int Id;
    public String Name;
    public String Surname;
    public String Email;
    public String Password;
    public int User_type_id;

    //#region CTR

    public User(){}

    public User(String name, String surname, String email, String password, int user_type_id) {
        Name = name;
        Surname = surname;
        Email = email;
        Password = password;
        User_type_id = user_type_id;
    }


    //#endregion

    //#region GETTER and SETTER

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getSurname() {
        return Surname;
    }

    public void setSurname(String surname) {
        Surname = surname;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public int getUser_type_id() {
        return User_type_id;
    }

    public void setUser_type_id(int user_type_id) {
        User_type_id = user_type_id;
    }

    //#endregion
}
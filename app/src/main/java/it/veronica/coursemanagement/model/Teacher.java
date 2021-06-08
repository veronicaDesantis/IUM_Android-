package it.veronica.coursemanagement.model;

public class Teacher {
    public int Id;
    public String Name;
    public String Surname;
    public int User_id;

    //#region CTR

    public Teacher(){}

    public Teacher(int id, String name, String surname) {
        Id = id;
        Name = name;
        Surname = surname;
    }

    public Teacher(String name, String surname, int user_id) {
        Name = name;
        Surname = surname;
        User_id = user_id;
    }


    //#endregione

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

    public String getFullName() { return Surname + " " + Name; }

    public int getUser_id() {
        return User_id;
    }

    public void setUser_id(int user_id) {
        User_id = user_id;
    }

    //#endregion
}

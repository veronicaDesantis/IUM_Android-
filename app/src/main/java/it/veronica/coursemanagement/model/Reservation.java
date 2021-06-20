package it.veronica.coursemanagement.model;

import android.content.SharedPreferences;

import java.sql.Date;

public class Reservation {
    public int Id;
    public int User_id;
    public int Disponibility_id;
    public int Deleted;
    public Disponibility Disponibility;
    public Boolean Editable;

    //#region GETTER and SETTER

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getUser_id() {
        return User_id;
    }

    public void setUser_id(int user_id) {
        User_id = user_id;
    }

    public int getDeleted() {
        return Deleted;
    }

    public void setDeleted(int deleted) {
        Deleted = deleted;
    }

    public int getDisponibility_id() {
        return Disponibility_id;
    }

    public void setDisponibility_id(int disponibility_id) {
        Disponibility_id = disponibility_id;
    }

    public Disponibility getDisponibility() {
        return Disponibility;
    }

    public void setDisponibility(Disponibility disponibility) {
        Disponibility = disponibility;
    }

    public Boolean getEditable(){
        return Editable;
    }

    public void setEditable(Boolean editable) {
        Editable = editable;
    }

    //#endregion
}

package it.veronica.coursemanagement.model;

import java.sql.Date;

public class Reservation {
    public int Id;
    public int User_id;
    public int Disponibility_id;

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

    public int getDisponibility_id() {
        return Disponibility_id;
    }

    public void setDisponibility_id(int disponibility_id) {
        Disponibility_id = disponibility_id;
    }

    //#endregion
}

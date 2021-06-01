package com.example.coursemanagement.model;

import java.sql.Date;

public class Reservation {
    public int Id;
    public int User_id;
    public int Teacher_course_id;
    public int Status_type_id;
    public Date Datetime;

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

    public int getTeacher_course_id() {
        return Teacher_course_id;
    }

    public void setTeacher_course_id(int teacher_course_id) {
        Teacher_course_id = teacher_course_id;
    }

    public int getStatus_type_id() {
        return Status_type_id;
    }

    public void setStatus_type_id(int status_type_id) {
        Status_type_id = status_type_id;
    }

    public Date getDatetime() {
        return Datetime;
    }

    public void setDatetime(Date datetime) {
        Datetime = datetime;
    }

    //#endregion
}

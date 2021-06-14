package it.veronica.coursemanagement.model;

import java.sql.Date;
import java.sql.Time;

public class Disponibility {
    public int Id;
    public int Teacher_Course_id;
    public Course course;
    public Teacher teacher;
    public String Datetime;
    public String StartTime;
    public String EndTime;
    public int Availability;

    //#region CTR

    public Disponibility()
    {}

    public Disponibility(String datetime, String startTime, String endTime) {
        Datetime = datetime;
        StartTime = startTime;
        EndTime = endTime;
    }


    //#endregion

    //#region GETTER and SETTER

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getTeacher_Course_id() {
        return Teacher_Course_id;
    }

    public void setTeacher_Course_id(int teacher_Course_id) {
        Teacher_Course_id = teacher_Course_id;
    }

    public String getDatetime() {
        return Datetime;
    }

    public void setDatetime(String datetime) {
        Datetime = datetime;
    }

    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }

    public String getSlotTime(){ return StartTime + " - " + EndTime; }

    public int getAvailability() {
        return Availability;
    }

    public void setAvailability(int availability) {
        Availability = availability;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

//#endregion
}

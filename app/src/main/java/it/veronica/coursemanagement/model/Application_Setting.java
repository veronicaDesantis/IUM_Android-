package it.veronica.coursemanagement.model;

public class Application_Setting {
    private int Id;
    private String Days;
    private String StarTime;
    private String EndTime;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getDays() {
        return Days;
    }

    public void setDays(String days) {
        Days = days;
    }

    public String getStarTime() {
        return StarTime;
    }

    public void setStarTime(String starTime) {
        StarTime = starTime;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }
}

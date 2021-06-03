package it.veronica.coursemanagement.model;

public class Course {
    public int Id;
    public String Code;
    public String Title;
    public String Description;
    public int Cfu;

    //#region GETTER and SETTER

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public int getCfu() {
        return Cfu;
    }

    public void setCfu(int cfu) {
        Cfu = cfu;
    }

    //#endregion
}

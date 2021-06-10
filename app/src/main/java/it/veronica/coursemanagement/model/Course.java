package it.veronica.coursemanagement.model;

public class Course {
    public int Id;
    public String Code;
    public String Title;
    public String Description;
    public int Cfu;

    public int Associated = 0;

    //#region CTR

    public Course(){}

    public Course(String code, String title, String description, int cfu) {
        Code = code;
        Title = title;
        Description = description;
        Cfu = cfu;
    }

    public Course(String code, String title, String description, int cfu, int associated)
    {
        Code = code;
        Title = title;
        Description = description;
        Cfu = cfu;
        Associated = associated;
    }


    //#endregion

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

    public int getAssociated() { return Associated; }

    public void setAssociated(int associated) { Associated = associated; }

    //#endregion
}

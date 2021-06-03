package it.veronica.coursemanagement.model;

public class Teacher_course {
    public int Id;
    public int Teacher_id;
    public int Course_id;

    //#region GETTER and SETTER

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public int getTeacher_id() {
        return Teacher_id;
    }

    public void setTeacher_id(int teacher_id) {
        Teacher_id = teacher_id;
    }

    public int getCourse_id() {
        return Course_id;
    }

    public void setCourse_id(int course_id) {
        Course_id = course_id;
    }

    //#endregion
}

package it.veronica.coursemanagement.model;

public class FilterModel {
    public static String Title = "";
    public static int Start_cfu = 0;
    public static int End_cfu = 20;
    public static int Teacher_id = -1;
    public static int Teacher_position = 0;

    public static void Reset(){
        Title = "";
        Start_cfu = 0;
        End_cfu = 20;
        Teacher_id = -1;
        Teacher_position = 0;
    }

    public static void ApplyFilter(String title, int start_cfu, int end_cfu, int teacher_id, int teacher_position)
    {
        Title = title;
        Start_cfu = start_cfu;
        End_cfu = end_cfu;
        Teacher_id = teacher_id;
        Teacher_position = teacher_position;
    }

    public static String getTitle() {
        return Title;
    }

    public static void setTitle(String title) {
        Title = title;
    }

    public static int getStart_cfu() {
        return Start_cfu;
    }

    public static void setStart_cfu(int start_cfu) {
        Start_cfu = start_cfu;
    }

    public static int getEnd_cfu() {
        return End_cfu;
    }

    public static void setEnd_cfu(int end_cfu) {
        End_cfu = end_cfu;
    }

    public static int getTeacher_id() {
        return Teacher_id;
    }

    public static void setTeacher_id(int teacher_id) {
        Teacher_id = teacher_id;
    }

    public static int getTeacher_position() {
        return Teacher_position;
    }

    public static void setTeacher_position(int teacher_position) {
        Teacher_position = teacher_position;
    }
}

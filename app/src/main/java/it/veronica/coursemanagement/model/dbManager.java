package it.veronica.coursemanagement.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class dbManager {
    private dbHelper dbHelper;
    public dbManager(Context ctx)
    {
        dbHelper=new dbHelper(ctx);
    }

    //#region USER

    public int InsertUser(User user)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", user.getName());
        contentValues.put("surname", user.getSurname());
        contentValues.put("email", user.getEmail());
        contentValues.put("password", user.getPassword());
        contentValues.put("user_type_id", user.getUser_type_id());
        long id = 0;
        try
        {
            id = db.insert(User.class.getSimpleName(), null, contentValues);
        }
        catch (SQLException sqle)
        {
            System.out.println(sqle.getMessage());
            return -1;
        }
        return (int)id;
    }

    public void UpdateUser(int user_id, String name, String surname, String email)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("surname", surname);
        contentValues.put("email", email);
        db.update(User.class.getSimpleName(), contentValues, "id="+String.valueOf(user_id), null);
    }

    public void UpdateMail(int user_id, String email)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("email", email);
        db.update(User.class.getSimpleName(), contentValues, "id="+String.valueOf(user_id), null);
    }

    public void UpdatePassword(int user_id, String password)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("password", password);
        db.update(User.class.getSimpleName(), contentValues, "id="+String.valueOf(user_id), null);
    }

    public void DeleteUser(int id)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(User.class.getSimpleName(), "id="+String.valueOf(id), null);
    }

    public User[] GetAllUser()
    {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cur = db.query(User.class.getSimpleName(), new String[]{"id", "name", "surname", "email", "password", "user_type_id"}, null, null, null, null, "");
        ArrayList<User> listItems = new ArrayList<User>();
        if (cur != null && cur.moveToFirst())
        {
            do {
                listItems.add(readUser(cur));
            }while (cur.moveToNext());
        }

        User[] arrItems = new User[cur.getCount()];
        arrItems = listItems.toArray(arrItems);
        return arrItems;
    }

    public User GetUserById(int user_id)
    {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cur = db.query(User.class.getSimpleName(), new String[]{"id", "name", "surname", "email", "password", "user_type_id"}, "id like ?", new String[]{ String.valueOf(user_id) }, null, null, "");
        if (cur != null && cur.moveToFirst()) {
            return readUser(cur);
        }
        else
        {
            return null;
        }
    }

    public User GetUserByMail(String email)
    {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cur = db.query(User.class.getSimpleName(), new String[]{"id", "name", "surname", "email", "password", "user_type_id"}, "email like ?", new String[]{ email }, null, null, "");
        if (cur != null && cur.moveToFirst()) {
            return readUser(cur);
        }
        else
        {
            return null;
        }
    }

    public User GetUserByMail_Password(String email, String password)
    {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cur = db.query(User.class.getSimpleName(), new String[]{"id", "name", "surname", "email", "password", "user_type_id"}, "email like ? AND password like ?", new String[]{ email, password }, null, null, "");
        if (cur != null && cur.moveToFirst()) {
            return readUser(cur);
        }
        else
        {
            return null;
        }
    }

    public User readUser(Cursor cur){
        User user = new User();
        user.setId(cur.getInt(cur.getColumnIndex("id")));
        user.setName(cur.getString(cur.getColumnIndex("name")));
        user.setSurname(cur.getString(cur.getColumnIndex("surname")));
        user.setEmail(cur.getString(cur.getColumnIndex("email")));
        user.setPassword(cur.getString(cur.getColumnIndex("password")));
        user.setUser_type_id(cur.getInt(cur.getColumnIndex("user_type_id")));
        return user;
    }

    //#endregion

    //#region COURSE

    public int InsertCourse(Course course)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("code", course.getCode());
        contentValues.put("title", course.getTitle());
        contentValues.put("description", course.getDescription());
        contentValues.put("cfu", course.getCfu());
        long id = 0;
        try
        {
            id = db.insert(Course.class.getSimpleName(), null, contentValues);
        }
        catch (SQLException sqle)
        {
            System.out.println(sqle.getMessage());
            return -1;
        }
        return (int)id;
    }

    public void UpdateCourse(int id, String code, String title, String description, String cfu)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("code", code);
        contentValues.put("title", title);
        contentValues.put("description", description);
        contentValues.put("cfu",cfu);
        db.update(Course.class.getSimpleName(), contentValues, "id="+String.valueOf(id), null);
    }

    public void DeleteCourse(int id)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(Course.class.getSimpleName(), "id="+String.valueOf(id), null);
    }

    public int CountCourse()
    {
        return GetAllCourse().length;
    }

    public Course[] GetAllCourse()
    {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cur = db.query(Course.class.getSimpleName(), new String[]{"id", "code", "title", "description", "cfu"}, null, null, null, null, "");
        ArrayList<Course> listItems = new ArrayList<Course>();
        if (cur != null && cur.moveToFirst())
        {
            do {
                listItems.add(readCourse(cur));
            }while (cur.moveToNext());
        }

        Course[] arrItems = new Course[cur.getCount()];
        arrItems = listItems.toArray(arrItems);
        return arrItems;
    }

    public Course GetCourseById(int course_id)
    {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cur = db.query(Course.class.getSimpleName(), new String[]{"id", "code", "title", "description", "cfu"}, "id like ?", new String[]{ String.valueOf(course_id) }, null, null, "");
        if (cur != null && cur.moveToFirst()) {
            return readCourse(cur);
        }
        else
        {
            return null;
        }
    }

    public Course readCourse(Cursor cur){
        Course course = new Course();
        course.setId(cur.getInt(cur.getColumnIndex("id")));
        course.setCode(cur.getString(cur.getColumnIndex("code")));
        course.setTitle(cur.getString(cur.getColumnIndex("title")));
        course.setDescription(cur.getString(cur.getColumnIndex("description")));
        course.setCfu(cur.getInt(cur.getColumnIndex("cfu")));
        return course;
    }

    //#endregion

    //#region TEACHER

    public int InsertTeacher(Teacher teacher)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", teacher.getName());
        contentValues.put("surname", teacher.getSurname());
        contentValues.put("user_id", teacher.getUser_id());
        long id = 0;
        try
        {
            id = db.insert(Teacher.class.getSimpleName(), null, contentValues);
        }
        catch (SQLException sqle)
        {
            System.out.println(sqle.getMessage());
            return -1;
        }
        return (int)id;
    }

    public void UpdateTeacher(int id, String name, String surname)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("surname", surname);
        db.update(Teacher.class.getSimpleName(), contentValues, "id="+String.valueOf(id), null);
    }

    public void DeleteTeacher(int id)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(Teacher.class.getSimpleName(), "id="+String.valueOf(id), null);
    }

    public int CountTeacher()
    {
        return GetAllTeacher().length;
    }

    public Teacher[] GetAllTeacher()
    {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cur = db.query(Teacher.class.getSimpleName(), new String[]{"id", "name", "surname", "user_id"}, null, null, null, null, "");
        ArrayList<Teacher> listItems = new ArrayList<Teacher>();
        if (cur != null && cur.moveToFirst())
        {
            do {
                listItems.add(readTeacher(cur));
            }while (cur.moveToNext());
        }

        Teacher[] arrItems = new Teacher[cur.getCount()];
        arrItems = listItems.toArray(arrItems);
        return arrItems;
    }

    public Teacher GetTeacherById(int teacher_id)
    {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cur = db.query(Teacher.class.getSimpleName(), new String[]{"id", "name", "surname", "user_id"}, "id like ?", new String[]{ String.valueOf(teacher_id) }, null, null, "");
        if (cur != null && cur.moveToFirst()) {
            return readTeacher(cur);
        }
        else
        {
            return null;
        }
    }

    public Teacher readTeacher(Cursor cur){
        Teacher teacher = new Teacher();
        teacher.setId(cur.getInt(cur.getColumnIndex("id")));
        teacher.setName(cur.getString(cur.getColumnIndex("name")));
        teacher.setSurname(cur.getString(cur.getColumnIndex("surname")));
        teacher.setUser_id(cur.getInt(cur.getColumnIndex("user_id")));
        return teacher;
    }

    //#endregion
}

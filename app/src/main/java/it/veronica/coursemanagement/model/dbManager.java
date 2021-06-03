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

    public void UpdatePassword(int user_id, String password)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("password", password);
        db.update(User.class.getSimpleName(), contentValues, "id="+String.valueOf(user_id), null);
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
        user.setPassword(cur.getString(cur.getColumnIndex("password")));
        user.setUser_type_id(cur.getInt(cur.getColumnIndex("user_type_id")));
        return user;
    }

    //#endregion

    //#region COURSE

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

    public int CountTeacher()
    {
        return GetAllTeacher().length;
    }

    public Teacher[] GetAllTeacher()
    {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cur = db.query(Teacher.class.getSimpleName(), new String[]{"id", "name", "surname"}, null, null, null, null, "");
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

    public Teacher readTeacher(Cursor cur){
        Teacher course = new Teacher();
        course.setId(cur.getInt(cur.getColumnIndex("id")));
        course.setName(cur.getString(cur.getColumnIndex("name")));
        course.setSurname(cur.getString(cur.getColumnIndex("surname")));
        return course;
    }

    //#endregion
}

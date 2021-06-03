package it.veronica.coursemanagement.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.coursemanagement.R;

import it.veronica.coursemanagement.utility.AesCrypt;

public class dbHelper extends SQLiteOpenHelper {
    public static final String DBNAME="CourseManagement";
    public static final String ADMINPW = "P4$$word";
    public dbHelper(Context context){

        super(context, DBNAME, null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String q="CREATE TABLE user_type (id INTEGER PRIMARY KEY, name TEXT);";
        db.execSQL(q);
        q = " CREATE TABLE user (id INTEGER PRIMARY KEY, name TEXT,surname TEXT, email TEXT, password TEXT, user_type_id INTEGER, FOREIGN KEY (user_type_id) REFERENCES user_type (id) );";
        db.execSQL(q);
        q = " CREATE TABLE course (id INTEGER PRIMARY KEY, code TEXT, title TEXT,description TEXT, cfu INTEGER);";
        db.execSQL(q);
        q = " CREATE TABLE teacher (id INTEGER PRIMARY KEY, name TEXT, surname TEXT, user_id INTEGER, FOREIGN KEY (user_id) REFERENCES user (id));";
        db.execSQL(q);
        q = " CREATE TABLE teacher_course (id INTEGER PRIMARY KEY, teacher_id INTEGER, course_id , FOREIGN KEY (teacher_id) REFERENCES teacher (id), FOREIGN KEY (course_id) REFERENCES course (id) );";
        db.execSQL(q);
        q = " CREATE TABLE status_type (id INTEGER PRIMARY KEY, name TEXT);";
        db.execSQL(q);
        q = " CREATE TABLE reservation (id INTEGER PRIMARY KEY, user_id INTEGER, teacher_course_id INTEGER, status_type_id INTEGER, datetime INTEGER, " +
                "FOREIGN KEY (teacher_course_id) REFERENCES teacher_course (id), " +
                "FOREIGN KEY (user_id) REFERENCES user (id), " +
                "FOREIGN KEY (status_type_id) REFERENCES status_type (id) );";
        db.execSQL(q);

        //Populate data source
        String i = "INSERT INTO user_type(name) VALUES('Studente');";
        db.execSQL(i);
        i = " INSERT INTO user_type(name) VALUES('Docente');";
        db.execSQL(i);
        i = " INSERT INTO user_type(name) VALUES('Amministratore');";
        db.execSQL(i);
        i = " INSERT INTO status_type(name) VALUES('Attiva');";
        db.execSQL(i);
        i = " INSERT INTO status_type(name) VALUES('Effettuata');";
        db.execSQL(i);
        i = " INSERT INTO status_type(name) VALUES('Disdetta');";
        db.execSQL(i);
        //Inserisco utenza amministrativa
        i = " INSERT INTO user(name,surname,email,password,user_type_id) VALUES('admin', 'admin','admin@mail.it', '" + AesCrypt.encrypt(ADMINPW) + "', 3)";
        db.execSQL(i);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    { }
}
package it.veronica.coursemanagement.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.coursemanagement.R;

import it.veronica.coursemanagement.utility.AesCrypt;

public class dbHelper extends SQLiteOpenHelper {
    public static final String DBNAME="CourseManagement";
    public static final String DEFAULTPW = "P4$$word";
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
        q = " CREATE TABLE disponibility (id INTEGER PRIMARY KEY, teacher_course_id INTEGER, datetime TEXT, start_time TEXT, end_time TEXT, available INTEGER)";
        db.execSQL(q);
        q = " CREATE TABLE reservation (id INTEGER PRIMARY KEY, user_id INTEGER, disponibility_id INTEGER, deleted INTEGER, " +
                "FOREIGN KEY (disponibility_id) REFERENCES disponibility (id), " +
                "FOREIGN KEY (user_id) REFERENCES user (id));";
        db.execSQL(q);
        q = " CREATE TABLE application_setting (id INTEGER PRIMARY KEY, days TEXT, start_time TEXT, end_time TEXT)";
        db.execSQL(q);

        //Populate data source
        String i = "INSERT INTO user_type(name) VALUES('Studente');";
        db.execSQL(i);
        i = " INSERT INTO user_type(name) VALUES('Docente');";
        db.execSQL(i);
        //Inserisco utenza amministrativa
        i = " INSERT INTO user(name,surname,email,password,user_type_id) VALUES('admin', 'admin','admin@mail.it', '" + AesCrypt.encrypt(DEFAULTPW) + "', 3)";
        db.execSQL(i);
        //Inserisco utenza docente
        i = " INSERT INTO user(name,surname,email,password,user_type_id) VALUES('docente', 'docente', 'docente@mail.it', '" + AesCrypt.encrypt(DEFAULTPW) + "', 2)";
        db.execSQL(i);
        //Inserisco docente
        i = " INSERT INTO teacher(name, surname, user_id) VALUES ('docente', 'docente', 2)";
        db.execSQL(i);
        //Inserisco utenza utente
        i = " INSERT INTO user(name,surname,email,password,user_type_id) VALUES('utente', 'utente', 'utente@mail.it', '" + AesCrypt.encrypt(DEFAULTPW) + "', 1)";
        db.execSQL(i);
        //Inserisco i setting
        i = " INSERT INTO application_setting(days,start_time,end_time) VALUES('lunedi,martedi,mercoledi,giovedi,venerdi', '14:00', '18:00')";
        db.execSQL(i);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
    }
}
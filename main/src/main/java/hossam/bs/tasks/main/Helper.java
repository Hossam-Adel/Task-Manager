package hossam.bs.tasks.main;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Map;

public class Helper extends SQLiteOpenHelper{
    private static final String DATABASE_NAME = "tasks.DB";
    private static final int DATABASE_VERSION = 1;

    public Helper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS USERS(ID INTEGER PRIMARY KEY AUTOINCREMENT, name CHAR(20),CONSTRAINT unique_name UNIQUE (name));");
        db.execSQL("CREATE TABLE IF NOT EXISTS TASK(ID INTEGER PRIMARY KEY AUTOINCREMENT, name CHAR(20), description char(50),dt char(40),priority integer, done integer , user integer);");
        db.execSQL("CREATE TABLE IF NOT EXISTS COMMENTS(id integer primary key autoincrement, text char(200), time char(40), task integer)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}

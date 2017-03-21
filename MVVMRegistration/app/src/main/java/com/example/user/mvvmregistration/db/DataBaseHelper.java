package com.example.user.mvvmregistration.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DataBaseHelper extends SQLiteOpenHelper implements IDataBaseHelper{

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "mvvmRegistration";
    private static final String TABLE_USERS = "users";
    protected SQLiteDatabase db;
    private DataBaseHelper mDbHelper;

    public static String KEY_ID = "id";
    public static String KEY_FIRST_NAME = "firstName";
    public static String KEY_LAST_NAME = "lastName";
    public static String KEY_AVATAR = "avatar";

    String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + " ("
            + KEY_ID + " INTEGER PRIMARY KEY,"
            + KEY_FIRST_NAME + " TEXT,"
            + KEY_LAST_NAME + " TEXT,"
            + KEY_AVATAR + " TEXT" + ")";

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mDbHelper = this;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db = db;
        db.execSQL(CREATE_USERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    public DataBaseHelper open() throws SQLException {
        db = mDbHelper.getWritableDatabase();
        return this;
    }

    public DataBaseHelper openForRead() throws SQLException {
        db = mDbHelper.getReadableDatabase();
        return this;
    }

    public void close() {
        db.close();
    }

    public void addUser(int id, String firstName, String lastName, String avatar){
        ContentValues cv = new ContentValues();
        cv.put(KEY_ID, id);
        cv.put(KEY_FIRST_NAME, firstName);
        cv.put(KEY_LAST_NAME, lastName);
        cv.put(KEY_AVATAR, avatar);
        db.insert(TABLE_USERS, null, cv);
    }

    public void updateOrInsertUsers(int id, String firstName, String lastName, String avatar){
        String where = KEY_ID + " = " + id;
        ContentValues cv = new ContentValues();
        cv.put(KEY_ID, id);
        cv.put(KEY_FIRST_NAME, firstName);
        cv.put(KEY_LAST_NAME, lastName);
        cv.put(KEY_AVATAR, avatar);
        int u = db.update(TABLE_USERS, cv, where, null);
        //Log.d("UPDATE DATABASE", firstName + " UPDATE");
        if (u == 0) {
            db.insertWithOnConflict(TABLE_USERS, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
            //Log.d("INSERT DATABASE", firstName + " INSERT");
        }
    }

    public void updateUser(int id, String firstName, String lastName, String avatar){
        String where = KEY_ID + " = " + id;
        ContentValues cv = new ContentValues();
        cv.put(KEY_ID, id);
        cv.put(KEY_FIRST_NAME, firstName);
        cv.put(KEY_LAST_NAME, lastName);
        cv.put(KEY_AVATAR, avatar);
        db.update(TABLE_USERS, cv, where, null);
    }

    public Cursor getAllUsers(){
        return db.query(TABLE_USERS, new String[]{
                KEY_ID,
                KEY_FIRST_NAME,
                KEY_LAST_NAME,
                KEY_AVATAR}, null, null, null, null, null);
    }

}

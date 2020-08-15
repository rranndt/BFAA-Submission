package com.belajar.bfaa_submission1.Db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.belajar.bfaa_submission1.Model.Users;

import java.util.ArrayList;

import static com.belajar.bfaa_submission1.Db.DbContract.LikesColumns.AVATAR;
import static com.belajar.bfaa_submission1.Db.DbContract.LikesColumns.ID;
import static com.belajar.bfaa_submission1.Db.DbContract.LikesColumns.URL;
import static com.belajar.bfaa_submission1.Db.DbContract.LikesColumns.USERNAME;
import static com.belajar.bfaa_submission1.Db.DbContract.TABLE_NAME;

public class LikesHelper {
    private static final String DB_TABLE = TABLE_NAME;
    private static DbHelper dbHelper;
    private static LikesHelper INSTANCE;

    private static SQLiteDatabase database;

    private LikesHelper(Context context) {
        dbHelper = new DbHelper(context);
    }

    public static LikesHelper getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (SQLiteOpenHelper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new LikesHelper(context);
                }
            }
        }
        return INSTANCE;
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
        if (database.isOpen())
            database.close();
    }

    public Cursor queryAll() {
        return database.query(DB_TABLE,
                null,
                null,
                null,
                null,
                null,
                ID + " ASC");
    }

    public Cursor queryById(String id) {
        return database.query(DB_TABLE, null,
                ID + " = ?",
                new String[]{id},
                null,
                null,
                null,
                null);
    }

    public ArrayList<Users> getDataUsers() {
        ArrayList<Users> usersArrayList = new ArrayList<>();
        Cursor cursor = database.query(DB_TABLE,
                null,
                null,
                null,
                null,
                null,
                USERNAME + " ASC",
                null);
        cursor.moveToFirst();
        Users users;
        if (cursor.getCount() > 0) {
            do {
                users = new Users();
                users.setId(cursor.getInt(cursor.getColumnIndexOrThrow(ID)));
                users.setAvatarUrl(cursor.getString(cursor.getColumnIndexOrThrow(AVATAR)));
                users.setLogin(cursor.getString(cursor.getColumnIndexOrThrow(USERNAME)));
                users.setHtmlUrl(cursor.getString(cursor.getColumnIndexOrThrow(URL)));
                usersArrayList.add(users);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        } cursor.close();
        return usersArrayList;
    }

    public long insert(Users users) {
        ContentValues values = new ContentValues();
        values.put(ID, users.getId());
        values.put(AVATAR, users.getAvatarUrl());
        values.put(USERNAME, users.getLogin());
        values.put(URL, users.getHtmlUrl());
        return database.insert(DB_TABLE, null, values);
    }

    public int delete(String id) {
        return database.delete(TABLE_NAME, ID + " ='" + id + "'", null);
    }
}

package com.belajar.bfaa_submission1.Db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "likesdb";
    private static final int DB_VERSION = 1;

    private static final String CREATE_TABLE = String.format("CREATE TABLE %s"
                    + "(%s INTEGET PRIMARY KEY,"
                    + "%s TEXT NOT NULL,"
                    + "%s TEXT NOT NULL,"
                    + "%s TEXT NOT NULL)",
            DbContract.TABLE_NAME,
            DbContract.LikesColumns._ID,
            DbContract.LikesColumns.AVATAR,
            DbContract.LikesColumns.USERNAME,
            DbContract.LikesColumns.URL
    );

    public DbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DbContract.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}

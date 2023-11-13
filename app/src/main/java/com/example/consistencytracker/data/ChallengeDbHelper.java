package com.example.consistencytracker.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ChallengeDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "challenge.db";
    private static final int DATABASE_VERSION = 1;

    // SQL query to create the Challenge table
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + ChallengeContract.ChallengeEntry.TABLE_NAME + " (" +
                    ChallengeContract.ChallengeEntry._ID + " INTEGER PRIMARY KEY," +
                    ChallengeContract.ChallengeEntry.COLUMN_TITLE + " TEXT," +
                    ChallengeContract.ChallengeEntry.COLUMN_DESCRIPTION + " TEXT," +
                    ChallengeContract.ChallengeEntry.COLUMN_DURATION + " INTEGER)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + ChallengeContract.ChallengeEntry.TABLE_NAME;

    public ChallengeDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}

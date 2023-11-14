package com.example.consistencytracker.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class ChallengeDataSource {
    private SQLiteDatabase database;
    private ChallengeDbHelper dbHelper;

    public ChallengeDataSource(Context context) {
        dbHelper = new ChallengeDbHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long insertChallenge(Challenge challenge) {
        ContentValues values = new ContentValues();
        values.put(ChallengeContract.ChallengeEntry.COLUMN_TITLE, challenge.getTitle());
        values.put(ChallengeContract.ChallengeEntry.COLUMN_DESCRIPTION, challenge.getDescription());
        values.put(ChallengeContract.ChallengeEntry.COLUMN_DURATION, challenge.getDuration());

        return database.insert(ChallengeContract.ChallengeEntry.TABLE_NAME, null, values);
    }

    public List<Challenge> getAllChallenges() {
        List<Challenge> challenges = new ArrayList<>();
        Cursor cursor = database.query(
                ChallengeContract.ChallengeEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Challenge challenge = new Challenge();
                challenge.setId(cursor.getLong(cursor.getColumnIndexOrThrow(ChallengeContract.ChallengeEntry._ID)));
                challenge.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(ChallengeContract.ChallengeEntry.COLUMN_TITLE)));
                challenge.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(ChallengeContract.ChallengeEntry.COLUMN_DESCRIPTION)));
                challenge.setDuration(cursor.getInt(cursor.getColumnIndexOrThrow(ChallengeContract.ChallengeEntry.COLUMN_DURATION)));
                challenges.add(challenge);
            } while (cursor.moveToNext());

            cursor.close();
        }

        return challenges;
    }

    public int updateChallenge(Challenge challenge) {
        ContentValues values = new ContentValues();
        values.put(ChallengeContract.ChallengeEntry.COLUMN_TITLE, challenge.getTitle());
        values.put(ChallengeContract.ChallengeEntry.COLUMN_DESCRIPTION, challenge.getDescription());
        values.put(ChallengeContract.ChallengeEntry.COLUMN_DURATION, challenge.getDuration());

        return database.update(
                ChallengeContract.ChallengeEntry.TABLE_NAME,
                values,
                ChallengeContract.ChallengeEntry._ID + " = ?",
                new String[]{String.valueOf(challenge.getId())}
        );
    }

    public boolean deleteChallenge(long challengeId) {
        int rowsAffected = database.delete(
                ChallengeContract.ChallengeEntry.TABLE_NAME,
                ChallengeContract.ChallengeEntry._ID + " = ?",
                new String[]{String.valueOf(challengeId)}
        );

        return rowsAffected > 0;
    }
}

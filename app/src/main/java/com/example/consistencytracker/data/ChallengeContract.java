package com.example.consistencytracker.data;

import android.provider.BaseColumns;

public final class ChallengeContract {
    private ChallengeContract() {}

    public static class ChallengeEntry implements BaseColumns {
        public static final String TABLE_NAME = "challenge";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_DURATION = "duration";
    }
}

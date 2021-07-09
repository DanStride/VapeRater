package com.upskilled.vaperater;

import android.provider.BaseColumns;

// Class to define and create Database
public final class JuiceRaterDatabaseContract {

    // Make non-creatable
    private JuiceRaterDatabaseContract() {}

    public static final class JuiceInfoEntry implements BaseColumns {
        public static final String TABLE_NAME = "juice_info";
        public static final String COLUMN_NAME = "juice_name";
        public static final String COLUMN_BRAND = "juice_brand";
        public static final String COLUMN_CATEGORY = "juice_category";
        public static final String COLUMN_DESCRIPTION = "juice_description";
        public static final String COLUMN_RATING = "juice_rating";


        public static final String SQL_CREATE_TABLE =
                "CREATE TABLE " + TABLE_NAME + " (" + _ID + " INTEGER PRIMARY KEY, " + COLUMN_NAME + " TEXT NOT NULL, " +
                        COLUMN_BRAND + " TEXT, " + COLUMN_CATEGORY + " INTEGER, " + COLUMN_DESCRIPTION + " STRING, " +
                        COLUMN_RATING + " REAL)";
    }

}

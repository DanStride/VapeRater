package com.upskilled.vaperater;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class JuiceRaterOpenHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "JuiceRater.db";
    public static final int DATABASE_VERSION = 1;
    public JuiceRaterOpenHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // If database doesn't exist, creates new database based on definitions in
        // JuiceRateDatabaseContract class. Calls a new Database worker to populate database with
        // default data using an insert method
        db.execSQL(JuiceRaterDatabaseContract.JuiceInfoEntry.SQL_CREATE_TABLE);

        DatabaseDataWorker worker = new DatabaseDataWorker(db);
        worker.insertJuices();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // Use when database needs to be upgraded
    }
}

package com.upskilled.vaperater;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;


// Class for generating some initial Database info if none exists
public class DatabaseDataWorker {
    private SQLiteDatabase mDb;

    public DatabaseDataWorker(SQLiteDatabase db) {
        mDb = db;
    }

    public void insertJuices() {
        insertJuice("Simply Lime", "Simply", 1, "The says it all. An excellent lime flavour, nice on hot days. The ice variant is also good", (float) 8.5);
        insertJuice("Admiral V", "VapourEyes", 2, "Supposedly like a glass of strawberry milk, but to me tasted more like a strawberry biscuit. Gets old quite fast and slightly harsh", (float) 6);
        insertJuice("-273", "VapourEyes", 3, "This is the mintiest mint that has ever existed. So cold it hurts. Works only as an additive to other flavours IMO.", (float) 3);
    }

    public void insertJuice(String name, String brand, int category, String desc, float rating) {
        ContentValues values = new ContentValues();
        values.put(JuiceRaterDatabaseContract.JuiceInfoEntry.COLUMN_NAME, name);
        values.put(JuiceRaterDatabaseContract.JuiceInfoEntry.COLUMN_BRAND, brand);
        values.put(JuiceRaterDatabaseContract.JuiceInfoEntry.COLUMN_CATEGORY, category);
        values.put(JuiceRaterDatabaseContract.JuiceInfoEntry.COLUMN_DESCRIPTION, desc);
        values.put(JuiceRaterDatabaseContract.JuiceInfoEntry.COLUMN_RATING, rating);

        long newRowId = mDb.insert(JuiceRaterDatabaseContract.JuiceInfoEntry.TABLE_NAME, null, values);
    }
}

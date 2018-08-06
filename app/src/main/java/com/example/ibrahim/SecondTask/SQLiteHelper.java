package com.example.ibrahim.SecondTask;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE_DICTIONARY = "dictionary";
    public static final String COL_ALPHABETS = "alphabets";
    public static final String COL_EXPLAINATION = "description";
    private static final int DATABASE_VERSION = 1;
    static String DATABASE_NAME = "Dictionaries";


    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_DICTIONARY + " (" + COL_ALPHABETS + " TEXT);";
        database.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DICTIONARY);
        onCreate(db);
    }

    public boolean insertData(String alphabets) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_ALPHABETS, alphabets);

        long result = db.insert(TABLE_DICTIONARY, null, contentValues);
        if (result == -1)
            return false;
        else
            return true;
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_DICTIONARY, null);
        if (res.getCount() > 0) {
            return res;
        } else {
            return null;
        }
    }

    public Cursor getItemID(String alphabets) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_DICTIONARY +
                " WHERE " + COL_ALPHABETS + " = '" + alphabets + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

}
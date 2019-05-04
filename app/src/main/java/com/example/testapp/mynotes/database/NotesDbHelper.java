package com.example.testapp.mynotes.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class NotesDbHelper extends SQLiteOpenHelper {
    public NotesDbHelper(Context context) {
        super(context, NotesContract.DB_NAME, null, NotesContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        for(String query : NotesContract.CREATE_DATABASE_QUERIES){
//            db.execSQL(query);
//        }

        db.execSQL(NotesContract.Notes.CREATE_TABLE);
        db.execSQL(NotesContract.Notes.CREATE_UPDATED_TS_INDEX);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + NotesContract.DB_NAME);
        onCreate(db);
    }
}

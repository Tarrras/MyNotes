package com.example.testapp.mynotes.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

public class NotesProvider extends ContentProvider {

    private NotesDbHelper notesDbHelper;
    public static final int ITEMS = 1;
    public static final int ITEM = 2;

    public static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        URI_MATCHER.addURI(NotesContract.AUTHORITY, NotesContract.PATH,ITEMS);
        URI_MATCHER.addURI(NotesContract.AUTHORITY, NotesContract.PATH + "/#",ITEM);
    }

    @Override
    public boolean onCreate() {
        notesDbHelper = new NotesDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteDatabase database = notesDbHelper.getReadableDatabase();
        Cursor cursor;

        switch (URI_MATCHER.match(uri)){
            case ITEMS:
                if(TextUtils.isEmpty(sortOrder)){
                    sortOrder = NotesContract.Notes.COLUMN_CREATED_TS + " DESC";
                }

                cursor = database.query(
                        NotesContract.Notes.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case ITEM:
                selection = NotesContract.Notes._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(
                        NotesContract.Notes.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Error in cursor");

        }

        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (URI_MATCHER.match(uri)) {
            case ITEMS:
                return NotesContract.Notes.URI_TYPE_NOTE_DIR;

            case ITEM:
                return NotesContract.Notes.URI_TYPE_NOTE_ITEM;

            default:
                return null;
        }
    }


    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase database = notesDbHelper.getWritableDatabase();
        switch (URI_MATCHER.match(uri)){
            case ITEMS:
                long id = database.insert(NotesContract.Notes.TABLE_NAME, null, values);
                if(id>0){
                    Uri itemUri = ContentUris.withAppendedId(NotesContract.Notes.CONTENT_URI,id);
                    getContext().getContentResolver().notifyChange(uri,null);
                    return itemUri;
                }
                return null;
                default:
                    return null;
        }

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase database = notesDbHelper.getWritableDatabase();

        switch (URI_MATCHER.match(uri)){
            case ITEM:
                selection = NotesContract.Notes._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                int deleteRows = database.delete(
                        NotesContract.Notes.TABLE_NAME,
                        selection,
                        selectionArgs);
                getContext().getContentResolver().notifyChange(uri,null);
                return deleteRows;

        }
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase database = notesDbHelper.getWritableDatabase();

        switch (URI_MATCHER.match(uri)){
            case ITEM:
                selection = NotesContract.Notes._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                int rowsUpdated = database.update(
                        NotesContract.Notes.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);
                getContext().getContentResolver().notifyChange(uri,null);
                return rowsUpdated;

        }

        return 0;
    }
}

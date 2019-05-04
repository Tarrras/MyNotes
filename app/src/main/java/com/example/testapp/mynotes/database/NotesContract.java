package com.example.testapp.mynotes.database;

import android.net.Uri;
import android.provider.BaseColumns;

public final class NotesContract {

    public static final String DB_NAME = "savedNews";
    public static final int DB_VERSION = 1;

    public static final String SCHEME = "content://";
    public static final String AUTHORITY = "com.example.testapp.mynotes";
    public static final String PATH = "notes";

    public static final Uri BASE_CONTENT_URI = Uri.parse(SCHEME + AUTHORITY);
    public static final String[] CREATE_DATABASE_QUERIES = {
            Notes.CREATE_TABLE,
            Notes.CREATE_UPDATED_TS_INDEX
    };

    public static final class Notes implements BaseColumns{
        public static final String TABLE_NAME = "notes";

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI,PATH);

        public static final String COLUMN_NOTE = "note";
        public static final String COLUMN_CREATED_TS = "created_ts";
        public static final String COLUMN_UPDATED_TS = "updated_ts";

        public static final String CREATE_TABLE = String.format("CREATE TABLE %s " +
                        "(%s INTEGER PRIMARY KEY, " +
                        "%s TEXT NOT NULL, " +
                        "%s INTEGER NOT NULL, " +
                        "%s INTEGER NOT NULL);",
                TABLE_NAME,
                _ID,
                COLUMN_NOTE,
                COLUMN_CREATED_TS,
                COLUMN_UPDATED_TS);

        public static final String CREATE_UPDATED_TS_INDEX = String.format("CREATE INDEX updated_ts_index " +
                        "ON %s (%s);",
                TABLE_NAME,
                COLUMN_UPDATED_TS);

        public static final String CREATE_SAVED_INDEX = String.format("CREATE INDEX saved_index " +
                        "ON %s (%s);",
                TABLE_NAME,
                COLUMN_CREATED_TS);


        public static final String[] LIST_PROJECTION = {
                _ID,
                COLUMN_NOTE,
                COLUMN_CREATED_TS,
                COLUMN_UPDATED_TS
        };

        public static final String URI_TYPE_NOTE_DIR = "vnd.android.cursor.dir/vnd.example.testapp.mynotes";

        public static final String URI_TYPE_NOTE_ITEM = "vnd.android.cursor.item/vnd.example.testapp.mynotes";

    }
}

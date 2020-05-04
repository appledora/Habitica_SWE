package com.tgc.appledora.habitica;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class app_database {

    private Context ctx;

    public app_database(Context ctx) {
        this.ctx = ctx;
    }

    public static abstract class tb_Struct implements BaseColumns {

        public static final String TABLE_NAME = "life_reminders";
        public static final String KEY_ROW_ID = "_id";
        public static final String KEY_TITLE = "rem_title";
        public static final String KEY_BODY = "rem_body";
        public static final String KEY_CREATED_DATE = "created_date_time";
        public static final String KEY_SCHEDULED_DATE = "scheduled_date_time";
        public static final String KEY_SCHEDULED_DATE_IN_WORDS = "scheduled_date_time_words";
        public static final String KEY_CURRENT_TIME = "current_time_data_only";
        public static final String KEY_EXPIRED = "has_expired";
        public static final String KEY_COLOR = "reminder_color";
        public static final String KEY_FAVOURITE = "reminder_favourites";
        public static final String KEY_PRIORITY = "reminder_flag";
        public static final String KEY_REPEAT = "reminder_repeat";
        public static final String KEY_IMAGE_URI = "reminder_icon_uri";
        public static final String KEY_IMAGE_PATH = "reminder_icon_path";
    }

    private final static String CREATE_TABLE = "create table " + tb_Struct.TABLE_NAME + " (" + tb_Struct.KEY_ROW_ID + " integer primary key autoincrement, "
            + tb_Struct.KEY_TITLE + " text not null, " + tb_Struct.KEY_BODY + " text not null, " + tb_Struct.KEY_CREATED_DATE + " text not null, " + tb_Struct.KEY_SCHEDULED_DATE
            + " text not null, " + tb_Struct.KEY_SCHEDULED_DATE_IN_WORDS + " text not null, " + tb_Struct.KEY_CURRENT_TIME + " text not null, " + tb_Struct.KEY_EXPIRED + " text not null, "
            + tb_Struct.KEY_COLOR + " integer not null, " + tb_Struct.KEY_FAVOURITE + " text not null, " + tb_Struct.KEY_REPEAT + " integer not null, " + tb_Struct.KEY_IMAGE_URI
            + " text not null, " + tb_Struct.KEY_IMAGE_PATH + " text not null, " + tb_Struct.KEY_PRIORITY + " integer not null )";

    private final static String DROP_TABLE = "drop table if exists " + tb_Struct.TABLE_NAME;
    private SQLiteDatabase dbSQL;
    private DatabaseHelper dbHelper;

    public class DatabaseHelper extends SQLiteOpenHelper {

        public static final String DATABASE_NAME = "life_reminder_epic.db";
        public static final int DATABASE_VERSION = 40;

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(DROP_TABLE);
            onCreate(db);
        }

        @Override
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
        }
    }

    public app_database open() throws SQLException {

        dbHelper = new DatabaseHelper(ctx);
        dbSQL = dbHelper.getWritableDatabase();

        return this;
    }

    public void close() {
        dbSQL.close();
        dbHelper.close();

    }

    public long insertDb(String title, String body, String createdDate, String scheduledDate, String scheduledDateInWords, String currentTime, String hasExpired, int setColor, String favourite, int priority, int repeat, String imageUri, String imagePath) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(tb_Struct.KEY_TITLE, title);
        initialValues.put(tb_Struct.KEY_BODY, body);
        initialValues.put(tb_Struct.KEY_CREATED_DATE, createdDate);
        initialValues.put(tb_Struct.KEY_SCHEDULED_DATE, scheduledDate);
        initialValues.put(tb_Struct.KEY_SCHEDULED_DATE_IN_WORDS, scheduledDateInWords);
        initialValues.put(tb_Struct.KEY_CURRENT_TIME, currentTime);
        initialValues.put(tb_Struct.KEY_EXPIRED, hasExpired);
        initialValues.put(tb_Struct.KEY_COLOR, setColor);
        initialValues.put(tb_Struct.KEY_FAVOURITE, favourite);
        initialValues.put(tb_Struct.KEY_PRIORITY, priority);
        initialValues.put(tb_Struct.KEY_REPEAT, repeat);
        initialValues.put(tb_Struct.KEY_IMAGE_URI, imageUri);
        initialValues.put(tb_Struct.KEY_IMAGE_PATH, imagePath);

        return dbSQL.insert(tb_Struct.TABLE_NAME, null, initialValues);
    }

    public boolean updateDb(long rowId, String title, String body, String createdDate, String scheduledDate, String scheduledDateInWords, String currentTime, String hasExpired, int setColor, String favourite, int priority, int repeat, String imageUri, String imagePath) {

        ContentValues updateValues = new ContentValues();
        updateValues.put(tb_Struct.KEY_TITLE, title);
        updateValues.put(tb_Struct.KEY_BODY, body);
        updateValues.put(tb_Struct.KEY_CREATED_DATE, createdDate);
        updateValues.put(tb_Struct.KEY_SCHEDULED_DATE, scheduledDate);
        updateValues.put(tb_Struct.KEY_SCHEDULED_DATE_IN_WORDS, scheduledDateInWords);
        updateValues.put(tb_Struct.KEY_CURRENT_TIME, currentTime);
        updateValues.put(tb_Struct.KEY_EXPIRED, hasExpired);
        updateValues.put(tb_Struct.KEY_COLOR, setColor);
        updateValues.put(tb_Struct.KEY_FAVOURITE, favourite);
        updateValues.put(tb_Struct.KEY_PRIORITY, priority);
        updateValues.put(tb_Struct.KEY_REPEAT, repeat);
        updateValues.put(tb_Struct.KEY_IMAGE_URI, imageUri);
        updateValues.put(tb_Struct.KEY_IMAGE_PATH, imagePath);

        return dbSQL.update(tb_Struct.TABLE_NAME, updateValues, tb_Struct.KEY_ROW_ID + " = " + rowId, null) > 0;
    }

    public boolean deleteDb(String rowId) {
        return dbSQL.delete(tb_Struct.TABLE_NAME, rowId, null) > 0;
    }


    boolean updateDbCompleted(long rowId, String completedB) {

        ContentValues values = new ContentValues();
        values.put(tb_Struct.KEY_EXPIRED, completedB);

        return dbSQL.update(tb_Struct.TABLE_NAME, values, tb_Struct.KEY_ROW_ID + " = " + rowId, null) > 0;

    }

    public boolean updateTimeOnlyDb(long rowId, String scheduledDate) {

        ContentValues updateValues = new ContentValues();
        updateValues.put(tb_Struct.KEY_SCHEDULED_DATE, scheduledDate);

        return dbSQL.update(tb_Struct.TABLE_NAME, updateValues, tb_Struct.KEY_ROW_ID + " = " + rowId, null) > 0;
    }

    public Cursor readDb(String selection, String selectionArgs[], String sortOrder) {

        Cursor cursor = dbSQL.query(tb_Struct.TABLE_NAME, new String[]{tb_Struct.KEY_ROW_ID, tb_Struct.KEY_TITLE, tb_Struct.KEY_BODY, tb_Struct.KEY_CREATED_DATE, tb_Struct.KEY_SCHEDULED_DATE,
                tb_Struct.KEY_SCHEDULED_DATE_IN_WORDS, tb_Struct.KEY_CURRENT_TIME, tb_Struct.KEY_EXPIRED, tb_Struct.KEY_COLOR, tb_Struct.KEY_FAVOURITE, tb_Struct.KEY_PRIORITY, tb_Struct.KEY_REPEAT, tb_Struct.KEY_IMAGE_URI, tb_Struct.KEY_IMAGE_PATH}, selection, selectionArgs, null, null, sortOrder, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public boolean updateFavOnlyDb(long rowId, String fav) {

        ContentValues updateValues = new ContentValues();
        updateValues.put(tb_Struct.KEY_FAVOURITE, fav);

        return dbSQL.update(tb_Struct.TABLE_NAME, updateValues, tb_Struct.KEY_ROW_ID + " = " + rowId, null) > 0;
    }
}

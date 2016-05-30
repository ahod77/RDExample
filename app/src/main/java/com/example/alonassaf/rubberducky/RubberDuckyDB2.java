package com.example.alonassaf.rubberducky;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by AlonAssaf on 5/28/2016.
 */
public final class RubberDuckyDB2 {

    public static final String DB_NAME = "rubberducky.db";
    public static final int DB_VERSION = 1;

    // private member
    private static SQLiteDatabase connection;

    //private constructor
    private RubberDuckyDB2() {
        //Prevents instantiation
    }

    public static void connect(Context context) {
        DBHelper dbHelper = new DBHelper(context, DB_NAME, null, DB_VERSION);
        connection = dbHelper.getWritableDatabase();
    }

    public static void populate() {
        //Populates settings table
        Settings.set(new Setting("userId", 1000));
        Settings.set(new Setting("userName", "Asaf Hod"));
        Settings.set(new Setting("pinnedPanes", new long[] { -1, -2, -3, 4 }));
    }

    private static class DBHelper extends SQLiteOpenHelper {
        public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Settings.initialize(db);

            //db.execSQL(CREATE_ENTITY_TABLE);

            // db.execSQL(CREATE_ACTIVITY_TABLE);
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.d("Rubber Ducky", "Upgrading db from version " + oldVersion + " to " + newVersion);

            Settings.uninitialize(db);
            // db.execSQL(RubberDuckyDB.DROP_ENTITY_TABLE);
            // db.execSQL(RubberDuckyDB.DROP_ACTIVITY_TABLE);

            onCreate(db);
        }
    }

    public class Entities{
        public class Entity{

        }
    }

    public class Activities{
        public class Activity{

        }
    }

    public static class Settings{

        public static final String SETTINGS_TABLE = "settings";
        public static final String SETTINGS_ID = "id";
        public static final int SETTINGS_ID_COL = 0;

        public static final String SETTINGS_KEY = "key";
        public static final int SETTINGS_KEY_COL = 1;

        public static final String SETTINGS_VALUE = "value";
        public static final int SETTINGS_VALUE_COL = 2;

        public static final String CREATE_SETTINGS_TABLE =
                "CREATE TABLE " + SETTINGS_TABLE + " (" +
                        SETTINGS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        SETTINGS_KEY + " TEXT NOT NULL, " +
                        SETTINGS_VALUE + " TEXT NOT NULL);" //Allow null?
                ;

        public static final String DROP_SETTINGS_TABLE =
                "DROP TABLE IF EXISTS " + SETTINGS_TABLE;

        public static void initialize(SQLiteDatabase _conn) {
            _conn.execSQL(CREATE_SETTINGS_TABLE);
        }

        public static void uninitialize(SQLiteDatabase _conn) {
            _conn.execSQL(RubberDuckyDB.DROP_SETTINGS_TABLE);
        }

        public static Setting get(String key) {
            String where = SETTINGS_KEY + "= ?";
            String[] whereArgs = { key };

            Cursor cursor = connection.query(SETTINGS_TABLE, null, where, whereArgs, null, null, null);
            cursor.moveToFirst();
            if (cursor.getCount() == 0)
                return null;
            else {
                Setting s = new Setting(cursor.getLong(SETTINGS_ID_COL), key).setData(cursor.getString(SETTINGS_VALUE_COL));
                cursor.close();
                s.markUnDirty();
                return s;
            }
        }

        public static long set(Setting s) {
            ContentValues cv = new ContentValues();
            cv.put(SETTINGS_KEY, s.getKey());
            cv.put(SETTINGS_VALUE, s.getData());

            if (s.isNew())
                return s.setRowId(connection.insertOrThrow(SETTINGS_TABLE, null, cv));
            else if (s.isDirty())
                return s.markSaved(connection.update(SETTINGS_TABLE, cv, SETTINGS_ID + "= ?", new String[] { Long.toString(s.getRowId()) }));
            else
                return s.getRowId();
        }
    }
}
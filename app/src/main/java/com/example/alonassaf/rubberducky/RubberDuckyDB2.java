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
    public static final int DB_VERSION = 2;

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

    public void clear() {
        int deleteEntityCount = Entities.deleteAll();
        int deleteActivityCount = Activities.deleteAll();
        int deleteSettingsCount = Settings.deleteAll();

        //Test
        StringBuilder sb = new StringBuilder();
        sb.append("Entity table cleared! Delete count: " + deleteEntityCount + "\n\n");
        sb.append("Activity table cleared! Delete count: " + deleteActivityCount + "\n\n");
        sb.append("Settings table cleared! Delete count: " + deleteSettingsCount + "\n\n");

        Log.d("Rubber Ducky", sb.toString());
    }

    public static void populate() {
        Entities.set(new Entity(-1, 0, "Actors", "All Actors"));
        Entities.set(new Entity(-2, 0, "Actions", "All Actions"));
        Entities.set(new Entity(-3, 0, "Activities", "All Activities"));
        long asafID = Entities.set(new Entity(1, "Asaf", "Asaf Hod"));
        long repHoursID = Entities.set(new Entity(2, "Report Hours", "Reports hours worked"));
        long billTimeID = Entities.set(new Entity(2, "Bill Time", "Bills time worked"));
        long rubberDuckyID = Entities.set(new Entity(3, "Rubber Ducky", "Current project"));

        // Populates settings table
        Settings.set(new Setting("userId", asafID));
        Settings.set(new Setting("userName", "Asaf Hod"));
        Settings.set(new Setting("pinnedPanes", new long[] { -1, -2, -3, rubberDuckyID }));
    }

    private static class DBHelper extends SQLiteOpenHelper {
        public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Settings.initialize(db);
            Entities.initialize(db);
            Activities.initialize(db);
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.d("Rubber Ducky", "Upgrading db from version " + oldVersion + " to " + newVersion);

            Settings.uninitialize(db);
            Entities.uninitialize(db);
            Activities.uninitialize(db);

            onCreate(db);
        }
    }

    public static class Entities {

        public static final String ENTITY_TABLE = "entity";

        public static final String ENTITY_ID = "id";
        public static final int ENTITY_ID_COL = 0;

        public static final String ENTITY_TYPE = "type";
        public static final int ENTITY_TYPE_COL = 1;

        public static final String ENTITY_NAME = "name";
        public static final int ENTITY_NAME_COL = 2;

        public static final String ENTITY_DESC = "desc";
        public static final int ENTITY_DESC_COL = 3;

        public static final String CREATE_ENTITY_TABLE =
                "CREATE TABLE " + ENTITY_TABLE + " (" +
                        ENTITY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        ENTITY_TYPE + " INTEGER NOT NULL, " +
                        ENTITY_NAME + " TEXT NOT NULL, " +
                        ENTITY_DESC + " TEXT);"
                ;

        public static final String DROP_ENTITY_TABLE =
                "DROP TABLE IF EXISTS " + ENTITY_TABLE;

        public static void initialize(SQLiteDatabase _conn) {
            _conn.execSQL(CREATE_ENTITY_TABLE);
        }

        public static void uninitialize(SQLiteDatabase _conn) {
            _conn.execSQL(DROP_ENTITY_TABLE);
        }

        public static Entity get(long id) {
            Cursor cursor = connection.query(ENTITY_TABLE, null, ENTITY_ID + "= ?", new String[] { Long.toString(id) }, null, null, null);
            cursor.moveToFirst();
            if (cursor.getCount() == 0)
                return null;
            else {
                Entity e = new Entity(id,
                                      cursor.getInt(ENTITY_TYPE_COL),
                                      cursor.getString(ENTITY_NAME_COL),
                                      cursor.getString(ENTITY_DESC_COL));
                cursor.close();
                e.markUnDirty();
                return e;
            }
        }

        public static long set(Entity e) {
            ContentValues cv = new ContentValues();
            cv.put(ENTITY_TYPE, e.getType());
            cv.put(ENTITY_NAME, e.getName());
            cv.put(ENTITY_DESC, e.getDesc());

            if (e.isNew())
                return e.setRowId(connection.insertOrThrow(ENTITY_TABLE, null, cv));
            else if (e.isDirty())
                return e.markSaved(connection.update(ENTITY_TABLE, cv, ENTITY_ID + "= ?", new String[] { Long.toString(e.getRowId()) }));
            else
                return e.getRowId();
        }

        public static int deleteAll() {
            return connection.delete(ENTITY_TABLE, null, null);
        }
    }

    public static class Activities{

        public static final String ACTIVITY_TABLE = "activity";

        public static final String ACTIVITY_ID = "id";
        public static final int ACTIVITY_ID_COL = 0;

        public static final String ACTIVITY_TIMESTAMP = "timestamp";
        public static final int ACTIVITY_TIMESTAMP_COL = 1;

        public static final String ACTIVITY_CREATOR = "creator";
        public static final int ACTIVITY_CREATOR_COL = 2;

        public static final String ACTIVITY_ACTOR = "container";
        public static final int ACTIVITY_ACTOR_COL = 3;

        public static final String ACTIVITY_ACTION = "action";
        public static final int ACTIVITY_ACTION_COL = 4;

        public static final String CREATE_ACTIVITY_TABLE =
                "CREATE TABLE " + ACTIVITY_TABLE + " (" +
                        ACTIVITY_ID          + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        ACTIVITY_TIMESTAMP   + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL, " +
                        ACTIVITY_CREATOR     + " INTEGER, " +               //References entity table
                        ACTIVITY_ACTOR       + " INTEGER NOT NULL, " +      //""
                        ACTIVITY_ACTION      + " INTEGER NOT NULL);"        //""
                ;

        public static final String DROP_ACTIVITY_TABLE =
                "DROP TABLE IF EXISTS " + ACTIVITY_TABLE;

        public static void initialize(SQLiteDatabase _conn) {
            _conn.execSQL(CREATE_ACTIVITY_TABLE);
        }

        public static void uninitialize(SQLiteDatabase _conn) {
            _conn.execSQL(DROP_ACTIVITY_TABLE);
        }

        public static Activity get(long id) {
            Cursor cursor = connection.query(ENTITY_TABLE, null, ENTITY_ID + "= ?", new String[] { Long.toString(id) }, null, null, null);
            cursor.moveToFirst();
            if (cursor.getCount() == 0)
                return null;
            else {
                Activity a = new Entity(id,
                        cursor.getInt(ENTITY_TYPE_COL),
                        cursor.getString(ENTITY_NAME_COL),
                        cursor.getString(ENTITY_DESC_COL));
                cursor.close();
                a.markUnDirty();
                return a;
            }
        }

        public static long set(Activity a) {
            ContentValues cv = new ContentValues();
            cv.put(ENTITY_TYPE, e.getType());
            cv.put(ENTITY_NAME, e.getName());
            cv.put(ENTITY_DESC, e.getDesc());

            if (a.isNew())
                return a.setRowId(connection.insertOrThrow(ENTITY_TABLE, null, cv));
            else if (a.isDirty())
                return a.markSaved(connection.update(ENTITY_TABLE, cv, ENTITY_ID + "= ?", new String[] { Long.toString(e.getRowId()) }));
            else
                return a.getRowId();
        }

        public static int deleteAll() {
            return connection.delete(ACTIVITY_TABLE, null, null);
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
            Cursor cursor = connection.query(SETTINGS_TABLE, null, SETTINGS_KEY + "= ?", new String[] { key }, null, null, null);
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

        public static int deleteAll() {
            return connection.delete(SETTINGS_TABLE, null, null);
        }
    }
}
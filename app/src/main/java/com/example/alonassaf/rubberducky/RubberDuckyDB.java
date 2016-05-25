package com.example.alonassaf.rubberducky;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;

/**
 * Created by AlonAssaf on 5/15/2016.
 */
public class RubberDuckyDB {

    // database constants
    public static final String DB_NAME = "rubberducky.db";
    public static final int DB_VERSION = 1;

    // entity table constants
    public static final String ENTITY_TABLE = "entity";
    public static final String ACTIVITY_TABLE = "activity";
    public static final String SETTINGS_TABLE = "settings";

    public static final String ENTITY_ID = "id";
    public static final int ENTITY_ID_COL = 0;

    public static final String ENTITY_TYPE = "type";
    public static final int ENTITY_TYPE_COL = 1;

    public static final String ENTITY_NAME = "name";
    public static final int ENTITY_NAME_COL = 2;

    public static final String ENTITY_DESC = "desc";
    public static final int ENTITY_DESC_COL = 3;

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

    public static final String SETTINGS_ID = "id";
    public static final int SETTINGS_ID_COL = 0;

    public static final String SETTINGS_KEY = "key";
    public static final int SETTINGS_KEY_COL = 1;

    public static final String SETTINGS_VALUE = "value";
    public static final int SETTINGS_VALUE_COL = 2;

    // CREATE and DROP TABLE statements
    public static final String CREATE_ENTITY_TABLE =
            "CREATE TABLE " + ENTITY_TABLE + " (" +
                    ENTITY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    ENTITY_TYPE + " TEXT NOT NULL, " +
                    ENTITY_NAME + " TEXT NOT NULL, " +
                    ENTITY_DESC + " TEXT);"
            ;

    public static final String DROP_ENTITY_TABLE =
            "DROP TABLE IF EXISTS " + ENTITY_TABLE;

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

    public static final String CREATE_SETTINGS_TABLE =
            "CREATE TABLE " + SETTINGS_TABLE + " (" +
                    SETTINGS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    SETTINGS_KEY + " TEXT NOT NULL, " +
                    SETTINGS_VALUE + " TEXT NOT NULL);" //Allow null?
            ;

    public static final String DROP_SETTINGS_TABLE =
            "DROP TABLE IF EXISTS " + SETTINGS_TABLE;

    private static class DBHelper extends SQLiteOpenHelper {
        public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_ENTITY_TABLE);

            db.execSQL(CREATE_ACTIVITY_TABLE);

            db.execSQL(CREATE_SETTINGS_TABLE);
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.d("Rubber Ducky", "Upgrading db from version " + oldVersion + " to " + newVersion);

            db.execSQL(RubberDuckyDB.DROP_ENTITY_TABLE);
            db.execSQL(RubberDuckyDB.DROP_ACTIVITY_TABLE);
            db.execSQL(RubberDuckyDB.DROP_SETTINGS_TABLE);
            onCreate(db);
        }
    }

    //database object and database helper object
    private SQLiteDatabase db;
    private DBHelper dbHelper;

    //constructor
    public RubberDuckyDB(Context context) {
        dbHelper = new DBHelper(context, DB_NAME, null, DB_VERSION);
    }

    //private methods
    private void openReadableDB() {
        db = dbHelper.getReadableDatabase();
    }

    private void openWritableDB() {
        db = dbHelper.getWritableDatabase();
    }

    private void closeDB() {
        if (db != null)
            db.close();
    }

    //Add method(s) to retrieve multiple rows, perhaps based on entity_type

    //Retrieves single row from entity table
    public Entity getEntity(int id) {
        String where = ENTITY_ID + "= ?";
        String[] whereArgs = {Integer.toString(id)};

        this.openReadableDB();
        Cursor cursor = db.query(ENTITY_TABLE, null, where, whereArgs, null, null, null);
        cursor.moveToFirst();
        Entity entity = getEntityFromCursor(cursor);
        if (cursor != null)
            cursor.close();
        this.closeDB();

        return entity;
    }

    //Gets entity from cursor
    private static Entity getEntityFromCursor(Cursor cursor) {
        if (cursor == null || cursor.getCount() == 0) {
            return null;
        } else {
            try {
                Entity entity = new Entity(
                        cursor.getInt(ENTITY_ID_COL),
                        cursor.getString(ENTITY_TYPE_COL),
                        cursor.getString(ENTITY_NAME_COL),
                        cursor.getString(ENTITY_DESC_COL));
                return entity;
            } catch (Exception e) {
                return null;
            }
        }
    }

    //Retrieves single row from activity table
    public Activity getActivity(int id){
        String where = ACTIVITY_ID + "= ?";
        String[] whereArgs = { Integer.toString(id) };

        this.openReadableDB();
        Cursor cursor = db.query(ACTIVITY_TABLE, null, where, whereArgs, null, null, null);
        cursor.moveToFirst();
        Activity activity = getActivityFromCursor(cursor);
        if (cursor != null)
            cursor.close();
        this.closeDB();

        return activity;
    }

    //Gets activity with key IDs from cursor
    private static Activity getActivityFromCursor(Cursor cursor){
        if (cursor == null || cursor.getCount() == 0){
            return null;
        }
        else {
            try {
                Activity activity = new Activity(
                        cursor.getInt(ACTIVITY_ID_COL),
                        Timestamp.valueOf(cursor.getString(ACTIVITY_TIMESTAMP_COL)),
                        cursor.getInt(ACTIVITY_CREATOR_COL),
                        cursor.getInt(ACTIVITY_ACTOR_COL),
                        cursor.getInt(ACTIVITY_ACTION_COL));
                return activity;
            }
            catch(Exception e){
                return null;
            }
        }
    }

    //Retrieves single row from settings table as JSON object
    public JSONObject getSetting(String key) {
        String where = SETTINGS_KEY + "= ?";
        String[] whereArgs = { key };

        this.openReadableDB();
        Cursor cursor = db.query(SETTINGS_TABLE, null, where, whereArgs, null, null, null);
        cursor.moveToFirst();
        JSONObject setting = getSettingFromCursor(cursor);
        if (cursor != null)
            cursor.close();
        this.closeDB();

        return setting;
    }

    //Gets setting from cursor as JSON object
    private static JSONObject getSettingFromCursor(Cursor cursor) {
        if (cursor == null || cursor.getCount() == 0) {
            return null;
        } else {
            try {
                JSONObject setting = new JSONObject(        //Creates JSON object from string in db
                        cursor.getString(SETTINGS_VALUE_COL));
                return setting;
            } catch (Exception e) {
                return null;
            }
        }
    }

    //Inserts Entity
    public long insertEntity(Entity entity) {
        ContentValues cv = new ContentValues();
        cv.put(ENTITY_TYPE, entity.getType());
        cv.put(ENTITY_NAME, entity.getName());
        cv.put(ENTITY_DESC, entity.getDesc());

        this.openWritableDB();
        long rowID = db.insert(ENTITY_TABLE, null, cv);
        this.closeDB();

        return rowID;
    }

    //Inserts Activity with key ID instance variables
    public long insertActivity(Activity activity){
        ContentValues cv = new ContentValues();
        if (activity.getTimestamp() != null) {
            cv.put(ACTIVITY_TIMESTAMP, activity.getTimestamp().toString()); //If null, inserts current timestamp
        }
        cv.put(ACTIVITY_CREATOR, activity.getCreator_id());
        cv.put(ACTIVITY_ACTOR, activity.getContainer_id());
        cv.put(ACTIVITY_ACTION, activity.getAction_id());

        this.openWritableDB();
        long rowID = db.insert(ACTIVITY_TABLE, null, cv);
        this.closeDB();

        return rowID;
    }

    //Inserts Setting from JSON object
    public long insertSetting(String key, JSONObject setting) {
        ContentValues cv = new ContentValues();
        cv.put(SETTINGS_KEY, key);
        cv.put(SETTINGS_VALUE, setting.toString()); //Converts JSON Object to string for db insertion

        this.openWritableDB();
        long rowID = db.insert(SETTINGS_TABLE, null, cv);
        this.closeDB();

        return rowID;
    }

    //Updates Entity by id
    public long updateEntity(Entity entity) {
        ContentValues cv = new ContentValues();
        cv.put(ENTITY_TYPE, entity.getType());
        cv.put(ENTITY_NAME, entity.getName());
        cv.put(ENTITY_DESC, entity.getDesc());

        String where = ENTITY_ID + "= ?";
        String[] whereArgs = {String.valueOf(entity.getId())};

        this.openWritableDB();
        int rowCount = db.update(ENTITY_TABLE, cv, where, whereArgs);
        this.closeDB();

        return rowCount;
    }

    //Updates Activity by id and using key ID instance variables
    public long updateActivity(Activity activity) {
        ContentValues cv = new ContentValues();
        cv.put(ACTIVITY_TIMESTAMP, activity.getTimestamp().toString());
        cv.put(ACTIVITY_CREATOR, activity.getCreator_id());
        cv.put(ACTIVITY_ACTOR, activity.getContainer_id());
        cv.put(ACTIVITY_ACTION, activity.getAction_id());

        String where = ACTIVITY_ID + "= ?";
        String[] whereArgs = {String.valueOf(activity.getId())};

        this.openWritableDB();
        int rowCount = db.update(ACTIVITY_TABLE, cv, where, whereArgs);
        this.closeDB();

        return rowCount;
    }

    //Updates Setting by key
    public long updateSetting(String key, JSONObject setting) {
        ContentValues cv = new ContentValues();
        cv.put(SETTINGS_VALUE, setting.toString()); //Converts JSON object to string for db update

        String where = key + "= ?";
        String[] whereArgs = { key };

        this.openWritableDB();
        int rowCount = db.update(SETTINGS_TABLE, cv, where, whereArgs);
        this.closeDB();

        return rowCount;
    }

    //Deletes an entity
    public int deleteEntity(long id) {
        String where = ENTITY_ID + "= ?";
        String[] whereArgs = {String.valueOf(id)};

        this.openWritableDB();
        int rowCount = db.delete(ENTITY_TABLE, where, whereArgs);
        this.closeDB();

        return rowCount;
    }

    //Deletes an activity
    public int deleteActivity(long id) {
        String where = ACTIVITY_ID + "= ?";
        String[] whereArgs = {String.valueOf(id)};

        this.openWritableDB();
        int rowCount = db.delete(ACTIVITY_TABLE, where, whereArgs);
        this.closeDB();

        return rowCount;
    }

    //Deletes a setting
    public int deleteSetting(long id) {
        String where = SETTINGS_ID + "= ?";
        String[] whereArgs = {String.valueOf(id)};

        this.openWritableDB();
        int rowCount = db.delete(SETTINGS_TABLE, where, whereArgs);
        this.closeDB();

        return rowCount;
    }

    //Deletes all Entities
    public int deleteAllEntities() {
        this.openWritableDB();

        //Deletes all rows in entity table
        int rowCount = db.delete(ENTITY_TABLE, null, null);

        //Resets index auto-increment
        db.execSQL("DELETE FROM sqlite_sequence WHERE name='" + ENTITY_TABLE + "';");

        this.closeDB();

        return rowCount;
    }

    //Deletes all Activities
    public int deleteAllActivities() {
        this.openWritableDB();

        //Deletes all rows in activity table
        int rowCount = db.delete(ACTIVITY_TABLE, null, null);

        //Resets index auto-increment
        db.execSQL("DELETE FROM sqlite_sequence WHERE name='" + ACTIVITY_TABLE + "';");

        this.closeDB();

        return rowCount;
    }

    //Deletes all Settings
    public int deleteAllSettings() {
        this.openWritableDB();

        //Deletes all rows in settings table
        int rowCount = db.delete(SETTINGS_TABLE, null, null);

        //Resets index auto-increment
        db.execSQL("DELETE FROM sqlite_sequence WHERE name='" + SETTINGS_TABLE + "';");

        this.closeDB();

        return rowCount;
    }

    private StringBuilder sb = new StringBuilder(); //For test

    public void initializeDB(){
        Entity entity = new Entity("Actor", "Asaf", "Building Rubber Ducky!");
        long insertId = insertEntity(entity);

        //For test
        if (insertId > 0) {
            sb.append("Entity row inserted: Insert Id: " + insertId + "\n");
        }

        Entity entity2 = new Entity("Actor", "Alon","");
        insertId = insertEntity(entity2);

        //For test
        if (insertId > 0) {
            sb.append("Entity row inserted: Insert Id: " + insertId + "\n");
        }

        Activity activity = new Activity(null, 0, 1, 3);
        insertId = insertActivity(activity);

        //For test
        if (insertId > 0) {
            sb.append("Activity row inserted: Insert Id: " + insertId + "\n");
        }

        Activity activity2 = new Activity(null, 0, 2, 4);
        insertId = insertActivity(activity2);

        //For test
        if (insertId > 0) {
            sb.append("Activity row inserted: Insert Id: " + insertId + "\n");
        }

        //----------------------------------------------------------------
        JSONObject setting = new JSONObject();
        JSONArray arr = new JSONArray();
        arr.put(-1); arr.put(-2); arr.put(-3); arr.put("RubberDucky");
        try {
            setting.put("Panes Pinned", arr);
        }
        catch (JSONException e){
            e.printStackTrace();
        }

        //------------------------------------------------------------------

        //Test
        Log.d("Rubber Ducky", sb.toString());
    }

    public void clearDB(){
        int deleteEntityCount = deleteAllEntities();
        int deleteActivityCount = deleteAllActivities();
        int deleteSettingsCount = deleteAllSettings();

        //Test
        sb.setLength(0); //Clears string builder
        sb.append("Entity table cleared! Delete count: " + deleteEntityCount + "\n\n");
        sb.append("Activity table cleared! Delete count: " + deleteActivityCount + "\n\n");
        sb.append("Settings table cleared! Delete count: " + deleteSettingsCount + "\n\n");
    }

}
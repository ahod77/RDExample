package com.example.alonassaf.rubberducky;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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

    public static final String ENTITY_ID = "_id";
    public static final int ENTITY_ID_COL = 0;

    public static final String ENTITY_TYPE = "entity_type";
    public static final int ENTITY_TYPE_COL = 1;

    public static final String ENTITY_NAME = "entity_name";
    public static final int ENTITY_NAME_COL = 2;

    public static final String ENTITY_DESC = "entity_desc";
    public static final int ENTITY_DESC_COL = 3;

    public static final String ACTIVITY_ID = "_id";
    public static final int ACTIVITY_ID_COL = 0;

    public static final String ACTIVITY_TIMESTAMP = "activity_timestamp";
    public static final int ACTIVITY_TIMESTAMP_COL = 1;

    public static final String ACTIVITY_CREATOR = "activity_creator";
    public static final int ACTIVITY_CREATOR_COL = 2;

    public static final String ACTIVITY_ACTOR = "activity_actor";
    public static final int ACTIVITY_ACTOR_COL = 3;

    public static final String ACTIVITY_ACTION = "activity_action";
    public static final int ACTIVITY_ACTION_COL = 4;

    // CREATE and DROP TABLE statements
    public static final String CREATE_ENTITY_TABLE =
            "CREATE TABLE " + ENTITY_TABLE + " (" +
                    ENTITY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    ENTITY_TYPE + " TEXT NOT NULL, " +
                    ENTITY_NAME + " TEXT NOT NULL, " +
                    ENTITY_DESC + " TEXT);";

    public static final String DROP_ENTITY_TABLE =
            "DROP TABLE IF EXISTS " + ENTITY_TABLE;

    /*public static final String CREATE_ACTIVITY_TABLE =
            "CREATE TABLE " + ACTIVITY_TABLE + " (" +
                    ACTIVITY_ID          + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    ACTIVITY_TIMESTAMP   + " DATETIME DEFAULT CURRENT_TIMESTAMP NOT NULL, " +
                    ACTIVITY_CREATOR     + " INTEGER, " +
                    ACTIVITY_ACTOR       + " INTEGER, " +       //NOT NULL?
                    ACTIVITY_ACTION      + " INTEGER, " +       //NOT NULL?
                    "FOREIGN KEY (" + ACTIVITY_CREATOR + ") REFERENCES " +
                        ENTITY_TABLE + "(" + ENTITY_ID + "), " +
                    "FOREIGN KEY (" + ACTIVITY_ACTOR + ") REFERENCES " +
                        ENTITY_TABLE + "(" + ENTITY_ID + "), " +
                    "FOREIGN KEY (" + ACTIVITY_ACTION + ") REFERENCES " +
                        ENTITY_TABLE + "(" + ENTITY_ID + ");";*/

    public static final String DROP_ACTIVITY_TABLE =
            "DROP TABLE IF EXISTS " + ACTIVITY_TABLE;

    private static class DBHelper extends SQLiteOpenHelper {
        public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_ENTITY_TABLE);

            //db.execSQL(CREATE_ACTIVITY_TABLE);

            /*insert sample entities
            db.execSQL("INSERT INTO entity VALUES (1, 'Actor', 'Asaf', 'Building Rubber Ducky!'");
            db.execSQL("INSERT INTO entity VALUES (2, 'Actor', 'Alon',''");*/
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.d("Rubber Ducky", "Upgrading db from version " + oldVersion + " to " + newVersion);

            db.execSQL(RubberDuckyDB.DROP_ENTITY_TABLE);
            //db.execSQL(RubberDuckyDB.DROP_ACTIVITY_TABLE);
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

    private void openWriteableDB() {
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

    //Inserts Entity
    public long insertEntity(Entity entity) {
        ContentValues cv = new ContentValues(); //What about id? Is it only when retrieving?
        cv.put(ENTITY_TYPE, entity.getType());
        cv.put(ENTITY_NAME, entity.getName());
        cv.put(ENTITY_DESC, entity.getDesc());

        this.openWriteableDB();
        long rowID = db.insert(ENTITY_TABLE, null, cv);
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

        this.openWriteableDB();
        int rowCount = db.update(ENTITY_TABLE, cv, where, whereArgs);
        this.closeDB();

        return rowCount;
    }

    //Deletes an entity
    public int deleteEntity(long id) {
        String where = ENTITY_ID + "= ?";
        String[] whereArgs = {String.valueOf(id)};

        this.openWriteableDB();
        int rowCount = db.delete(ENTITY_TABLE, where, whereArgs);
        this.closeDB();

        return rowCount;
    }

    //Deletes an activity
    public int deleteActivity(long id) {
        String where = ACTIVITY_ID + "= ?";
        String[] whereArgs = {String.valueOf(id)};

        this.openWriteableDB();
        int rowCount = db.delete(ACTIVITY_TABLE, where, whereArgs);
        this.closeDB();

        return rowCount;
    }

    //Deletes all Entities
    public int deleteAllEntities() {
        this.openWriteableDB();

        //Deletes all rows in entity table
        int rowCount = db.delete(ENTITY_TABLE, null, null);

        //Resets index auto-increment
        db.execSQL("DELETE FROM sqlite_sequence WHERE name='" + ENTITY_TABLE + "';");

        this.closeDB();

        return rowCount;
    }

    //Deletes all Activities
    public int deleteAllActivities() {
        this.openWriteableDB();

        //Deletes all rows in activity table
        int rowCount = db.delete(ACTIVITY_TABLE, null, null);

        //Resets index auto-increment
        db.execSQL("DELETE FROM sqlite_sequence WHERE name='" + ACTIVITY_TABLE + "';");

        this.closeDB();

        return rowCount;
    }

    /*Retrieves single row from activity table
    public Activity getActivity(int id){
        String where = ACTIVITY_ID + "= ?";
        String[] whereArgs = { Integer.toString(id) };

        this.openReadableDB();
        Cursor cursor = db.query(ACTIVITY_TABLE, null, where, whereArgs, null, null, null);
        cursor.moveToFirst();
        Activity activity = getActivityFromCursor(cursor);  //Issue: reference to incomplete method
        if (cursor != null)
            cursor.close();
        this.closeDB();

        return activity;
    }*/

        /*Gets activity from cursor
    private static Activity getActivityFromCursor(Cursor cursor){
        if (cursor == null || cursor.getCount() == 0){
            return null;
        }
        else {
            try {
                Activity activity = new Activity(
                        cursor.getInt(ACTIVITY_ID_COL),
                        cursor.getString(ACTIVITY_TIMESTAMP_COL),   //Issue: Type mismatches
                        cursor.getInt(ACTIVITY_CREATOR_COL),        //
                        cursor.getInt(ACTIVITY_ACTOR_COL),          //
                        cursor.getInt(ACTIVITY_ACTION_COL));        //
                return activity;
            }
            catch(Exception e){
                return null;
            }
        }
    }*/

       /*Inserts Activity
    public long insertActivity(Activity activity{
        ContentValues cv = new ContentValues(); //What about id? Is it only when retrieving?
        cv.put(ACTIVITY_TIMESTAMP, activity.getTimestamp());    //Issue: Type mismatch
        cv.put(ACTIVITY_CREATOR, activity.getCreator());        //
        cv.put(ACTIVITY_ACTOR, activity.getActor());            //
        cv.put(ACTIVITY_ACTION, activity.getAction());          //

        this.openWriteableDB();
        long rowID = db.insert(ACTIVITY_TABLE, null, cv);
        this.closeDB();

        return rowID;
    }*/

       /*Updates Activity by id
    public long updateActivity(Activity activity) {
        ContentValues cv = new ContentValues();
        cv.put(ACTIVITY_TIMESTAMP, activity.getTimestamp());    //Issue: Type mismatch
        cv.put(ACTIVITY_CREATOR, activity.getCreator());
        cv.put(ACTIVITY_ACTOR, activity.getActor());
        cv.put(ACTIVITY_ACTION, activity.getAction());

        String where = ACTIVITY_ID + "= ?";
        String[] whereArgs = {String.valueOf(activity.getId())};

        this.openWriteableDB();
        int rowCount = db.update(ACTIVITY_TABLE, cv, where, whereArgs);
        this.closeDB();

        return rowCount;
    }*/

}
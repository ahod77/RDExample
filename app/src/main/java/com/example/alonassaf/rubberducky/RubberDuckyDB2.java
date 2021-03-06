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

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by AlonAssaf on 5/28/2016.
 */
public final class RubberDuckyDB2 {

    public static final String DB_NAME = "rubberducky.db";
    public static final int DB_VERSION = 7;

    // private member
    private static SQLiteDatabase connection = null;

    //private constructor
    private RubberDuckyDB2() {
        //Prevents instantiation
    }

    public static void connect(Context context) {
        DBHelper dbHelper = new DBHelper(context, DB_NAME, null, DB_VERSION);
        connection = dbHelper.getWritableDatabase();
    }

    public static void clear() {
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
        Entity actors = new Entity(0, "Actors", "All Actors", null, null);
        Entities.set(actors);
        Entity actions = new Entity(0, "Actions", "All Actions", null, null);
        Entities.set(actions);
        Entity activities = new Entity(0, "Activities", "All Activities", null, null);
        Entities.set(activities);

        /*
        Entities.set(new Entity(-1, 0, "Actors", "All Actors"));
        Entities.set(new Entity(-2, 0, "Actions", "All Actions"));
        Entities.set(new Entity(-3, 0, "Activities", "All Activities"));
        */

        Entity SimplIT = new Entity(1, "SimplIT", "SimplIT Corp", null, null); //Asaf: Change null to BaselineEntityCorp
        Entities.set(SimplIT);

        JSONObject params = new JSONObject();
        try {
            params.put("memberOf", SimplIT.getRowId());
        } catch (JSONException e) {
            params = null;
        }

        Entity Alon = new Entity(1, "Alon", "Alon Hod", null, params); //Asaf: Change null to BaselineEntityPerson
        Entities.set(Alon);
        Entity Asaf = new Entity(1, "Asaf", "Asaf Hod", null, params); //Asaf: Change null to BaselineEntityPerson
        Entities.set(Asaf);

        Entity messageAction =  new Entity(2, "Message", "Free-text message", BaselineActionMessage.class, null);
        Entities.set(messageAction);
        Entity reportHoursAction =  new Entity(2, "Report Hours", "Reports hours worked", BaselineActionReportHours.class, null);
        Entities.set(reportHoursAction);
        Entity billTimeAction =  new Entity(2, "Bill Time", "Bills time worked", BaselineActionBillTime.class, null);
        Entities.set(billTimeAction);


        params = new JSONObject();
        long[] ids = {Alon.getRowId(), Asaf.getRowId() };
        try {
            JSONArray arr = new JSONArray();
            for (long v: ids) { arr.put(v); }
            params.put("participants", arr);
        } catch (JSONException e) {
            params = null;
        }

        Entity rubberDuckyProject = new Entity(3, "Rubber Ducky", "Current project", null, params); //Asaf: Change null to BaselineEntitySoftwareProject
        Entities.set(rubberDuckyProject);

        // Populate Activities table
        Activities.set(new Activity(null, rubberDuckyProject, reportHoursAction, null, 0));
        Activities.set(new Activity(null, rubberDuckyProject, billTimeAction, null, 0));

        params = new JSONObject();
        try {
            params.put("text", "Good Job! Rubber Ducky really looks great. BUT, we need it ready by tomorrow because we have to show it to some potential investors.");
        } catch (JSONException e) {
            params = null;
        }
        Activities.set(new Activity(Alon, rubberDuckyProject, messageAction, params, 1));

        params = new JSONObject();
        try {
            params.put("text", "Thanks, bud!");
        } catch (JSONException e) {
            params = null;
        }
        Activities.set(new Activity(Asaf, rubberDuckyProject, messageAction, params, 0));

        // Populates settings table
        Settings.set(new Setting("userId", Asaf.getRowId()));
        Settings.set(new Setting("userName", "Asaf Hod"));
        Settings.set(new Setting("pinnedPanes", new long[] { actors.getRowId(), actions.getRowId(), activities.getRowId(), rubberDuckyProject.getRowId() }));
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

            // ASAF: please change this some time
            connection = db;
            populate();
            connection = null;
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.d("Rubber Ducky", "Upgrading db from version " + oldVersion + " to " + newVersion);

            Settings.uninitialize(db);
            Entities.uninitialize(db);
            Activities.uninitialize(db);

            onCreate(db);
        }
    }

    public static class Entities {

        public enum Type { UNSPECIFIED /* 0 */, COLLECTION /* 1 */, ACTOR /* 2 */, ACTION /* 3 */}

        public static final String ENTITY_TABLE = "entity";

        public static final String ENTITY_ID = "id";
        public static final int ENTITY_ID_COL = 0;

        public static final String ENTITY_TYPE = "type";
        public static final int ENTITY_TYPE_COL = 1;

        public static final String ENTITY_NAME = "name";
        public static final int ENTITY_NAME_COL = 2;

        public static final String ENTITY_DESC = "desc";
        public static final int ENTITY_DESC_COL = 3;

        public static final String ENTITY_FQCN = "fqcn";
        public static final int ENTITY_FQCN_COL = 4;

        public static final String ENTITY_BADGES = "badges";
        public static final int ENTITY_BADGES_COL = 5;

        public static final String CREATE_ENTITY_TABLE =
                "CREATE TABLE " + ENTITY_TABLE + " (" +
                        ENTITY_ID     + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        ENTITY_TYPE   + " INTEGER NOT NULL, " +
                        ENTITY_NAME   + " TEXT NOT NULL, " +
                        ENTITY_DESC   + " TEXT, " +
                        ENTITY_FQCN   + " TEXT, " + //Asaf: Change this to NOT NULL
                        ENTITY_BADGES + " TEXT);"
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
            Entity e = null;

            Cursor cursor = connection.query(ENTITY_TABLE, null, ENTITY_ID + "= ?", new String[] { Long.toString(id) }, null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    e = loadFromCursor(cursor);
                }

                cursor.close();
            }

            return e;
        }

        public static long set(Entity e) {
            ContentValues cv = new ContentValues();
            cv.put(ENTITY_TYPE,   e.getType());
            cv.put(ENTITY_NAME,   e.getName());
            cv.put(ENTITY_DESC,   e.getDesc());
            cv.put(ENTITY_FQCN,   e.getFQCN());
            cv.put(ENTITY_BADGES, e.getSerializedBadges());

            if (e.isNew())
                return e.setRowId(connection.insertOrThrow(ENTITY_TABLE, null, cv));
            else if (e.isDirty())
                return e.markSaved(connection.update(ENTITY_TABLE, cv, ENTITY_ID + "= ?", new String[] { Long.toString(e.getRowId()) }));
            else
                return e.getRowId();
        }

        public static List<Entity> getAllByType(long type) {
            List<Entity> l = new ArrayList<Entity>();

            Cursor cursor = connection.query(ENTITY_TABLE, null, ENTITY_TYPE + "= ?", new String[] { Long.toString(type) }, null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        l.add(loadFromCursor(cursor));
                    } while (cursor.moveToNext());
                }

                cursor.close();
            }

            return l;
        }

        public static int deleteAll() {
            return connection.delete(ENTITY_TABLE, null, null);
        }

        private static Entity loadFromCursor(Cursor c) { //Add badges
            try {
                String fqcn = c.getString(ENTITY_FQCN_COL);

                String badges_str = c.getString(ENTITY_BADGES_COL);
                JSONObject badges = null;

                try {
                    badges = badges_str == null ? null : new JSONObject(badges_str);
                } catch (JSONException e) {
                    badges = null;
                }

                Entity e = new Entity(c.getLong(ENTITY_ID_COL),
                        c.getInt(ENTITY_TYPE_COL),
                        c.getString(ENTITY_NAME_COL),
                        c.getString(ENTITY_DESC_COL),
                        fqcn == null ? null : Class.forName(fqcn),
                        badges);
                e.markUnDirty();

                return e;
            }
            catch (Exception e) {
                return null;
            }
        }

    }

    public static class Activities {

        public static final String ACTIVITY_TABLE = "activity";

        public static final String ACTIVITY_ID = "id";
        public static final int ACTIVITY_ID_COL = 0;

        public static final String ACTIVITY_TIMESTAMP = "timestamp";
        public static final int ACTIVITY_TIMESTAMP_COL = 1;

        public static final String ACTIVITY_CREATOR = "creator";
        public static final int ACTIVITY_CREATOR_COL = 2;

        public static final String ACTIVITY_CONTAINER = "container";
        public static final int ACTIVITY_CONTAINER_COL = 3;

        public static final String ACTIVITY_ACTION = "action";
        public static final int ACTIVITY_ACTION_COL = 4;

        public static final String ACTIVITY_PARAMS = "params";
        public static final int ACTIVITY_PARAMS_COL = 5;

        public static final String ACTIVITY_STATUS = "status";
        public static final int ACTIVITY_STATUS_COL = 6;

        public static final String CREATE_ACTIVITY_TABLE =
                "CREATE TABLE " + ACTIVITY_TABLE + " (" +
                        ACTIVITY_ID          + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        ACTIVITY_TIMESTAMP   + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL, " +
                        ACTIVITY_CREATOR     + " INTEGER, " +               //References entity table
                        ACTIVITY_CONTAINER   + " INTEGER NOT NULL, " +      //""
                        ACTIVITY_ACTION      + " INTEGER NOT NULL," +       //""
                        ACTIVITY_PARAMS      + " TEXT," +                   // JSON coded
                        ACTIVITY_STATUS      + " INTEGER NOT NULL);"
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
            Activity a = null;

            Cursor cursor = connection.query(ACTIVITY_TABLE, null, ACTIVITY_ID + "= ?", new String[] { Long.toString(id) }, null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    a = loadFromCursor(cursor);
                }

                cursor.close();
            }

            return a;
        }

        public static long set(Activity a) {
            ContentValues cv = new ContentValues();
            cv.put(ACTIVITY_CONTAINER, a.getContainer_id());
            cv.put(ACTIVITY_CREATOR, a.getCreator_id());
            cv.put(ACTIVITY_ACTION, a.getAction_id());
            cv.put(ACTIVITY_PARAMS, a.getSerializedParams());
            cv.put(ACTIVITY_STATUS, a.getStatus());

            if (a.isNew())
                return a.setRowId(connection.insertOrThrow(ACTIVITY_TABLE, null, cv));
            else if (a.isDirty())
                return a.markSaved(connection.update(ACTIVITY_TABLE, cv, ACTIVITY_ID + "= ?", new String[] { Long.toString(a.getRowId()) }));
            else
                return a.getRowId();
        }

        public static List<Activity> getAllByContainer(long container) {
            List<Activity> l = new ArrayList<Activity>();

            Cursor cursor = connection.query(ACTIVITY_TABLE, null, ACTIVITY_CONTAINER + "= ?", new String[] { Long.toString(container) }, null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    do {
                        l.add(loadFromCursor(cursor));
                    } while (cursor.moveToNext());
                }

                cursor.close();
            }

            return l;
        }

        public static int deleteAll() {
            return connection.delete(ACTIVITY_TABLE, null, null);
        }

        private static Activity loadFromCursor(Cursor c) {

            String params_str = c.getString(ACTIVITY_PARAMS_COL);
            JSONObject params = null;

            try {
                params = params_str == null ? null : new JSONObject(params_str);
            } catch (JSONException e) {
                params = null;
            }
            Activity a = new Activity(c.getLong(ACTIVITY_ID_COL),
                                      Timestamp.valueOf(c.getString(ACTIVITY_TIMESTAMP_COL)),
                                      c.getLong(ACTIVITY_CREATOR_COL),
                                      c.getLong(ACTIVITY_CONTAINER_COL),
                                      c.getLong(ACTIVITY_ACTION_COL),
                                      params,
                                      c.getLong(ACTIVITY_STATUS_COL));
            a.markUnDirty();

            return a;
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
                        SETTINGS_VALUE + " TEXT NOT NULL);" // JSON coded
                ;

        public static final String DROP_SETTINGS_TABLE =
                "DROP TABLE IF EXISTS " + SETTINGS_TABLE;

        public static void initialize(SQLiteDatabase _conn) {
            _conn.execSQL(CREATE_SETTINGS_TABLE);
        }

        public static void uninitialize(SQLiteDatabase _conn) {
            _conn.execSQL(DROP_SETTINGS_TABLE);
        }

        public static Setting get(String key) {
            Setting s = null;

            Cursor cursor = connection.query(SETTINGS_TABLE, null, SETTINGS_KEY + "= ?", new String[] { key }, null, null, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    s = new Setting(cursor.getLong(SETTINGS_ID_COL), key).setData(cursor.getString(SETTINGS_VALUE_COL));
                    s.markUnDirty();
                }

                cursor.close();
            }

            return s;
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
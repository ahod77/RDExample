package com.example.alonassaf.rubberducky;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

/**
 * Created by AlonAssaf on 5/28/2016.
 */
public class Setting extends DBObject {
    private String key;
    private JSONObject data = null;

    public Setting(long _id, String _key) {
        super(_id);
        key = _key;
        data = null;
    }

    public Setting(String _key, long l) {
        try {
            key = _key;
            data = new JSONObject();
            data.put("data", l);
        } catch (JSONException e) {
            data = null;
        }
    }

    public Setting(String _key, String s) {
        try {
            key = _key;
            data = new JSONObject();
            data.put("data", s);
        } catch (JSONException e) {
            data = null;
        }
    }

    public Setting(String _key, long[] al) {
        try {
            key = _key;
            data = new JSONObject();
            JSONArray arr = new JSONArray();
            for (long v: al) { arr.put(v); }
            data.put("data", arr);
        } catch (JSONException e) {
            data = null;
        }
    }

    public long getEntityId() {
        try {
            return data.getLong("data");
        } catch (Exception e) {
            return 0;
        }
    }

    public String getString() {
        try {
            return data.getString("data");
        } catch (Exception e) {
            return null;
        }
    }

    public long[] getLongs() {
        try {
            JSONArray ja = data.getJSONArray("data");
            long[] la = new long[ja.length()];
            for (int i=0; i < ja.length(); i++) {
                la[i] = ja.getLong(i);
            }
            return la;
        } catch (Exception e) {
            return null;
        }
    }

    public String getKey() {
        return key;
    }

    public Setting setData(String _data) {
        try {
            data = new JSONObject(_data);
        } catch (JSONException e) {
            data = null;
        }
        markDirty();

        return this;
    }

    public String getData() {
        return data.toString();
    }

}
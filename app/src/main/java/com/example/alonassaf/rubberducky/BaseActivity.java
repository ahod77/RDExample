package com.example.alonassaf.rubberducky;

import android.content.Context;

import org.json.JSONObject;

/**
 * Created by assaf on 6/13/2016.
 */
public class BaseActivity {
    public void saveNewActivity(long userId, long containerId, long actionId, JSONObject actionParams){

    }

    public JSONObject getData( long id ){
        return null;
    }

    public Boolean act(Context context, Activity a){
        return false;
    }

    public String getDescription(Context context, Activity a){
        return null;
    }

    public Boolean isActionable(Context context, Activity a) {
        return false;
    }
}

package com.example.alonassaf.rubberducky;

import android.content.Context;
import android.view.View;

import org.json.JSONObject;

/**
 * Created by assaf on 6/13/2016.
 */
public class BaseAction {
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

    public Boolean isTextView(Context context, Activity a) {
        return true;
    }

    public View getView(Context context, Activity a) {
        return null;
    }

}

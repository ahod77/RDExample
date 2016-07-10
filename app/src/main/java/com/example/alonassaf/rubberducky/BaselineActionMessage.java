package com.example.alonassaf.rubberducky;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by assaf on 7/10/2016.
 */
public class BaselineActionMessage extends BaseActivity {
    @Override
    public String getDescription(Context context, Activity a){
        long currentUserId = ((Globals)context.getApplicationContext()).getUserId();

        String text = null;
        try {
            text = a.getAction_params().getString("text");
        } catch(JSONException e) {
            e.printStackTrace();
        }

        if(a.getCreator().getRowId() != currentUserId){ //Formats text for other users
            Date date = a.getTimestamp();
            return String.format("%s: %s\n[%tT]", a.getCreator().getName(), text, date);
        }
        else {
            return text;
        }
    }

    @Override
    public void saveNewActivity(long userId, long containerId, long actionId, JSONObject actionParams){
        Entity user = RubberDuckyDB2.Entities.get(userId);
        Entity container = RubberDuckyDB2.Entities.get(containerId);
        Entity action = RubberDuckyDB2.Entities.get(actionId);
        RubberDuckyDB2.Activities.set(new Activity(user, container, action, actionParams, 1));
    }
}
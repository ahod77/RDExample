package com.example.alonassaf.rubberducky;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import org.json.JSONObject;

/**
 * Created by assaf on 6/13/2016.
 */
public class BaselineActionBillTime extends BaseAction {

    public static final String HOURS_TOTAL = "hoursTotal";
    public static final String HOURS_NEW = "hoursNew";
    public static final String BILL_HOURS = "billHours";

    @Override
    public String getDescription(Context context, Activity a){
        if(a.getCreator() == null){
            return "Bill Time >>";
        }
        else {
            try {
                double bh = a.getAction_params().getDouble("billHours");
                return String.format("%s billed %2.1f hours", a.getCreator().getName(),bh);
            } catch (Exception e) {
                return "Bill Time: Data error";
            }
        }
    }

    @Override
    public Boolean act(Context context, Activity a){
        Entity project = RubberDuckyDB2.Entities.get(a.getContainer_id());
        JSONObject badges = project.getBadges();
        double hoursNew = badges.optDouble(HOURS_NEW, 0);

        if (hoursNew > 0) {
            Intent intent = new Intent(context, BaselineActionBillTimeDialog.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //Necessary to start an activity from outside an activity

            intent.putExtra("rowId", a.getRowId()); //Try using parcelization to pass Activity
            intent.putExtra("containerId", a.getContainer_id());
            intent.putExtra("userId", ((Globals)context.getApplicationContext()).getUserId()); //Hardcoded user id
            intent.putExtra("actionId", a.getAction_id());

            context.startActivity(intent);
            return true; //Does this need to be a Boolean?
        }
        else {
            Toast.makeText(context, "Before you can bill time, you have to report hours!", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    @Override
    public void saveNewActivity(long userId, long containerId, long actionId, JSONObject actionParams){//Move to parent?
        Entity user = RubberDuckyDB2.Entities.get(userId);
        Entity container = RubberDuckyDB2.Entities.get(containerId);
        Entity action = RubberDuckyDB2.Entities.get(actionId);
        RubberDuckyDB2.Activities.set(new Activity(user, container, action, actionParams, 1));
    }

    @Override
    public Boolean isActionable(Context context, Activity a) {
        return true;
    }
}

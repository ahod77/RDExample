package com.example.alonassaf.rubberducky;

import android.content.Context;
import android.content.Intent;

/**
 * Created by assaf on 6/13/2016.
 */
public class BaselineActivityBillTime extends BaseActivity {
    @Override
    public String getDescription(Activity a){
        return "Bill Time";
    }

    @Override
    public Boolean act(Context context, Activity a){
        Intent intent = new Intent(context, BillTimeDialog.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //Necessary to start an activity from outside an activity
        //Any extras
        context.startActivity(intent);
        return true; //Does this need to be a Boolean?
    }
}

package com.example.alonassaf.rubberducky;

import android.content.Context;
import android.content.Intent;

/**
 * Created by assaf on 6/13/2016.
 */
public class BaselineActivityReportHours extends BaseActivity {
    @Override
    public String getDescription(){
        return "Report Hours (D)";
    }

    @Override
    public Boolean act(Context context){
        Intent intent = new Intent(context, ReportHoursDialog.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //Necessary to start an activity from outside an activity
        //Any extras
        context.startActivity(intent);
        return true; //Does this need to be a Boolean?
    }
}

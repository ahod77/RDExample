package com.example.alonassaf.rubberducky;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

/**
 * Created by assaf on 6/8/2016.
 */
public class RDAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Activity> activities;

    public RDAdapter(Context context, ArrayList<Activity> activities){
        this.context = context;
        this.activities = activities;
    }

    @Override
    public int getCount(){
        return activities.size();
    }

    @Override
    public Object getItem(int position){
        return activities.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        ActivitiesLayout activitiesLayout = null;

        Activity activity = activities.get(position);

        if(convertView == null) {
            activitiesLayout = new ActivitiesLayout(context, activity);
        }
        else {
            activitiesLayout = (ActivitiesLayout) convertView;
            activitiesLayout.setActivity(activity);
        }
        return activitiesLayout;
    }
}

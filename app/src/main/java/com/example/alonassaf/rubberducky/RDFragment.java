package com.example.alonassaf.rubberducky;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by assaf on 6/6/2016.
 */
public class RDFragment extends Fragment {
    //private TextView rdTextView;
    private ListView activityListView;
    private String currentTabTag;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_rd, container, false);

        //rdTextView = (TextView) view.findViewById(R.id.rdTextView);
        activityListView = (ListView) view.findViewById(R.id.activityListView);

        TabHost tabhost = (TabHost) container.getParent().getParent();
        currentTabTag = tabhost.getCurrentTabTag();

        refresh();

        return view;
    }

    public void refresh(){
        //String text = "This is tab " + currentTabTag;
        //rdTextView.setText(text);

        Context context = getActivity().getApplicationContext();
        long containerId = 7; //Hardcoded RubberDucky id for test, change this

        //Needs way to only query activities with no creator
        ArrayList<Activity> activities = (ArrayList)RubberDuckyDB2.Activities.getAllByContainer(containerId);

        RDAdapter adapter = new RDAdapter(context, activities);
        activityListView.setAdapter(adapter);
    }

    @Override
    public void onResume(){
        super.onResume();
        refresh();
    }
}

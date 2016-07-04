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
    private ListView activityListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_rd, container, false);

        activityListView = (ListView) view.findViewById(R.id.activityListView);

        //TabHost tabhost = (TabHost) container.getParent().getParent();

        refresh();

        return view;
    }

    public void refresh() {
        Context context = getActivity().getApplicationContext();
        Bundle bundle = this.getArguments();
        long containerId = bundle.getLong("paneID");

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

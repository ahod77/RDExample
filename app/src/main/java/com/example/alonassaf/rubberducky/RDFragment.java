package com.example.alonassaf.rubberducky;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by assaf on 6/6/2016.
 */
public class RDFragment extends Fragment
implements TextView.OnEditorActionListener {
    private ListView activityListView;
    private EditText editText; //Change name

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_rd, container, false);

        //Get widget references
        activityListView = (ListView) view.findViewById(R.id.activityListView);
        editText = (EditText) view.findViewById(R.id.editText);

        //TabHost tabhost = (TabHost) container.getParent().getParent();

        //Set listeners
        editText.setOnEditorActionListener(this);

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
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE ||
            actionId == EditorInfo.IME_ACTION_UNSPECIFIED)
        {

        }
        return false;
    }

    @Override
    public void onResume(){
        super.onResume();
        refresh();
    }
}

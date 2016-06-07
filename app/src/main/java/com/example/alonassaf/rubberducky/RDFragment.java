package com.example.alonassaf.rubberducky;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TextView;

/**
 * Created by assaf on 6/6/2016.
 */
public class RDFragment extends Fragment {
    private TextView rdTextView;
    private String currentTabTag;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_rd, container, false);

        rdTextView = (TextView) view.findViewById(R.id.rdTextView);

        TabHost tabhost = (TabHost) container.getParent().getParent();
        currentTabTag = tabhost.getCurrentTabTag();

        refresh();

        return view;
    }

    public void refresh(){
        String text = "This is tab " + currentTabTag;
        rdTextView.setText(text);
    }

    @Override
    public void onResume(){
        super.onResume();
        refresh();
    }
}

package com.example.alonassaf.rubberducky;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by assaf on 6/6/2016.
 */
public class RDFragment extends Fragment
implements TextView.OnEditorActionListener, View.OnClickListener {
    private ListView activityListView;
    private EditText inputText;
    private ImageButton sendButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_rd, container, false);

        //Get widget references
        activityListView = (ListView) view.findViewById(R.id.activityListView);
        inputText = (EditText) view.findViewById(R.id.inputText);
        sendButton = (ImageButton) view.findViewById(R.id.sendButton);

        //TabHost tabhost = (TabHost) container.getParent().getParent();

        //Set listeners
        inputText.setOnEditorActionListener(this);
        sendButton.setOnClickListener(this);

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sendButton:
                String message = inputText.getText().toString();
                inputText.setText(null);

                if (message == null || message.equals(""))
                    break;

                JSONObject params = new JSONObject();
                try {
                    params.put("text", message);
                } catch (JSONException e) {
                    params = null;
                }

                long userId = ((Globals) getActivity().getApplicationContext()).getUserId();
                long containerId = this.getArguments().getLong("paneID");
                long actionId = ((Globals)getActivity().getApplicationContext()).getActionId();
                Entity action = RubberDuckyDB2.Entities.get(actionId);
                try {
                    Class c = Class.forName(action.getFQCN());
                    BaseActivity ba = (BaseActivity) c.newInstance();

                    ba.saveNewActivity(userId, containerId, actionId, params);

                    refresh();

                    MediaPlayer mp = MediaPlayer.create(getActivity(), Settings.System.DEFAULT_NOTIFICATION_URI);
                    mp.start();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}

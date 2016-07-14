package com.example.alonassaf.rubberducky;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by assaf on 6/7/2016.
 */
public class ActivitiesLayout extends RelativeLayout
implements SlideButtonListener {
    private TextView actTextView;
    private SlideButton sb;

    private Activity activity;
    private Context context;

    public ActivitiesLayout(Context context) { //used by Android tools
        super(context);
    }

    public ActivitiesLayout(final Context context, Activity a) {
        super(context);

        //set context
        this.context = context;

        //inflate layout
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.listview_activities, this, true);

        //get references to widgets
        //checkBox = (CheckBox) findViewById(R.id.checkBox);
        actTextView = (TextView) findViewById(R.id.activityTextView);
        sb = (SlideButton) findViewById(R.id.unlockButton);

        //set listeners
        sb.setSlideButtonListener(this);

        //set activity data on widgets
        setActivity(a);
    }

    public void setActivity(Activity a){
        activity = a;

        //Sets text to activity's action name
        try {
            Class c = Class.forName(RubberDuckyDB2.Entities.get(activity.getAction_id()).getFQCN());
            BaseActivity ba = (BaseActivity) c.newInstance();

            if (!ba.isActionable(context, activity)) {
                sb.setVisibility(View.GONE);
            }
            actTextView.setText(ba.getDescription(context, activity));

            if (activity.getStatus() == 1) {
                final AnimationDrawable drawable = new AnimationDrawable();
                final Handler handler = new Handler();

                drawable.addFrame(new ColorDrawable(Color.RED), 400);
                drawable.addFrame(new ColorDrawable(Color.GREEN), 400);
                drawable.setOneShot(false);

                setBackground(drawable);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        drawable.start();
                    }
                }, 100);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        drawable.stop();
                        setBackground(null);

                        activity.setStatus(0);
                        RubberDuckyDB2.Activities.set(activity);
                    }
                }, 5000 + (int) (Math.random() * 10000));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void handleSlide() {
        try {
            Class c = Class.forName(RubberDuckyDB2.Entities.get(activity.getAction_id()).getFQCN());
            BaseActivity ba = (BaseActivity) c.newInstance();
            ba.act(context, activity);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}

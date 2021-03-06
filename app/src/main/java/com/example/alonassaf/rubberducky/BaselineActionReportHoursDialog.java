package com.example.alonassaf.rubberducky;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by assaf on 6/15/2016.
 */
public class BaselineActionReportHoursDialog extends AppCompatActivity
implements View.OnClickListener {

    private TextView hoursWorkedTextView;
    private Button incrementHoursButton;
    private Button decrementHoursButton;
    private Button submitButton;

    private double hoursWorked = 2.0f;

    //private SharedPreferences savedValues;

    private long rowId = 0;
    private long containerId = 0;
    private long userId = 0;
    private long actionId = 0;

    private Entity project = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_hours);

        //Sets up action bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarRH);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Gets widget references
        hoursWorkedTextView = (TextView) findViewById(R.id.hoursWorkedTextView);
        incrementHoursButton = (Button) findViewById(R.id.incrementHoursRH);
        decrementHoursButton = (Button) findViewById(R.id.decrementHoursRH);
        submitButton = (Button) findViewById(R.id.submitBtnRH);

        //Set listeners
        incrementHoursButton.setOnClickListener(this);
        decrementHoursButton.setOnClickListener(this);
        submitButton.setOnClickListener(this);

        //Get values from intent
        Intent intent = getIntent();
        rowId = intent.getLongExtra("rowId", 0);
        containerId = intent.getLongExtra("containerId", 0);
        userId = intent.getLongExtra("userId", 0);
        actionId = intent.getLongExtra("actionId", 0);

        //Get badge values
        project = RubberDuckyDB2.Entities.get(containerId);
        JSONObject badges = project.getBadges();

        //Gets previous hoursWorked value for repeated action
        Activity a = RubberDuckyDB2.Activities.get(rowId);
        if (a.getCreator() != null) {
            hoursWorked = a.getAction_params().optDouble(BaselineActionReportHours.HOURS_WORKED, -1);
        }

        display();

        //Get shared preferences object
        //savedValues = getSharedPreferences("SavedValues", MODE_PRIVATE);
    }

//    @Override
//    public void onPause(){
//        //save the instance variables
//        SharedPreferences.Editor editor = savedValues.edit();
//        editor.putFloat("hoursWorked", hoursWorked);
//        editor.commit();
//
//        super.onPause();
//    }

//    @Override
//    public void onResume(){
//        super.onResume();
//
//        //get the instance variables
//        hoursWorked = savedValues.getFloat("hoursWorked", 2.0f);
//
//        display();
//    }

    public void display(){
        hoursWorkedTextView.setText(String.valueOf(hoursWorked));
    }

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.incrementHoursRH:
                hoursWorked += 0.5f;
                display();
                break;
            case R.id.decrementHoursRH:
                if (hoursWorked > 0) {
                    hoursWorked -= 0.5f;
                }
                display();
                break;
            case R.id.submitBtnRH:
                if (hoursWorked <= 0){
                    Toast.makeText(this, "Please enter a valid number of hours", Toast.LENGTH_LONG).show();
                }
                else {
                    BaselineActionReportHours helper = new BaselineActionReportHours();
                    try {
                        JSONObject j = new JSONObject();
                        j.put(BaselineActionReportHours.HOURS_WORKED, hoursWorked);
                        helper.saveNewActivity(userId, containerId, actionId, j);

                        updateBadges();

                        finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void updateBadges(){
        JSONObject badges = project.getBadges();

        double hoursNew = badges.optDouble(BaselineActionReportHours.HOURS_NEW, 0.0);
        double hoursTotal = badges.optDouble(BaselineActionReportHours.HOURS_TOTAL, 0.0);

        hoursNew += hoursWorked;
        hoursTotal += hoursWorked;

        try {
            badges.put(BaselineActionReportHours.HOURS_NEW, hoursNew);
            badges.put(BaselineActionBillTime.HOURS_TOTAL, hoursTotal);
        } catch(JSONException e) {
            e.printStackTrace();
        }

        project.setBadges(badges);
        RubberDuckyDB2.Entities.set(project);
    }
}
package com.example.alonassaf.rubberducky;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by assaf on 6/15/2016.
 */
public class BaselineActionBillTimeDialog extends AppCompatActivity
implements View.OnClickListener {
    private TextView billTimeTextView;
    private Button incrementHoursButton;
    private Button decrementHoursButton;
    private Button submitButton;

    private long rowId = 0;
    private long containerId = 0;
    private long userId = 0;
    private long actionId = 0;

    private Entity project = null;
    private double hoursNew = 0.0;
    private double billHours = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_time);

        //Sets up action bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarBT);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Gets widget references
        billTimeTextView = (TextView) findViewById(R.id.billTimeTextView);
        incrementHoursButton = (Button) findViewById(R.id.incrementHoursBT);
        decrementHoursButton = (Button) findViewById(R.id.decrementHoursBT);
        submitButton = (Button) findViewById(R.id.submitBtnBT);

        //Set listeners
        incrementHoursButton.setOnClickListener(this);
        decrementHoursButton.setOnClickListener(this);
        submitButton.setOnClickListener(this);

        //Gets values from intent
        Intent intent = getIntent();
        rowId = intent.getLongExtra("rowId", 0);
        containerId = intent.getLongExtra("containerId", 0);
        userId = intent.getLongExtra("userId", 0);
        actionId = intent.getLongExtra("actionId", 0);

        //Gets badge values
        project = RubberDuckyDB2.Entities.get(containerId);
        JSONObject badges = project.getBadges();

        hoursNew = badges.optDouble(BaselineActionBillTime.HOURS_NEW, 0.0);

        //Sets billHours to hoursNew by default
        billHours = hoursNew;

        //Gets previous billHours value for repeated action
        Activity a = RubberDuckyDB2.Activities.get(rowId);
        if (a.getCreator() != null) {
            double previousBillHours = a.getAction_params().optDouble(BaselineActionBillTime.BILL_HOURS, -1);
            billHours = Math.min(previousBillHours, hoursNew); //Changes default value to previous billHours if it is less than hoursNew
        }

        display();
    }

    public void display(){
        billTimeTextView.setText(String.valueOf(billHours));
    }

    private double limitHours(double hours)
    {
        return Math.max(0.5f, Math.min(hoursNew, hours));
    }

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.incrementHoursBT:
                billHours = limitHours(billHours + 0.5f);
                display();
                break;
            case R.id.decrementHoursBT:
                billHours = limitHours(billHours - 0.5f);
                display();
                break;
            case R.id.submitBtnBT:
                BaselineActionBillTime helper = new BaselineActionBillTime();
                try {
                    JSONObject j = new JSONObject();
                    j.put(BaselineActionBillTime.BILL_HOURS, billHours);
                    helper.saveNewActivity(userId, containerId, actionId, j);

                    updateBadges();

                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
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
        hoursNew -= billHours;

        try {
            badges.put(BaselineActionBillTime.HOURS_NEW, hoursNew);
        } catch (JSONException e){
            e.printStackTrace();
        }

        project.setBadges(badges);
        RubberDuckyDB2.Entities.set(project);

        //Write billed record to bank
    }
}

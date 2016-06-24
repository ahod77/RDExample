package com.example.alonassaf.rubberducky;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by assaf on 6/15/2016.
 */
public class ReportHoursDialog extends AppCompatActivity
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

        Intent intent = getIntent();
        rowId = intent.getLongExtra("rowId", 0);
        containerId = intent.getLongExtra("containerId", 0);
        userId = intent.getLongExtra("userId", 0);
        actionId = intent.getLongExtra("actionId", 0);

        Activity a = RubberDuckyDB2.Activities.get(rowId);
        if (a.getCreator() != null) {
            JSONObject j = a.getAction_params();
            try {
                hoursWorked = j.getDouble("hoursWorked");
            } catch (Exception e) {
                hoursWorked = -1;
            }
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
                BaselineActivityReportHours helper = new BaselineActivityReportHours();
                try {
                    JSONObject j = new JSONObject();
                    j.put("hoursWorked", hoursWorked);
                    helper.saveNewActivity(userId, containerId, actionId, j);
                    finish();
                } catch(JSONException e){
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
}

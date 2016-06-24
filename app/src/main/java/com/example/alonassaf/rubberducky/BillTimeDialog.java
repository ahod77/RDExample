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
public class BillTimeDialog extends AppCompatActivity
implements View.OnClickListener {
    private TextView billTimeTextView;
    private Button incrementHoursButton;
    private Button decrementHoursButton;
    private Button submitButton;

    private long rowId = 0;
    private long containerId = 0;
    private long userId = 0;
    private long actionId = 0;

//    Test code. Will be moved to badge logic. Delete this.
    double hoursNew = 10.0;
//    -----------------------------------------------------

    private double billHours = hoursNew;

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

        Intent intent = getIntent();
        rowId = intent.getLongExtra("rowId", 0);
        containerId = intent.getLongExtra("containerId", 0);
        userId = intent.getLongExtra("userId", 0);
        actionId = intent.getLongExtra("actionId", 0);

        Activity a = RubberDuckyDB2.Activities.get(rowId);
        if (a.getCreator() != null) {
            JSONObject j = a.getAction_params();
            try {
                billHours = j.getDouble("billHours");
            } catch (Exception e) {
                billHours = -1;
            }
        }

        display();
    }

    public void display(){
        billTimeTextView.setText(String.valueOf(billHours));
    }

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.incrementHoursBT:
                if (billHours < hoursNew)
                billHours += 0.5f;
                display();
                break;
            case R.id.decrementHoursBT:
                if (billHours > 0) {
                    billHours -= 0.5f;
                }
                display();
                break;
            case R.id.submitBtnBT:
                BaselineActivityBillTime helper = new BaselineActivityBillTime();
                try {
                    JSONObject j = new JSONObject();
                    j.put("billHours", billHours);
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

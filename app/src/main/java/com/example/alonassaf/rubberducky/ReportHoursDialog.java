package com.example.alonassaf.rubberducky;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by assaf on 6/15/2016.
 */
public class ReportHoursDialog extends AppCompatActivity
implements View.OnClickListener {

    private TextView hoursWorkedTextView;
    private Button incrementHoursButton;
    private Button decrementHoursButton;

    private float hoursWorked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_hours);

        //Sets up action bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarRH);
        setSupportActionBar(toolbar);

        //Gets widget references
        hoursWorkedTextView = (TextView) findViewById(R.id.hoursWorkedTextView);
        incrementHoursButton = (Button) findViewById(R.id.increment_hours);
        decrementHoursButton = (Button) findViewById(R.id.decrement_hours);

        //Set listeners
        incrementHoursButton.setOnClickListener(this);
        decrementHoursButton.setOnClickListener(this);
    }

    @Override
    public void onPause(){


        super.onPause();
    }

    @Override
    public void onResume(){
        super.onResume();


    }

    public void display(){

    }

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.increment_hours:
                //Increment code
                break;
            case R.id.decrement_hours:
                //Decrement code
                break;
        }
    }
}

package com.example.alonassaf.rubberducky;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

/**
 * Created by assaf on 6/15/2016.
 */
public class BillTimeDialog extends AppCompatActivity {
    private TextView billTimeTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_time);

        //Sets up action bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarBT);
        setSupportActionBar(toolbar);

        //Gets widget references
        billTimeTextView = (TextView) findViewById(R.id.billTimeTextView);
    }


}

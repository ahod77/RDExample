package com.example.alonassaf.rubberducky;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TabHost;

public class MainActivity extends AppCompatActivity {
    private RubberDuckyDB db;
    private TabHost tabHost;
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Sets up action bar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        //Gets reference instance to database singleton
        db = RubberDuckyDB.getInstance();
        db.init(this); //Passes context

        db.populateDB();

        //Sets up tabs
        tabHost = (TabHost)findViewById(R.id.tabHost);
        tabHost.setup();

        //Add these tabs dynamically based on tables
        //Tab 1
        TabHost.TabSpec spec = tabHost.newTabSpec("Actors");
        spec.setContent(R.id.tab1);
        spec.setIndicator("Actors");
        tabHost.addTab(spec);

        //Tab 2
        spec = tabHost.newTabSpec("Actions");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Actions");
        tabHost.addTab(spec);

        //Tab 3
        spec = tabHost.newTabSpec("Activity");
        spec.setContent(R.id.tab3);
        spec.setIndicator("Activity");
        tabHost.addTab(spec);

        //Tab 4
        spec = tabHost.newTabSpec("Rubber Ducky");
        spec.setContent(R.id.tab4);
        spec.setIndicator("Rubber Ducky");
        tabHost.addTab(spec);

        //Set activity tab to show at launch -- will be changed to most recent
        tabHost.setCurrentTab(2);

        //Sets content for tab 4 text views to activity names
        /*Way too messy. Need to simply this and not hardcode activity ids
        tv = (TextView)findViewById(R.id.tab4tv);
        tv.setText(db.getEntity(db.getActivity(1).getAction_id()).getName());
        tv = (TextView)findViewById(R.id.tab4tv2);
        tv.setText(db.getEntity(db.getActivity(2).getAction_id()).getName());*/


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.menu_restart:
                db.clearDB();
                db.populateDB();
                return true;
            case R.id.menu_logout:
                Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_test:
                return true;
            case R.id.menu_test2:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

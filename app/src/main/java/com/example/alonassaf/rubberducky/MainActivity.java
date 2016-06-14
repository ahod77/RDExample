package com.example.alonassaf.rubberducky;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.google.tabmanager.TabManager;

public class MainActivity extends AppCompatActivity {
    // private RubberDuckyDB2 db;

    TabHost tabHost;
    TabManager tabManager;
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Sets up action bar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        //Gets reference instance to database singleton
        RubberDuckyDB2.connect(this);
        // RubberDuckyDB2.populate();

        //Sets up tabhost
        tabHost = (TabHost)findViewById(R.id.tabHost);
        tabHost.setup();
        tabManager = new TabManager(this, tabHost, R.id.realtabcontent);

        //Query for pinnedPanes JSON object for tab generation
        long[] pinnedPanes = RubberDuckyDB2.Settings.get("pinnedPanes").getLongs();

        //Tab generation
        int i = 0;
        for (long pp : pinnedPanes) {
            String tabName = RubberDuckyDB2.Entities.get(pp).getName();
            TabHost.TabSpec spec = tabHost.newTabSpec(tabName);
            spec.setIndicator(tabName);
            tabManager.addTab(spec, RDFragment.class, null);
        }

        //Set activity tab to show at launch -- will be changed to most recent
        //tabHost.setCurrentTab(2); //Is there a way to move this tab to center?
        //Set current tab to last tab opened
        if(savedInstanceState != null)
            tabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putString("tab", tabHost.getCurrentTabTag());
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
                RubberDuckyDB2.clear();
                RubberDuckyDB2.populate();
                //Refresh displayed info (like resuming activity)
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

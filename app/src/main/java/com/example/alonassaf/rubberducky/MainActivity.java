package com.example.alonassaf.rubberducky;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    // private RubberDuckyDB2 db;

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
        RubberDuckyDB2.connect(this);
        // RubberDuckyDB2.populate();

        //Sets up tabhost
        tabHost = (TabHost)findViewById(R.id.tabHost);
        tabHost.setup();

        //Query for pinnedPanes JSON object for tab generation
        long[] pinnedPanes = RubberDuckyDB2.Settings.get("pinnedPanes").getLongs();

        //Tab generation
        int i = 0;
        for (long pp : pinnedPanes) {
            final int j = i++;
            final String tabName = RubberDuckyDB2.Entities.get(pp).getName();
            final TabHost.TabSpec spec = tabHost.newTabSpec(tabName);
            spec.setContent(new TabHost.TabContentFactory() {
                @Override
                public View createTabContent(String tag) {
                    TextView text = new TextView(MainActivity.this);
                    text.setText("This is tab " + (j+1));
                    text.setTypeface(null, Typeface.BOLD);
                    text.setId(j+1);
                    return (text);
                }
            });
            spec.setIndicator(tabName);
            tabHost.addTab(spec);
        }

        //Set activity tab to show at launch -- will be changed to most recent
        tabHost.setCurrentTab(2); //Is there a way to move this tab to center?

        //Sets content for tab 4 text views to activity names
        /*Too messy. Need to simply this and not hardcode activity ids
        tv = (TextView)findViewById(R.id.tab4tv);
        tv.setText(db.getEntityName(db.getActivity(1).getAction_id()));
        tv = (TextView)findViewById(R.id.tab4tv2);
        tv.setText(db.getEntityName(db.getActivity(2).getAction_id()));*/
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

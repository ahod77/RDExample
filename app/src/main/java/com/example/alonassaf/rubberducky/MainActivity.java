package com.example.alonassaf.rubberducky;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.TabHost;
import android.widget.Toast;

import com.google.tabmanager.TabManager;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private TabHost tabHost;
    private TabManager tabManager;
    private Button badgeCount1 = null, badgeCount2 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Sets up action bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Reference to horizontal scrollview
        HorizontalScrollView s = (HorizontalScrollView) findViewById(R.id.tabHsv);

        //Gets reference instance to database singleton
        RubberDuckyDB2.connect(this);


        for (Entity e : RubberDuckyDB2.Entities.getAllByType(1)){
            if (e.getName().equals("Asaf")){
                ((Globals)getApplicationContext()).setUserId(e.getRowId());
            }
        }

        for (Entity e : RubberDuckyDB2.Entities.getAllByType(2)){
            if (e.getName().equals("Message")){
                ((Globals)getApplicationContext()).setMessageAction(e.getRowId());
            }
        }

        //Sets up tabhost
        tabHost = (TabHost)findViewById(R.id.tabHost);
        tabHost.setup();
        tabManager = new TabManager(this, tabHost, R.id.realtabcontent);

        //Query for pinnedPanes JSON object for tab generation
        long[] pinnedPanes = RubberDuckyDB2.Settings.get("pinnedPanes").getLongs();

        //Tab generation
        for (long pp : pinnedPanes) {
            String tabName = RubberDuckyDB2.Entities.get(pp).getName();
            TabHost.TabSpec spec = tabHost.newTabSpec(tabName);
            spec.setIndicator(tabName);

            Bundle bundle = new Bundle();
            bundle.putLong("paneID", pp);
            //Should add paneType here and to bundle to decide which fragment to add and for decisions within fragment class?

            tabManager.addTab(spec, RDFragment.class, bundle);
        }

        //Set activity tab to show at launch -- will be changed to most recent
        tabHost.setCurrentTab(3); //Is there a way to move this tab to center?

        s.fullScroll(HorizontalScrollView.FOCUS_RIGHT);

        //Set current tab to last tab opened
        if(savedInstanceState != null)
            tabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
    }

    public void updateBadges() {
        JSONObject badges = RubberDuckyDB2.Entities.get(7).getBadges();
        double b1 = badges.optDouble(BaselineActivityReportHours.HOURS_TOTAL, 0.0);
        double b2 = badges.optDouble(BaselineActivityReportHours.HOURS_NEW, 0.0);

        if (badgeCount1 != null) badgeCount1.setText(String.valueOf(b1));
        if (badgeCount2 != null) badgeCount2.setText(String.valueOf(b2));
    }

    @Override
    public void onResume(){
        super.onResume();
        updateBadges();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putString("tab", tabHost.getCurrentTabTag());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.activity_main, menu);

        MenuItem item = menu.findItem(R.id.badge1);
        MenuItemCompat.setActionView(item, R.layout.badge_update_count_red);
        badgeCount1 = (Button) MenuItemCompat.getActionView(item);

        MenuItem item2 = menu.findItem(R.id.badge2);
        MenuItemCompat.setActionView(item2, R.layout.badge_update_count_green);
        badgeCount2 = (Button) MenuItemCompat.getActionView(item2);

        updateBadges();

        return super.onCreateOptionsMenu(menu);
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

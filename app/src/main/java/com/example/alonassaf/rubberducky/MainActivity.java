package com.example.alonassaf.rubberducky;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.TabHost;
import android.widget.Toast;

import com.google.tabmanager.TabManager;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    ContainerCollectionPagerAdapter containersAdapter;
    ViewPager viewPager;

    private Button badgeCount1 = null, badgeCount2 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Gets reference instance to database singleton
        RubberDuckyDB2.connect(this);

        // Load a few globals
        // Noam: fix this, not elegant
        for (Entity e : RubberDuckyDB2.Entities.getAllByType(1)){
            if (e.getName().equals("Asaf")){
                ((Globals) getApplicationContext()).setUserId(e.getRowId());
            }
        }
        for (Entity e : RubberDuckyDB2.Entities.getAllByType(2)){
            if (e.getName().equals("Message")){
                ((Globals) getApplicationContext()).setMessageAction(e.getRowId());
            }
        }

        super.onCreate(savedInstanceState);
//        setContentView(R.layout.paged_tabs);
        View view = getLayoutInflater().inflate(R.layout.paged_tabs, null);
        setContentView(view);

        //Sets up action bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        containersAdapter = new ContainerCollectionPagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(containersAdapter);

//        SlidingTabLayout slidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.sliding_tabs);
//        slidingTabLayout.setDistributeEvenly(true);
//        slidingTabLayout.setViewPager(viewPager);

        viewPager.setCurrentItem(containersAdapter.getCount() + 1);

        viewPager.addOnPageChangeListener(
                new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        super.onPageSelected(position);
                        updateBadges();
                    }
                });

        /*
        final ActionBar actionBar = getActionBar();

        // Specify that tabs should be displayed in the action bar.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create a tab listener that is called when the user changes tabs.
        ActionBar.TabListener tabListener = new ActionBar.TabListener() {
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
                // When the tab is selected, switch to the
                // corresponding page in the ViewPager.
                viewPager.setCurrentItem(tab.getPosition());
            }

            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
                // hide the given tab
            }

            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
                // probably ignore this event
            }
        };

        long[] pinnedPanes = RubberDuckyDB2.Settings.get("pinnedPanes").getLongs();
        for (long pp : pinnedPanes) {
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(RubberDuckyDB2.Entities.get(pp).getName())
                            .setTabListener(tabListener));
        }
        */


        //Set activity tab to show at launch -- will be changed to most recent
//        tabHost.setCurrentTab(3); //Is there a way to move this tab to center?

//        s.fullScroll(HorizontalScrollView.FOCUS_RIGHT);

        //Set current tab to last tab opened
//        if(savedInstanceState != null)
//            tabHost.setCurrentTabByTag(savedInstanceState.getString("tab"));
    }

    @Override
    public void onResume(){
        super.onResume();
        updateBadges();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
  //      outState.putString("tab", tabHost.getCurrentTabTag());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.activity_main, menu);

        MenuItem item = menu.findItem(R.id.badge1);
        MenuItemCompat.setActionView(item, R.layout.badge_update_count_red);
        badgeCount1 = (Button) MenuItemCompat.getActionView(item);

        item = menu.findItem(R.id.badge2);
        MenuItemCompat.setActionView(item, R.layout.badge_update_count_green);
        badgeCount2 = (Button) MenuItemCompat.getActionView(item);

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

    public void updateBadges() {
        long containerId = containersAdapter.getContainerIdForItem(viewPager.getCurrentItem());
        JSONObject badges = RubberDuckyDB2.Entities.get(containerId).getBadges();
        double b1 = badges.optDouble(BaselineActionReportHours.HOURS_TOTAL, 0.0);
        double b2 = badges.optDouble(BaselineActionReportHours.HOURS_NEW, 0.0);

        if (badgeCount1 != null) badgeCount1.setText(String.valueOf(b1));
        if (badgeCount2 != null) badgeCount2.setText(String.valueOf(b2));
    }

}

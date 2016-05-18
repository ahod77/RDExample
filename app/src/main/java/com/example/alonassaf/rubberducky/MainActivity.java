package com.example.alonassaf.rubberducky;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.widget.TabHost;

public class MainActivity extends AppCompatActivity {
    private RubberDuckyDB db;
    private StringBuilder sb;

    private TabHost tabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Sets up action bar
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        //Sets up tabs
        TabHost host = (TabHost)findViewById(R.id.tabHost);
        host.setup();

        //Tab 1
        TabHost.TabSpec spec = host.newTabSpec("Actors");
        spec.setContent(R.id.tab1);
        spec.setIndicator("Actors");
        host.addTab(spec);

        //Tab 2
        spec = host.newTabSpec("Actions");
        spec.setContent(R.id.tab2);
        spec.setIndicator("Actions");
        host.addTab(spec);

        //Tab 3
        spec = host.newTabSpec("Activity");
        spec.setContent(R.id.tab3);
        spec.setIndicator("Activity");
        host.addTab(spec);

        //Tab 4
        spec = host.newTabSpec("Rubber Ducky");
        spec.setContent(R.id.tab4);
        spec.setIndicator("Rubber Ducky");
        host.addTab(spec);

        //Set activity tab to show at launch
        host.setCurrentTab(2);

        //gets database and StringBuilder objects
        db = new RubberDuckyDB(this);
        sb = new StringBuilder();

        initializeDB();
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
                clearDB();
                initializeDB();
                return true;
            case R.id.menu_logout:
                Toast.makeText(this, "Logout", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initializeDB(){
        Entity entity = new Entity("Actor", "Asaf", "Building Rubber Ducky!");
        long insertId = db.insertEntity(entity);

        if (insertId > 0) { //For test
            sb.append("Row inserted: Insert Id: " + insertId + "\n");
        }

        Entity entity2 = new Entity("Actor", "Alon","");
        insertId = db.insertEntity(entity2);

        if (insertId > 0) { //For test
            sb.append("Row inserted: Insert Id: " + insertId + "\n");
        }

        //Test
        Toast.makeText(this, sb.toString(), Toast.LENGTH_LONG).show();

    }

    private void clearDB(){
        sb.setLength(0); //Clears string builder for test

        int deleteCount = db.deleteAllEntities();
        sb.append("Entity table cleared! Delete count: " + deleteCount + "\n\n"); //For test
    }
}

    //Add display all Entities; Add update; Maybe display sb on UI;

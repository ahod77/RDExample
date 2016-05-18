package com.example.alonassaf.rubberducky;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private RubberDuckyDB db;
    private StringBuilder sb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

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

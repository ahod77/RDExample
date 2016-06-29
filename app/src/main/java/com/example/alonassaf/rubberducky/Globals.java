package com.example.alonassaf.rubberducky;

import android.app.Application;

/**
 * Created by assaf on 6/27/2016.
 */
public class Globals extends Application {
    private long userId;

    public long getUserId(){
        return userId;
    }

    public void setUserId(long userId){
        this.userId = userId;
    }
}

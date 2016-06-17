package com.example.alonassaf.rubberducky;

import android.content.Context;

import org.json.JSONObject;

/**
 * Created by assaf on 6/13/2016.
 */
public class BaseActivity {
    public void setData(JSONObject j){

    }

    public JSONObject getData(){
        return null;
    }

    public Boolean act(Context context){
        return false;
    }

    public String getDescription(){
        return null;
    }
}

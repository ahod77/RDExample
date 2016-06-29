package com.example.alonassaf.rubberducky;

import org.json.JSONObject;

/**
 * Created by AlonAssaf on 5/15/2016.
 */
public class Entity extends DBObject {

    private int type = 0;
    private String name = "";
    private String desc = "";
    private String fqcn = "";
    private JSONObject badges = null;

    // Constructors
    public Entity(long _id, int type, String name, String desc, Class c, JSONObject badges) {
        super(_id);
        this.type = type;
        this.name = name;
        this.desc = desc;
        this.fqcn = c == null ? null : c.getName();
        this.badges = badges == null ? new JSONObject() : badges;
    }

    public Entity(int type, String name, String desc, Class c, JSONObject badges) {
        this(0, type, name, desc, c, badges);
    }

    // Getters and Setters
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
        markDirty();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        markDirty();
    }

    public String getDesc(){
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
        markDirty();
    }

    public String getFQCN(){
        return fqcn;
    }

    public void setFQCN(String fqcn){
        this.fqcn = fqcn;
        markDirty();
    }

    public String getSerializedBadges() {
        if (badges == null)
            return null;
        else
            return badges.toString();
    }

    public JSONObject getBadges(){
        return badges;
    }

    public void setBadges(JSONObject badges){
        this.badges = badges;
        markDirty();
    }

    public String toString() {
        return String.format("%1$d| %2$s %3$s %4$s", getRowId(), type, name, desc);
    }

}

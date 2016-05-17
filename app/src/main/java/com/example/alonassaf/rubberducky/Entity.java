package com.example.alonassaf.rubberducky;

/**
 * Created by AlonAssaf on 5/15/2016.
 */
public class Entity {
    private int id; //Id holds idx value when entity is retrieved from db. Not used in insert, but can be used in update
    private String type;
    private String name;
    private String desc;

    public Entity(String type, String name){
        this.id = 0;
        this.type = type;
        this.name = name;
        this.desc = "";
    }

    public Entity(String type, String name, String desc){
        this.id = 0;
        this.type = type;
        this.name = name;
        this.desc = desc;
    }

    public Entity(int id, String type, String name){
        this.id = id;
        this.type = type;
        this.name = name;
        this.desc = "";
    }

    public Entity(int id, String type, String name, String desc){
        this.id = id;
        this.type = type;
        this.name = name;
        this.desc = desc;
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
     this.id = id;
    }

    public String getType(){
        return type;
    }

    public void setType(String type){
        this.type = type;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getDesc(){
        return desc;
    }

    public void setDesc(String desc){
        this.desc = desc;
    }
}

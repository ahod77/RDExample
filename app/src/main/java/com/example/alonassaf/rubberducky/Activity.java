package com.example.alonassaf.rubberducky;

import java.sql.Timestamp;

/**
 * Created by AlonAssaf on 5/17/2016.
 */
public class Activity {
    int id; //Id holds idx value when entity is retrieved from db. Not used in insert, but can be used in update
    Timestamp timestamp;
    Entity creator;
    Entity actor;
    Entity action;

    //Constructors
    public Activity(int id, Timestamp timestamp, Entity creator, Entity actor, Entity action){
        this.id = id;
        this.timestamp = timestamp;
        this.creator = creator;
        this.actor = actor;
        this.action = action;
    }

    public Activity(Timestamp timestamp, Entity creator, Entity actor, Entity action){
        this(0, timestamp, creator, actor, action );
    }

    public Activity(int id, Timestamp timestamp, Entity actor, Entity action){
        this(id, timestamp, null, actor, action);
    }

    public Activity(Timestamp timestamp, Entity actor, Entity action) {
        this(0, timestamp, null, actor, action);
    }

    public Activity(Entity actor, Entity action) {
        this(0, null, null, actor, action);
    }

    public Activity(int id, Entity actor, Entity action){
        this(id, null, null, actor, action);
    }

    public Activity(Entity creator, Entity actor, Entity action){
        this(0, null, creator, actor, action);
    }

    public Activity(int id, Entity creator, Entity actor, Entity action){
        this(id, null, creator, actor, action);
    }

    //Getters and Setters
    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public Timestamp getTimestamp(){
        return timestamp;
    }

    public void setTimestamp (Timestamp timestamp){
        this.timestamp = timestamp;
    }

    public Entity getCreator(){
        return creator;
    }

    public void setCreator(Entity creator){
        this.creator = creator;
    }

    public Entity getActor(){
        return actor;
    }

    public void setActor(Entity actor){
        this.actor = actor;
    }

    public Entity getAction(){
        return action;
    }

    public void setAction(Entity action){
        this.action = action;
    }

    public String toString(){
        return "<" + timestamp + "> " + id + "| " + creator.getName() + " " + actor.getName() + " " + action.getName();
    }
}

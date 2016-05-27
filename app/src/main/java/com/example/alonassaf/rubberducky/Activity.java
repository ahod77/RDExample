package com.example.alonassaf.rubberducky;

import java.sql.Timestamp;

/**
 * Created by AlonAssaf on 5/17/2016.
 */
public class Activity {
    int id; //Id holds idx value when entity is retrieved from db. Not used in insert, but can be used in update
    Timestamp timestamp;
    int creator_id, container_id, action_id;
    Entity creator;
    Entity container;
    Entity action;

    //Constructors
    public Activity(int id, Timestamp timestamp, int creator_id, int container_id, int action_id){
        this.id = id;
        this.timestamp = timestamp;
        this.creator_id = creator_id;
        this.container_id = container_id;
        this.action_id = action_id;

        this.creator = this.container = this.action = null;
    }

    public Activity(Timestamp timestamp, int creator_id, int container_id, int action_id){
        this(0, timestamp, creator_id, container_id, action_id);
    }

    public Activity(int creator_id, int container_id, int action_id){
        this(0, null, creator_id, container_id, action_id);
    }

    public Activity(int id, Timestamp timestamp, Entity creator, Entity container, Entity action){
        this.id = id;
        this.timestamp = timestamp;
        this.creator = creator;
        this.container = container;
        this.action = action;
    }

    public Activity(Timestamp timestamp, Entity creator, Entity container, Entity action){
        this(0, timestamp, creator, container, action );
    }

    public Activity(int id, Timestamp timestamp, Entity container, Entity action){
        this(id, timestamp, null, container, action);
    }

    public Activity(Timestamp timestamp, Entity container, Entity action) {
        this(0, timestamp, null, container, action);
    }

    public Activity(Entity container, Entity action) {
        this(0, null, null, container, action);
    }

    public Activity(int id, Entity container, Entity action){
        this(id, null, null, container, action);
    }

    public Activity(Entity creator, Entity container, Entity action){
        this(0, null, creator, container, action);
    }

    public Activity(int id, Entity creator, Entity container, Entity action){
        this(id, null, creator, container, action);
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

    public Entity getContainer(){
        return container;
    }

    public void setContainer(Entity container){
        this.container = container;
    }

    public Entity getAction(){
        return action;
    }

    public void setAction(Entity action){
        this.action = action;
    }

    public int getCreator_id(){
        return creator_id;
    }

    public void setCreator_id(int creator_id){
        this.creator_id = creator_id;
    }

    public int getContainer_id(){
        return container_id;
    }

    public void setContainer_id(int container_id){
        this.container_id = container_id;
    }

    public int getAction_id(){
        return action_id;
    }

    public void setAction_id(int action_id){
        this.action_id = action_id;
    }

    public String toString(){
        if (container == null || action == null)
            return "<" + timestamp + "> " + id + "| " + creator_id + " " + container_id + " " + action_id;
        else if (container != null && action != null && creator != null)
            return "<" + timestamp + "> " + id + "| " + creator.getName() + " " + container.getName() + " " + action.getName();
        else if (container != null && action != null && creator == null)
            return "<" + timestamp + "> " + id + "| " + "no creator" + " " + container.getName() + " " + action.getName();
        else
            return "Problem printing activity string";
    }

        public void buildEntitiesFromIDs(){       //Move this method?
            RubberDuckyDB db = RubberDuckyDB.getInstance();

            if (creator_id > 0){
                creator = db.getEntity(creator_id);
            }

            container = db.getEntity(container_id);
            action = db.getEntity(action_id);
        }

    /*public void buildIdsFromEntities{ //May be unnecessary
        if (creator != null)
            creator_id = creator.getId();
        else
            creator_id = 0;

        if (container != null)
            container_id = container.getId();
        else
            container_id = 0;

        if (action != null)
             action_id = action.getId();
        else
            action_id = 0;
    }*/
}

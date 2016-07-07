package com.example.alonassaf.rubberducky;

import java.sql.Timestamp;
import org.json.JSONObject;

/**
 * Created by AlonAssaf on 5/17/2016.
 */
public class Activity extends DBObject {
    private Timestamp timestamp = null;
    private long creator_id = 0, container_id = 0, action_id = 0;
    private JSONObject action_params = null;
    private Entity creator = null, container = null, action = null;
    private long status = 0;

    //Constructors
    public Activity(long _id, Timestamp timestamp, long creator_id, long container_id, long action_id, JSONObject params, long status){
        super(_id);
        this.timestamp = timestamp;
        this.creator_id = creator_id;
        this.container_id = container_id;
        this.action_id = action_id;
        this.action_params = params == null ? new JSONObject() : params;
        this.status = status;
    }

    public Activity(Entity creator, Entity container, Entity action, JSONObject params, long status) {
        this.creator = creator;
        this.container = container;
        this.action = action;
        this.action_params = params;
        this.status = status;

        this.creator_id = creator == null ? 0 : creator.getRowId();
        this.container_id = container == null ? 0 : container.getRowId();
        this.action_id = action == null ? 0 : action.getRowId();
    }

    //Getters and Setters
    public Timestamp getTimestamp(){
        return timestamp;
    }

    public void setTimestamp (Timestamp timestamp){
        this.timestamp = timestamp;
    }

    public Entity getCreator(){
        if (creator == null && creator_id != 0)
            creator = lazyLoadEntity(creator_id);

        return creator;
    }

//    public void setCreator(Entity creator){
//        this.creator = creator;
//        this.creator_id = creator == null ? 0 : creator.getRowId();
//    }

    public Entity getContainer(){
        if (container == null && container_id != 0)
            container = lazyLoadEntity(container_id);

        return container;
    }

//    public void setContainer(Entity container){
//        this.container = container;
//        this.container_id = container == null ? 0 : container.getRowId();
//    }

    public Entity getAction(){
        if (action == null && action_id != 0)
            action = lazyLoadEntity(action_id);

        return action;
    }

//    public void setAction(Entity action){
//        this.action = action;
//        this.action_id = action == null ? 0 : action.getRowId();
//    }

    public void setStatus(long status) {
        this.status = status;
        markDirty();
    }

    public long getCreator_id(){
        return creator_id;
    }

    public long getContainer_id(){
        return container_id;
    }

    public long getAction_id(){
        return action_id;
    }

    public String getSerializedParams() {
        if (action_params == null)
            return null;
        else
            return action_params.toString();
    }

    public JSONObject getAction_params() {
        return action_params;
    }

    public long getStatus(){
        return status;
    }

    public String toString(){
        if (container == null || action == null)
            return "<" + timestamp + "> " + getRowId() + "| " + creator_id + " " + container_id + " " + action_id;
        else if (container != null && action != null && creator != null)
            return "<" + timestamp + "> " + getRowId() + "| " + creator.getName() + " " + container.getName() + " " + action.getName();
        else if (container != null && action != null && creator == null)
            return "<" + timestamp + "> " + getRowId() + "| " + "no creator" + " " + container.getName() + " " + action.getName();
        else
            return "Problem printing activity string";
    }

    private Entity lazyLoadEntity(long id) {
        return RubberDuckyDB2.Entities.get(id);
    }
}

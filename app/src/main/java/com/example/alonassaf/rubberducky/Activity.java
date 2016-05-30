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

    //Constructors
    public Activity(long _id, Timestamp timestamp, long creator_id, long container_id, long action_id, JSONObject params){
        super(_id);
        this.timestamp = timestamp;
        this.creator_id = creator_id;
        this.container_id = container_id;
        this.action_id = action_id;
        action_params = params;
    }

    public Activity(Entity creator, Entity container, Entity action, JSONObject params) {
        this.creator = creator;
        this.container = container;
        this.action = action;
        this.action_params = params;

        this.creator_id = creator == null ? 0 : creator.getRowId();
        this.container_id = container == null ? 0 : container.getRowId();
        this.action_id = action == null ? 0 : action.getRowId();
    }

    /*
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
    */

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

    public void setCreator(Entity creator){
        this.creator = creator;
        this.creator_id = creator == null ? 0 : creator.getRowId();
    }

    public Entity getContainer(){
        if (container == null && container_id != 0)
            container = lazyLoadEntity(container_id);

        return container;
    }

    public void setContainer(Entity container){
        this.container = container;
        this.container_id = container == null ? 0 : container.getRowId();
    }

    public Entity getAction(){
        if (action == null && action_id != 0)
            action = lazyLoadEntity(action_id);

        return action;
    }

    public void setAction(Entity action){
        this.action = action;
        this.action_id = action == null ? 0 : action.getRowId();
    }

    public long getCreator_id(){
        return creator_id;
    }

    /*
    public void setCreator_id(long creator_id){
        this.creator_id = creator_id;
    }
    */

    public long getContainer_id(){
        return container_id;
    }

    /*
    public void setContainer_id(long container_id){
        this.container_id = container_id;
    }
    */

    public long getAction_id(){
        return action_id;
    }

    /*
    public void setAction_id(long action_id){
        this.action_id = action_id;
    }
    */

    public String getAction_params() {
        if (action_params == null)
            return null;
        else
            return action_params.toString();
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

    /*
        public void buildEntitiesFromIDs(){       //Move this method?
            RubberDuckyDB db = RubberDuckyDB.getInstance();

            if (creator_id > 0){
                creator = db.getEntity(creator_id);
            }

            container = db.getEntity(container_id);
            action = db.getEntity(action_id);
        }
        */

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

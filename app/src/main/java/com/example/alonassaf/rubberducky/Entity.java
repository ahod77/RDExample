package com.example.alonassaf.rubberducky;

/**
 * Created by AlonAssaf on 5/15/2016.
 */
public class Entity extends DBObject {

    private int type;
    private String name;
    private String desc;
    private String fqcn;

    // Constructors
    public Entity(long _id, int type, String name, String desc, Class c) {
        super(_id);
        this.type = type;
        this.name = name;
        this.desc = desc;
        this.fqcn = c == null ? null : c.getName();
    }

    public Entity(int type, String name, String desc, Class c) {
        this(0, type, name, desc, c);
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

    public String toString() {
        return String.format("%1$d| %2$s %3$s %4$s", getRowId(), type, name, desc);
    }

}

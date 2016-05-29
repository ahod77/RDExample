package com.example.alonassaf.rubberducky;

/**
 * Created by Noam Small on 5/29/2016.
 */


/*

    DBObjects are created DIRTY to save to minimize calls to markDirty() from each constructor.

 */

public class DBObject {
    private long id = 0;
    private boolean dirty = true;

    public DBObject() {}

    public DBObject(long _id) {
        id = _id;
    }

    public long getRowId() {
        return id;
    }

    public boolean isNew() {
        return id <= 0;
    }

    public boolean isDirty() {
        return dirty;
    }

    public void markDirty() {
        dirty = true;
    }

    public void markUnDirty() {
        dirty = false;
    }

    public long markSaved(int affectedRows)
    {
        dirty = affectedRows < 1;

        return id;
    }

    public long setRowId(long _id)
    {
        markUnDirty();
        return id = _id;
    }
}

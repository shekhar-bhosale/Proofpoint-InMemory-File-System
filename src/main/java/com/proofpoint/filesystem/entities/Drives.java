package com.proofpoint.filesystem.entities;

/**
 * Represents file system entity of type Drives
 * */

public class Drives extends AbstractFileSystemEntity{

    public Drives(){
        super();
    }

    /**
     * Calculates size of the Drive as total size of all the entities under it.
     * */
    @Override
    public void calculateSize(int totalNumberofDescendants) {
        setSize(totalNumberofDescendants);
    }

}

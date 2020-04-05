package com.proofpoint.filesystem.entities;


/**
 * Represents file system entity of type Folders
 * */

public class Folders extends AbstractFileSystemEntity {

    public Folders(){
        super();
    }

    /**
     * Calculates size of the Folder as total size of all the entities under it.
     * */
    @Override
    public void calculateSize(int totalSizeOfDescendants) {
        setSize(totalSizeOfDescendants);
    }
}

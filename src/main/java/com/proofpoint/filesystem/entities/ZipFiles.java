package com.proofpoint.filesystem.entities;


/**
 * Represents file system entity of type Zipfiles
 * */
public class ZipFiles extends AbstractFileSystemEntity{

    public ZipFiles(){
        super();
    }

    /**
     * Calculates size of the Zipfile as total length of the content divided by 2.
     * */
    @Override
    public void calculateSize(int totalNumberofDescendants) {
        setSize(totalNumberofDescendants/2);
    }
}

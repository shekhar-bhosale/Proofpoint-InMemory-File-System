package com.proofpoint.filesystem.entities;

/**
 * Abstract class indicates any file system entity. This implements Entity interface.
 * */

public abstract class AbstractFileSystemEntity implements Entity{

    /***
     * name: Name of file system entity
     * path: path of the file system entity
     * size: size of the file system entity
     */

    String name;
    String path;
    int size;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void setContent(String newContent){}

}

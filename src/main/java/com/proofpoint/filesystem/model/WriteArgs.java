package com.proofpoint.filesystem.model;

/*
Indicates arguments needed to write to file system entity.
path: Path of the file system entity to move
newContent: Content to write to text file
 */
public class WriteArgs {

    String path;
    String newContent;

    @Override
    public String toString() {
        return "WriteArgs{" +
                "path='" + path + '\'' +
                ", newContent='" + newContent + '\'' +
                '}';
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getNewContent() {
        return newContent;
    }

    public void setNewContent(String newContent) {
        this.newContent = newContent;
    }
}

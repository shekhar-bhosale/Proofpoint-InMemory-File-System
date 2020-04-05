package com.proofpoint.filesystem.model;

/*
Indicates arguments needed to move file system entity.
sourcePath: Source path of the file system entity to move
destinationPath: Destination path of the file system entity to move
 */
public class MoveArgs {

    String sourcePath;
    String destinationPath;

    @Override
    public String toString() {
        return "MoveArgs{" +
                "sourcePath='" + sourcePath + '\'' +
                ", destinationPath='" + destinationPath + '\'' +
                '}';
    }

    public String getSourcePath() {
        return sourcePath;
    }

    public void setSourcePath(String sourcePath) {
        this.sourcePath = sourcePath;
    }

    public String getDestinationPath() {
        return destinationPath;
    }

    public void setDestinationPath(String destinationPath) {
        this.destinationPath = destinationPath;
    }
}

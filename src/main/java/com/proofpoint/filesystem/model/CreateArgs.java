package com.proofpoint.filesystem.model;

/*
Indicates arguments needed to create file system entity.
type: Type of file system entity. Accepted values - Drives, Folders, Zipfiles, Textfiles
name: Name of the file system entity
path: Path of the file system entity
 */
public class CreateArgs {
    String type;
    String name;
    String path;

    @Override
    public String toString() {
        return "CreateArgs{" +
                "type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", path='" + path + '\'' +
                '}';
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

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
}

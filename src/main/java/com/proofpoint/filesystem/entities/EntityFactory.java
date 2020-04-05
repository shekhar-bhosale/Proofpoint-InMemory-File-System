package com.proofpoint.filesystem.entities;

/**
 * Using factory design pattern to create objects of file system entity of different types
 * */

public class EntityFactory {

    public static AbstractFileSystemEntity getInstanceOfEntity(String entityType){

        // Converting type received from user into consistent uppercase format to avoid typos
        entityType = entityType.toUpperCase();

        AbstractFileSystemEntity abstractFileSystemEntity = null;

        switch(entityType){
            case "DRIVE":
                abstractFileSystemEntity = new Drives();
                break;

            case "FOLDER":
                abstractFileSystemEntity = new Folders();
                break;

            case "ZIPFILE":
                abstractFileSystemEntity = new ZipFiles();
                break;

            case "TEXTFILE":
                abstractFileSystemEntity = new TextFiles();
                break;
        }

        return abstractFileSystemEntity;

    }
}

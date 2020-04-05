package com.proofpoint.filesystem.filesystemstructure;

import com.proofpoint.filesystem.entities.AbstractFileSystemEntity;

import java.util.ArrayList;
import java.util.List;


/**
 * File system is represented using N-ary tree.
 * Each node in N-ary tree is represented using below structure.
 *
 * entity: object of type AbstractFileSystemEntity which will represent any of the given types of entities: Drives, Folders, Textfiles, Zipfiles
 * children: it is Arraylist of all the file system entities which are descendants of given entity
 *
 * */

public class FileSystemNode {

    AbstractFileSystemEntity entity;
    List<FileSystemNode> children;

    public FileSystemNode(AbstractFileSystemEntity entity){
        this.entity = entity;
        children = new ArrayList<FileSystemNode>();
    }

}

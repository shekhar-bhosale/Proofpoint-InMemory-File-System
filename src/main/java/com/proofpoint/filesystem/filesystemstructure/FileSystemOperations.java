package com.proofpoint.filesystem.filesystemstructure;

import com.proofpoint.filesystem.controller.FileSystemController;
import com.proofpoint.filesystem.entities.*;
import org.slf4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * File system is represented using N-ary tree.
 * Each node in N-ary tree is represented using FileSystemNode class.
 * This class represents all the file system operations like create, delete, move, writetofile
 */

@Component
@Scope("singleton")
public class FileSystemOperations {

    /**
     * Creating root node with null entity
     * FileSystem is N-ary tree and everything is descendant of root node. Root node is dummy node with no data or entity.
     */
    static FileSystemNode root = new FileSystemNode(null);

    private Logger logger = FileSystemController.getLogger();

    public static FileSystemNode getRoot() {
        return root;
    }

    /**
     * Method creates/inserts the file system entity into the N-ary tree.
     */
    public boolean insertNode(final String type, final String name, String path) throws Exception {

        path = path + name;
        //Creating object of different type of file system entity by using EntityFactory
        AbstractFileSystemEntity abstractFileSystemEntity = EntityFactory.getInstanceOfEntity(type);
        abstractFileSystemEntity.setName(name);
        abstractFileSystemEntity.setPath(path);

        FileSystemNode nodeToInsert = new FileSystemNode(abstractFileSystemEntity);

        //If entity is of type Textfile, then set its children list to null as Text files are always at leaf of the tree
        if (type.toUpperCase().equals("TEXTFILE")) {
            nodeToInsert.children = null;
        }

        return insertNode(nodeToInsert, path);

    }

    private boolean insertNode(FileSystemNode nodeToInsert, final String destinationPath) throws Exception {

        logger.info("Inside insertNode method:");

        FileSystemNode runner = root;
        String path = destinationPath;
        //Splitting path by '\' to get distinct tokens
        String pathList[] = path.split("\\\\");

        logger.info("Input node details: Name:" + nodeToInsert.entity.getName() + "\t Path:" + nodeToInsert.entity.getPath() + "\t Destination path:" + destinationPath);

        int level;
        int length = pathList.length;

        boolean isPresent = false;
        //Pushing all the nodes on path till parent to stack. This will be used to update sizes till drive node.
        Stack<FileSystemNode> stackOfAncestors = new Stack<>();
        boolean isInserted = false;

        //Iterating through all the distinct entities on path
        for (level = 0; level < length; level++) {
            String curEntity = pathList[level];

            //Setting isPresent flag to indicate if entity is present in given list of children
            isPresent = false;

            //Checking if current entity on path is present in list of children
            for (FileSystemNode curNode : runner.children) {
                if (curNode.entity.getName().equals(curEntity)) {
                    stackOfAncestors.push(curNode);
                    runner = curNode;
                    isPresent = true;
                    break;
                }
            }

            //Insert node in the tree if reached at the end of the path
            if (level == length - 1) {

                if (!isPresent) {
                    //If parent node is of type TextFile then throws error because any entity can't be added under Textfile
                    if (runner.entity instanceof TextFiles) {
                        logger.info("Entity cannot be added inside TextFile.");
                        throw new Exception("Entity cannot be added inside TextFile.");
                    }
                    //Add entity to list of children
                    runner.children.add(nodeToInsert);
                    isInserted = true;

                } else {
                    //If entity already present in list of children then throws error of duplicate name
                    logger.info("Cannot create entity with same name already present.");
                    throw new Exception("Cannot create entity with same name already present.");
                }

            }


        }

        if (isInserted) {
            logger.info("Node inserted. Updating size.");
            //If node is inserted in tree then update size of all parents on path
            updateSize(stackOfAncestors);
        }

        //Returns if entity successfully created or not
        return isInserted;

    }

    /**
     * This method updates size of all the nodes on a path of modified node
     * */
    private void updateSize(Stack<FileSystemNode> stackOfAncestors) {

        //Calls calculatesize method of each entity in stack of parent nodes to update the size
        while (!stackOfAncestors.isEmpty()) {
            FileSystemNode currentNode = stackOfAncestors.pop();
            currentNode.entity.calculateSize(getTotalSizeOfDescendants(currentNode));
        }
        logger.info("Size updated.");
    }

    /**
     * This method gives total size of descendants.
     * For textfile, Totalsizeofdescendants indicates size as a length of content
     * For all other entity types, Totalsizeofdescendants is total size of all the files under it added with total number entities under it
     * */
    private int getTotalSizeOfDescendants(final FileSystemNode inputNode) {

        int totalSizeOfDescendants = 0;

        FileSystemNode currentNode = inputNode;

            //If entity is Textfile then size is length of content
            if (currentNode.entity instanceof TextFiles) {
                totalSizeOfDescendants += ((TextFiles) currentNode.entity).getContent().length();
            } else {
                //For all other entity types, size is Size of all the entities under it + number of children
                totalSizeOfDescendants += currentNode.children.size();  //Remove this if you do not want number of children included in size

                for (FileSystemNode currentChild : currentNode.children) {
                    totalSizeOfDescendants += currentChild.entity.getSize();
                }
            }

        return totalSizeOfDescendants;

    }

    /**
     * Deletes file system entity at given path
     * */
    public boolean deleteNode(String path) {
        FileSystemNode nodeDeleted = deleteNodeOpr(path);
        return nodeDeleted != null;
    }

    /**
     * Deletes the node in N-ary tree and returns deleted node.
     * */
    private FileSystemNode deleteNodeOpr(String path) {

        String pathList[] = path.split("\\\\");

        FileSystemNode runner = root;
        FileSystemNode parentRunner = root;
        FileSystemNode deletedNode = null;
        int level;
        int length = pathList.length;
//        boolean isPresent = false;
        Stack<FileSystemNode> stackOfAncestors = new Stack<>();

        for (level = 0; level < length; level++) {
            String curEntity = pathList[level];
//            System.out.println("\n Current pathlist value:"+curEntity);
//            isPresent = false;
            for (FileSystemNode curNode : runner.children) {
                if (curNode.entity.getName().equals(curEntity)) {
                    stackOfAncestors.push(curNode);
                    parentRunner = runner;
                    runner = curNode;
//                    isPresent = true;
                    break;
                }
            }

        }

        if (level == length) {
            deletedNode = runner;
            parentRunner.children.remove(runner);
        }

        logger.info("Node deleted. Updating size.");

        updateSize(stackOfAncestors);

        return deletedNode;

    }

    /**
     * This method moves file system entity from one location to another.
     * It first deletes the entity from its old path and then inserts it to new path on N-ary tree
     * */
    public boolean moveNode(String sourcePath, String destinationPath) throws Exception {
        logger.info("Inside moveNode method:");
        //Deletes the entity from old path and returns deleted node
        FileSystemNode deletedNode = deleteNodeOpr(sourcePath);

        logger.info("Node " + deletedNode.entity.getName() + " deleted from old path");

        //Change the path of deletednode to point to new path
        destinationPath = destinationPath + deletedNode.entity.getName();

        deletedNode.entity.setPath(destinationPath);

        //Insert the deleted node to new path in N-ary tree
        boolean isInserted = insertNode(deletedNode, destinationPath);


        if (isInserted) {
            logger.info("If folder or zipfile moved, update path of children.");
            //If folder or zipfile moved then update path of all children under moved entity
            if (deletedNode.entity instanceof Folders || deletedNode.entity instanceof ZipFiles) {
                if (deletedNode.children.size() > 0) {
                    updatePathOfDescendants(deletedNode);
                }
            }
        }

        return isInserted;
    }


/**
 * If entity moved then update path of all children under moved entity
 * */
    private void updatePathOfDescendants(FileSystemNode fileSystemNode) throws Exception {
        /*
        Using BFS approach to update path of all children
         */
        try {
            logger.info("Inside updatePathOfDescendants: size of children:" + fileSystemNode.children.size());
            FileSystemNode runner = fileSystemNode;
            Queue<FileSystemNode> levelQueue = new LinkedList<>();
            levelQueue.add(runner);

            while (!levelQueue.isEmpty()) {
                FileSystemNode currentNode = levelQueue.poll();
                if (!(currentNode.entity instanceof TextFiles) && currentNode.children != null && !(currentNode.children.isEmpty()) && currentNode.children.size() > 0) {
                    for (FileSystemNode currentChild : currentNode.children) {
                        String updatedPath = currentNode.entity.getPath() + "\\" + currentChild.entity.getName();
                        currentChild.entity.setPath(updatedPath);
                        levelQueue.add(currentChild);
                    }
                }
            }

            logger.info("Paths updated for all children");
        } catch (Exception e) {
            logger.info("Exception inside updatePathOfDescendants:" + e.getMessage());
            throw new Exception(e.getMessage());
        }

    }

    /**
     * Writes to file system entity.
     * Basically this changes the Content of Textfile
     * */
    public boolean writeToFile(String filePath, String newContent) throws Exception {

        boolean result = false;

        String pathList[] = filePath.split("\\\\");

        int level;
        int length = pathList.length;
        FileSystemNode runner = root;
        boolean isPresent = false;
        //Maintaining stack of all the nodes received on source path
        Stack<FileSystemNode> stackOfAncestors = new Stack<>();

        for (level = 0; level < length; level++) {
            String curEntity = pathList[level];
            isPresent = false;
            for (FileSystemNode curNode : runner.children) {
                if (curNode.entity.getName().equals(curEntity)) {
                    stackOfAncestors.push(curNode);
                    runner = curNode;
                    isPresent = true;
                    break;
                }
            }

        }

        if (level == length) {
            if (isPresent) {
                runner.entity.setContent(newContent);
                updateSize(stackOfAncestors);
                result = true;
            } else {
                logger.error("File not present at given path.");
                throw new Exception("File not present at given path.");
            }
        }

        return result;

    }

    /**
     * This method is used to display entire file system every time any operation is performed
     * This uses preorder traversal of the N-ary tree
     * */
    public void displayFileSystem() {
        logger.info("Preorder traversal of filesystem tree:");
        preorderTraversal(root);
    }

    private void preorderTraversal(FileSystemNode root) {

        Stack<FileSystemNode> stack = new Stack<>();
        stack.add(root);

        while (!stack.empty()) {
            root = stack.pop();
            if (root.entity != null) {

                System.out.println("Node name:" + root.entity.getName() + "\t Node size:" + root.entity.getSize() + "\t Node path:" + root.entity.getPath());
                if (root.entity instanceof TextFiles) {
                    if (!((TextFiles) root.entity).getContent().isEmpty()) {
                        System.out.println("File contents:" + ((TextFiles) root.entity).getContent());
                    }
                }
            }

            if (!(root.entity instanceof TextFiles)) {
                for (int i = root.children.size() - 1; i >= 0; i--)
                    stack.add(root.children.get(i));
            }

        }

    }


}

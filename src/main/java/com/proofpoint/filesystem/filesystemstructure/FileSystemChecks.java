package com.proofpoint.filesystem.filesystemstructure;

import com.proofpoint.filesystem.controller.FileSystemController;
import com.proofpoint.filesystem.entities.TextFiles;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

/**
 * Class performs all file system checks before performing any filesystem operation
 * */

@Component
@Scope("singleton")
public class FileSystemChecks {

    @Autowired
    FileSystemOperations fileSystemOperations;

    private Logger logger = FileSystemController.getLogger();

    /**
     * Checks if path is empty before creating Drive entity type.
     * Also checks if path is not empty and exists in system for entities other than Drive
     * */

    public boolean checkPath(final String type, final String path) {

        logger.info("Input values to checkPath: type:" + type + " path:" + path);

        if (type.toUpperCase().equals("DRIVE")) {
            if (path.length() > 0 || !(path.isEmpty())) {
                return false;
            } else {
                return true;
            }
        } else {
            if (path.length() == 0 || path.isEmpty()) {
                return false;
            } else if (!path.endsWith("\\")) {
                return false;
            } else if (!isPathExists(path)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Checks if given path valid destination path for moving or creating file system entity.
     * Returns false if leaf entity is Textfile
     * */
    public boolean isValidDestPath(String path) {
        String pathList[] = path.split("\\\\");
        String lastEntity = pathList[pathList.length - 1];

        FileSystemNode runner = FileSystemOperations.getRoot();
        boolean isPresent;

        for (int level = 0; level < pathList.length; level++) {
            String curEntity = pathList[level];
//            logger.info("Current path entity:"+curEntity);
//            isPresent = false;

            for (FileSystemNode curNode : runner.children) {
                if (curNode.entity.getName().equals(curEntity)) {
//                    logger.info(curEntity+" is present in children of parent");
                    runner = curNode;
//                    isPresent = true;
                    break;
                }
            }

            if (level == pathList.length - 1) {
                if (runner.entity instanceof TextFiles) {
                    return false;
                }
            }

        }

        return true;
    }

    /**
     * Checks if given path is only drive (not any other entity)
     * */

    private boolean isDrive(String sourcePath){
        String pathList[] = sourcePath.split("\\\\");
        String pathValue = pathList[0];
        boolean isDrive= Pattern.compile("[A-Z]:").matcher(pathValue).matches();
        return isDrive;
    }

    /**
     * Checks if move operation is trying to move drive
     * */

    public boolean isMovingDrive(String sourcePath){
        /*
            Drive name is considered single uppercase alphabetic character followed by ':'.
            So if sourcepath is only drive name then method will return false because Drive cannot be moved to some other path.
         */
        String pathList[] = sourcePath.split("\\\\");
        if(pathList.length>1){
            return false;
        }else if(isDrive(sourcePath)){
            if(pathList.length==1)
                return true;
        }

        return false;
    }

    /**
     * Checks if given path exists in file system
     * */
    public boolean isPathExists(String path) {

        logger.info("Inside isPathExists: path:" + path);

        if (path.isEmpty() || !path.endsWith("\\") || path.length() == 0) {
            return false;
        }

        String pathList[] = path.split("\\\\");

        FileSystemNode runner = FileSystemOperations.getRoot();
        boolean isPresent;

        for (int level = 0; level < pathList.length; level++) {
            String curEntity = pathList[level];
//            logger.info("Current path entity:"+curEntity);
            isPresent = false;

            for (FileSystemNode curNode : runner.children) {
                if (curNode.entity.getName().equals(curEntity)) {
//                    logger.info(curEntity+" is present in children of parent");
                    runner = curNode;
                    isPresent = true;
                    break;
                }
            }

            if (!isPresent) {
                logger.info(curEntity + " is NOT present in children of parent");
                return false;
            }
        }
        return true;
    }
}

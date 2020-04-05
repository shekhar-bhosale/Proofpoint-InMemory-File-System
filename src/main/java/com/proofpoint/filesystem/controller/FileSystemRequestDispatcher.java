package com.proofpoint.filesystem.controller;

import com.proofpoint.filesystem.filesystemstructure.FileSystemChecks;
import com.proofpoint.filesystem.filesystemstructure.FileSystemOperations;
import com.proofpoint.filesystem.model.CreateArgs;
import com.proofpoint.filesystem.model.MoveArgs;
import com.proofpoint.filesystem.model.WriteArgs;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * This component will receive calls from Controller service and dispatch it to appropriate method in FileSystemOperations component
 */

@Component
@Scope("singleton")
public class FileSystemRequestDispatcher {

    /* Component performing file system operations like create, delete, move, write to file */
    @Autowired
    private FileSystemOperations fileSystemOperations;

    /* Component to perform all necessary checks before performing file system operations */
    @Autowired
    private FileSystemChecks fileSystemChecks;

    private Logger logger = FileSystemController.getLogger();

    public String create(final CreateArgs payload) throws Exception {

        try {
            logger.info("Received POST call for creating file system entity..");

            logger.info("Received body:" + payload.toString());

            if (!fileSystemChecks.checkPath(payload.getType(), payload.getPath())) {
                throw new Exception("Invalid path while creating file system entity");
            }

            if (!fileSystemChecks.isValidDestPath(payload.getPath())) {
                throw new Exception ("File system entity cannot be created under another file");
            }

            final boolean result = fileSystemOperations.insertNode(payload.getType(), payload.getName(), payload.getPath());
            fileSystemOperations.displayFileSystem();

            if (result) {
                return "Successfully created file system entity";
            } else {
                return "Failure in creating file system entity";
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new Exception( e.getMessage());
        }

    }

    public String delete(final String pathToDelete) throws Exception {

        try {
            logger.info("Received POST call for deleting file system entity..");

            logger.info("Received body:" + pathToDelete);

            if (!fileSystemChecks.isPathExists(pathToDelete)) {
                throw new Exception ("Invalid path to delete file system entity");
            }

            final boolean result = fileSystemOperations.deleteNode(pathToDelete);
            fileSystemOperations.displayFileSystem();

            if (result) {
                return "Successfully deleted file system entity";
            } else {
                return "Failure in deleting file system entity";
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new Exception( e.getMessage());
        }

    }

    public String move(final MoveArgs payload) throws Exception {

        try {
            logger.info("Received POST call for moving file system entity..");

            logger.info("Received body:" + payload.toString());

            final String sourcepath = payload.getSourcePath().charAt(payload.getSourcePath().length() - 1) == '\\' ? payload.getSourcePath() : payload.getSourcePath() + "\\";
            final String destinationpath = payload.getDestinationPath().charAt(payload.getDestinationPath().length() - 1) == '\\' ? payload.getDestinationPath() : payload.getDestinationPath() + "\\";

            if (!fileSystemChecks.isPathExists(sourcepath)) {
                throw new Exception ("Invalid source path while creating file system entity");
            }

            if (!fileSystemChecks.isPathExists(destinationpath)) {
                throw new Exception ("Invalid destination path while creating file system entity");
            } else if (!fileSystemChecks.isValidDestPath(payload.getDestinationPath())) {
                throw new Exception ("File system entity cannot be moved under another file");
            }

            if(fileSystemChecks.isMovingDrive(sourcepath)){
                throw new Exception("Drive cannot be moved inside other entity");
            }

            final boolean result = fileSystemOperations.moveNode(payload.getSourcePath(), payload.getDestinationPath());
            logger.info("File system after move operation");
            fileSystemOperations.displayFileSystem();

            if (result) {
                return "Successfully moved file system entity";
            } else {
                return "Failure in moving file system entity";
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new Exception( e.getMessage());
        }

    }

    public String writeToFile( final WriteArgs payload) throws Exception {

        try {

            logger.info("Received POST call for writing to file system entity..");

            logger.info("Received body:" + payload.toString());

            if (!fileSystemChecks.isPathExists(payload.getPath() + "\\")) {
                throw new Exception ("Invalid path for writing to file system entity");
            } else if (fileSystemChecks.isValidDestPath(payload.getPath() + "\\")) {
                throw new Exception ("Text file not present at given path");
            }

            final boolean result = fileSystemOperations.writeToFile(payload.getPath(), payload.getNewContent());
            logger.info("File system after write operation");
            fileSystemOperations.displayFileSystem();

            if (result) {
                return "Successfully written to file system entity";
            } else {
                return "Failure in writing file system entity";
            }

        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new Exception( e.getMessage());
        }
    }

}

package com.proofpoint.filesystem.controller;

import com.proofpoint.filesystem.model.CreateArgs;
import com.proofpoint.filesystem.model.MoveArgs;
import com.proofpoint.filesystem.model.WriteArgs;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Controller service that will receive api calls
 Base URL: http://localhost:8080/filesystem/
 * */


@RestController
@RequestMapping("/filesystem")
public class FileSystemController {

    private static Logger logger = LoggerFactory.getLogger(FileSystemController.class.getName());

    public static Logger getLogger() {
        return logger;
    }

    @Autowired
    private FileSystemRequestDispatcher fileSystemRequestDispatcher;

    /*
    Usage: receives POST API call to create file system entity.
    Args: Arguments mapped to CreateArgs class
    Returns: Message indicating success, failure, errors
     */
    @RequestMapping(value = "/create",
            method = RequestMethod.POST,
            consumes = "application/json")
    public String create(@RequestBody final CreateArgs payload) {

        try {
            return fileSystemRequestDispatcher.create(payload);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return e.getMessage();
        }

    }

    /*
   Usage: receives POST API call to delete file system entity.
   Args: Path of the entity to delete
   Returns: Message indicating success, failure, errors
    */
    @RequestMapping(value = "/delete",
            method = RequestMethod.POST,
            consumes = "text/plain")
    public String delete(@RequestBody final String pathToDelete) {

        try {
          return fileSystemRequestDispatcher.delete(pathToDelete);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return e.getMessage();
        }

    }

    /*
  Usage: receives POST API call to move file system entity.
  Args: Arguments mapped to MoveArgs class
  Returns: Message indicating success, failure, errors
   */
    @RequestMapping(value = "/move",
            method = RequestMethod.POST,
            consumes = "application/json")
    public String move(@RequestBody final MoveArgs payload) {

        try {
            return fileSystemRequestDispatcher.move(payload);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return e.getMessage();
        }

    }

      /*
  Usage: receives POST API call to write to text file.
  Args: Arguments mapped to WriteArgs class
  Returns: Message indicating success, failure, errors
   */
    @RequestMapping(value = "/writetofile",
            method = RequestMethod.POST,
            consumes = "application/json")
    public String writeToFile(@RequestBody final WriteArgs payload) {

        try {

            return fileSystemRequestDispatcher.writeToFile(payload);

        } catch (Exception e) {
            logger.error(e.getMessage());
            return e.getMessage();
        }
    }
}

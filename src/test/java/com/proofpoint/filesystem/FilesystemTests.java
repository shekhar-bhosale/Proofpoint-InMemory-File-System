package com.proofpoint.filesystem;

import com.proofpoint.filesystem.controller.FileSystemRequestDispatcher;
import com.proofpoint.filesystem.model.CreateArgs;
import com.proofpoint.filesystem.model.MoveArgs;
import com.proofpoint.filesystem.model.WriteArgs;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Unit tests for application
 * To maintain order of execution of test cases, methods are named alphabetically starting with testA...
 * */

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FilesystemTests {

    @Autowired
    FileSystemRequestDispatcher fileSystemRequestDispatcher;

    //Test cases for creating file system entity

    @Test
    public void testAcreateDrive() throws Exception {

        CreateArgs createArgs = new CreateArgs();
        createArgs.setType("Drive");
        createArgs.setPath("");
        createArgs.setName("C:");

        String result = fileSystemRequestDispatcher.create(createArgs);
        Assert.assertEquals(result, "Successfully created file system entity");
    }

    @Test(expected = Exception.class)
    public void testBcreateDriveWrongPath() throws Exception {

        CreateArgs createArgs = new CreateArgs();
        createArgs.setType("Drive");
        createArgs.setPath("/dir1/dir2");
        createArgs.setName("C:");

        fileSystemRequestDispatcher.create(createArgs);
    }

    @Test
    public void testCcreateFolder() throws Exception {

//		createDrive();
        CreateArgs createArgs = new CreateArgs();
        createArgs.setType("folder");
        createArgs.setPath("C:\\");
        createArgs.setName("dir1");

        String result = fileSystemRequestDispatcher.create(createArgs);
        Assert.assertEquals(result, "Successfully created file system entity");
    }

    @Test(expected = Exception.class)
    public void testDcreateFolderWrongPath() throws Exception {

        CreateArgs createArgs = new CreateArgs();
        createArgs.setType("folder");
        createArgs.setPath("");
        createArgs.setName("dir1");

        fileSystemRequestDispatcher.create(createArgs);
    }

    @Test
    public void testEcreateTextFile() throws Exception {

//		createDrive();
        CreateArgs createArgs = new CreateArgs();
        createArgs.setType("textfile");
        createArgs.setPath("C:\\");
        createArgs.setName("file1.txt");

        String result = fileSystemRequestDispatcher.create(createArgs);
        Assert.assertEquals(result, "Successfully created file system entity");
    }

    @Test(expected = Exception.class)
    public void testFcreateTextFileWrongPath() throws Exception {

//		createDrive();
        CreateArgs createArgs = new CreateArgs();
        createArgs.setType("textfile");
        createArgs.setPath("");
        createArgs.setName("file1.txt");

        fileSystemRequestDispatcher.create(createArgs);
    }

    @Test(expected = Exception.class)
    public void testGcreateTextFileChild() throws Exception {

//		createTextFile();
        CreateArgs createArgs = new CreateArgs();
        createArgs.setType("folder");
        createArgs.setPath("C:\\file1.txt\\");
        createArgs.setName("testdir1");

        fileSystemRequestDispatcher.create(createArgs);

    }

    @Test
    public void testHcreateZipFile() throws Exception {

//		createDrive();
        CreateArgs createArgs = new CreateArgs();
        createArgs.setType("zipfile");
        createArgs.setPath("C:\\");
        createArgs.setName("file1.zip");

        String result = fileSystemRequestDispatcher.create(createArgs);
        Assert.assertEquals(result, "Successfully created file system entity");

    }

    @Test(expected = Exception.class)
    public void testIcreateZipFileWrongPath() throws Exception {
        CreateArgs createArgs = new CreateArgs();
        createArgs.setName("file2.zip");
        createArgs.setPath("");
        createArgs.setType("zipfile");

        fileSystemRequestDispatcher.create(createArgs);

    }

    @Test
    public void testJcreateZipFileChild() throws Exception {

        //createZipFile();
        CreateArgs createArgs = new CreateArgs();
        createArgs.setType("textfile");
        createArgs.setPath("C:\\file1.zip\\");
        createArgs.setName("textfile.txt");

        String result = fileSystemRequestDispatcher.create(createArgs);
        Assert.assertEquals(result, "Successfully created file system entity");
    }


    //Test cases for moving file system entity

    @Test
    public void testKmoveTextFile() throws Exception {

//		createDrive();
//		createTextFile();

        CreateArgs createArgs = new CreateArgs();
        createArgs.setType("folder");
        createArgs.setPath("C:\\");
        createArgs.setName("dir2");

        fileSystemRequestDispatcher.create(createArgs);

        MoveArgs moveArgs = new MoveArgs();
        moveArgs.setSourcePath("C:\\file1.txt");
        moveArgs.setDestinationPath("C:\\dir2\\");

        String result = fileSystemRequestDispatcher.move(moveArgs);
        Assert.assertEquals(result, "Successfully moved file system entity");
    }

    @Test(expected = Exception.class)
    public void testLmoveTextFileWrongPath() throws Exception {

        MoveArgs moveArgs = new MoveArgs();
        moveArgs.setSourcePath("C:\\dir2\\file1.txt");
        moveArgs.setDestinationPath("C:\\dir4\\");

        fileSystemRequestDispatcher.move(moveArgs);
    }

    @Test(expected = Exception.class)
    public void testMmoveTextFileEmptyPath() throws Exception {

        MoveArgs moveArgs = new MoveArgs();
        moveArgs.setSourcePath("C:\\dir2\\file1.txt");
        moveArgs.setDestinationPath("");

        fileSystemRequestDispatcher.move(moveArgs);
    }

    @Test(expected = Exception.class)
    public void testNmoveDrive() throws Exception {

        MoveArgs moveArgs = new MoveArgs();
        moveArgs.setSourcePath("C:");
        moveArgs.setDestinationPath("C:\\dir2\\");

        fileSystemRequestDispatcher.move(moveArgs);

    }

    @Test
    public void testOmoveFolder() throws Exception {

        MoveArgs moveArgs = new MoveArgs();
        moveArgs.setSourcePath("C:\\dir1\\");
        moveArgs.setDestinationPath("C:\\dir2\\");

        String result = fileSystemRequestDispatcher.move(moveArgs);
        Assert.assertEquals(result, "Successfully moved file system entity");
    }


    @Test(expected = Exception.class)
    public void testPmoveFolderWrongPath() throws Exception {

        MoveArgs moveArgs = new MoveArgs();
        moveArgs.setSourcePath("C:\\dir1\\");
        moveArgs.setDestinationPath("C:\\dir6\\");

        fileSystemRequestDispatcher.move(moveArgs);

    }

    @Test(expected = Exception.class)
    public void testQmoveFolderEmptyPath() throws Exception {

        MoveArgs moveArgs = new MoveArgs();
        moveArgs.setSourcePath("C:\\dir1\\");
        moveArgs.setDestinationPath("");

        fileSystemRequestDispatcher.move(moveArgs);

    }

    @Test
    public void testRmoveZipFile() throws Exception {

        MoveArgs moveArgs = new MoveArgs();
        moveArgs.setSourcePath("C:\\file1.zip");
        moveArgs.setDestinationPath("C:\\dir2\\");

        String result = fileSystemRequestDispatcher.move(moveArgs);
        Assert.assertEquals(result, "Successfully moved file system entity");
    }


    @Test(expected = Exception.class)
    public void testSmoveZipFileWrongPath() throws Exception {

        MoveArgs moveArgs = new MoveArgs();
        moveArgs.setSourcePath("C:\\dir2\\file1.zip");
        moveArgs.setDestinationPath("C:\\dir10\\");

        fileSystemRequestDispatcher.move(moveArgs);

    }

    @Test(expected = Exception.class)
    public void testTmoveZipFileEmptyPath() throws Exception {

        MoveArgs moveArgs = new MoveArgs();
        moveArgs.setSourcePath("C:\\dir2\\file1.zip");
        moveArgs.setDestinationPath("");

        fileSystemRequestDispatcher.move(moveArgs);

    }

    //Write to file test

    @Test
    public void testUwriteToFile() throws Exception {

        CreateArgs createArgs = new CreateArgs();
        createArgs.setType("Drive");
        createArgs.setPath("");
        createArgs.setName("D:");

        fileSystemRequestDispatcher.create(createArgs);
        WriteArgs writeArgs = new WriteArgs();
        writeArgs.setPath("C:\\dir2\\file1.zip\\textfile.txt");
        String content = "This is test data for test file.";
        writeArgs.setNewContent(content);
        String result = fileSystemRequestDispatcher.writeToFile(writeArgs);
        Assert.assertEquals(result, "Successfully written to file system entity");
    }


    @Test(expected = Exception.class)
    public void testVwriteToFileWrongPath() throws Exception {

        WriteArgs writeArgs = new WriteArgs();
        writeArgs.setPath("C:\\file1.zip\\textfile2.txt");
        String content = "This is test data for test file.";
        writeArgs.setNewContent(content);
       fileSystemRequestDispatcher.writeToFile(writeArgs);
    }



}

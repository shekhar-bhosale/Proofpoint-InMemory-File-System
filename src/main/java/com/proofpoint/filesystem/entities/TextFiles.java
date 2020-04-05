package com.proofpoint.filesystem.entities;


/**
 * Represents file system entity of type Textfiles
 * */
public class TextFiles extends AbstractFileSystemEntity{

    //Indicates content in text file
    String content;

    public TextFiles(){
        super();
        content="";
    }

    /**
     * Calculates size of the Textfile as total length of the content.
     * */
    public void calculateSize(int totalSizeOfDescendants) {
        setSize(totalSizeOfDescendants);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}

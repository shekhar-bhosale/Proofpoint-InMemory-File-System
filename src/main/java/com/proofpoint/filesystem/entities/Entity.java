package com.proofpoint.filesystem.entities;

/**
 * Interface declares a method calculateSize() which will have different implementation for each entity type.
 */
public interface Entity {
    /**
     *
     * @param totalSizeOfDescendants: Total size of the file system entity
     */
    public void calculateSize(int totalSizeOfDescendants);
}

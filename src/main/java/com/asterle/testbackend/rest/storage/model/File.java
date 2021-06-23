package com.asterle.testbackend.rest.storage.model;

import javax.persistence.Lob;
import lombok.Data;

@Data
public class File {

    private String fileName;

    @Lob
    private byte[] fileContent;

    public File(final String fileName, final byte[] fileContent) {
        this.fileName = fileName;
        this.fileContent = fileContent;
    }
}

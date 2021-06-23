package com.asterle.testbackend.database.model;

import com.asterle.testbackend.database.audit.Auditable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Entity
@NoArgsConstructor
@Data
public class DatabaseFile extends Auditable<String> {

    @Id
    @GeneratedValue
    private Long fileId;

    private String fileName;

    @Lob
    private byte[] fileContent;

    public DatabaseFile(final String fileName, final byte[] fileContent) {
        this.fileName = fileName;
        this.fileContent = fileContent;
    }
}

package com.asterle.testbackend.rest.storage.mapper;

import com.asterle.testbackend.database.model.DatabaseFile;
import com.asterle.testbackend.rest.storage.model.File;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class FileMapper {

    public DatabaseFile filesToDbFiles(final File file) {
        return new DatabaseFile(file.getFileName(), file.getFileContent());
    }

    public File dbFilesToFiles(final DatabaseFile dbFile) {
        return new File(dbFile.getFileName(), dbFile.getFileContent());
    }

    public List<DatabaseFile> filesToDbFiles(final List<File> files) {
        return files.stream().map(this::filesToDbFiles).collect(Collectors.toList());
    }

    public List<File> dbFilesToFiles(final List<DatabaseFile> dbFiles) {
        return dbFiles.stream().map(this::dbFilesToFiles).collect(Collectors.toList());
    }

}

package com.asterle.testbackend.rest.storage.service;

import com.asterle.testbackend.database.model.DatabaseFile;
import com.asterle.testbackend.database.repository.FileRepository;
import com.asterle.testbackend.rest.storage.mapper.FileMapper;
import com.asterle.testbackend.rest.storage.model.File;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class StorageService {

    private final FileRepository repository;
    private final FileMapper fileMapper;

    public StorageService(final FileRepository repository,
                          final FileMapper fileMapper) {
        this.repository = repository;
        this.fileMapper = fileMapper;
    }

    public Long createFile(final File file) {
        final DatabaseFile savedFile = repository.save(fileMapper.filesToDbFiles(file));

        return savedFile.getFileId();
    }

    public List<Long> createFiles(final List<File> files) {
        final List<DatabaseFile> dbFiles = repository.saveAll(fileMapper.filesToDbFiles(files));
        return dbFiles.stream().map(DatabaseFile::getFileId).collect(Collectors.toList());
    }

    public List<File> getFiles() {
        return fileMapper.dbFilesToFiles(repository.findAll());
    }
}

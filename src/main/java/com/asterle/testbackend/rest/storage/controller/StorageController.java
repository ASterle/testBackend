package com.asterle.testbackend.rest.storage.controller;

import com.asterle.testbackend.rest.storage.model.File;
import com.asterle.testbackend.rest.storage.service.StorageService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class StorageController {

    private final StorageService storageService;

    public StorageController(final StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping(value = {"/storage"}, produces = {"application/json"})
    @ResponseBody
    public List<File> getFiles() {
        return storageService.getFiles();
    }

    @PostMapping(value = {"/storage"})
    public HttpStatus createFiles(@RequestBody final List<File> files) {
        try {
            final List<Long> fileIds = storageService.createFiles(files);

            if (fileIds != null && fileIds.size() == files.size()) {
                return HttpStatus.OK;
            } else {
                return HttpStatus.INTERNAL_SERVER_ERROR;
            }

        } catch (final Exception e) {
            return HttpStatus.BAD_REQUEST;
        }
    }

}
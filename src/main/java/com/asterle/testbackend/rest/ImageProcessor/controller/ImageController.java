package com.asterle.testbackend.rest.ImageProcessor.controller;

import com.asterle.testbackend.rest.ImageProcessor.model.Image;
import com.asterle.testbackend.rest.ImageProcessor.service.ImageService;
import javax.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ImageController {

    private final ImageService imageService;

    public ImageController(final ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping(value = {"/image/{method}/{parameter}", "/image/{method}"})
    public byte[] convertImage(@RequestBody final byte[] imageBytes, @NotNull @PathVariable final String method, @PathVariable(required = false) final String parameter) {
        return imageService.process(new Image(imageBytes), method, parameter);
    }

}
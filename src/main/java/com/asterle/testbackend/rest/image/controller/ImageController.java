package com.asterle.testbackend.rest.image.controller;

import com.asterle.testbackend.rest.image.model.Image;
import com.asterle.testbackend.rest.image.service.ImageService;
import java.util.Base64;
import javax.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class ImageController {

    private final ImageService imageService;

    public ImageController(final ImageService imageService) {
        this.imageService = imageService;
    }

    @PostMapping(value = {"/image/{method}/{parameter}", "/image/{method}"})
    public byte[] convertImage(@RequestBody final String imageBase64, @NotNull @PathVariable final String method,
                               @PathVariable(required = false) final String parameter) {
        final byte[] imageBytes = Base64.getDecoder().decode(imageBase64);

        return imageService.process(new Image(imageBytes), method, parameter);
    }

}
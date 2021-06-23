package com.asterle.testbackend.rest.image;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.asterle.testbackend.rest.image.model.Image;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ImageIT {

    private File getFile(final String fileName) throws URISyntaxException {
        final ClassLoader classLoader = getClass().getClassLoader();

        final URL resource = classLoader.getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("File not found: " + fileName);
        }

        final File file = new File(resource.toURI());

        return file;
    }

    @Before
    public void setupURL() {
        // here we setup the default URL and API base path to use throughout the tests
        RestAssured.baseURI = "http://localhost:8080";
        RestAssured.basePath = "/image";
    }

    @Test
    public void color_grayscale() throws IOException, URISyntaxException {
        final File file = getFile("image/image.png");
        final byte[] image = Files.readAllBytes(file.toPath());
        final Image colorImage = new Image(image);

        final ValidatableResponse response = given().contentType(ContentType.BINARY)
                .when()
                .body(image)
                .post("/grayscale")
                .then();

        response.statusCode(200);

        final byte[] convertedImage = response
                .contentType(ContentType.BINARY)
                .extract().asByteArray();
        final Image grayscaleImage = new Image(convertedImage);

        // Files.write(Path.of("/tmp/convertedImage.png"), convertedImage);

        // Ensure we still have the same image dimensions
        assertEquals(colorImage.getWidth(), grayscaleImage.getWidth());
        assertEquals(colorImage.getHeight(), grayscaleImage.getHeight());

        final int x = 0;
        final int y = 0;

        final Color colorPixel = colorImage.getPixel(x, y);

        final int gray = (colorPixel.getRed() + colorPixel.getGreen() + colorPixel.getBlue()) / 3;

        final Color grayscalePixel = grayscaleImage.getPixel(x, y);

        // Ensure the pixels were set to grayscale
        assertEquals(gray, grayscalePixel.getRed());
        assertEquals(gray, grayscalePixel.getGreen());
        assertEquals(gray, grayscalePixel.getBlue());
        assertEquals(gray * 3, grayscalePixel.getRed() + grayscalePixel.getGreen() + grayscalePixel.getBlue());

    }

    @Test
    public void color_red() throws IOException, URISyntaxException {
        final File file = getFile("image/image.png");
        final byte[] image = Files.readAllBytes(file.toPath());
        final Image originalImage = new Image(image);

        final ValidatableResponse response = given().contentType(ContentType.BINARY)
                .when()
                .body(image)
                .post("/color/1,0,0")
                .then();

        response.statusCode(200);

        final byte[] convertedImage = response
                .contentType(ContentType.BINARY)
                .extract().asByteArray();
        final Image redImage = new Image(convertedImage);

        // Files.write(Path.of("/tmp/convertedImage.png"), convertedImage);

        // Ensure we still have the same image dimensions
        assertEquals(originalImage.getWidth(), redImage.getWidth());
        assertEquals(originalImage.getHeight(), redImage.getHeight());

        final int x = 0;
        final int y = 0;

        final Color originalPixel = originalImage.getPixel(x, y);
        final Color redPixel = redImage.getPixel(x, y);


        // Ensure the pixels were set to red
        assertEquals(originalPixel.getRed(), redPixel.getRed());
        assertEquals(0, redPixel.getGreen());
        assertEquals(0, redPixel.getBlue());
    }

    @Test
    public void color_custom() throws IOException, URISyntaxException {
        final File file = getFile("image/image.png");
        final byte[] image = Files.readAllBytes(file.toPath());
        final Image originalImage = new Image(image);

        final ValidatableResponse response = given().contentType(ContentType.BINARY)
                .when()
                .body(image)
                .post("/color/0.5,0.7,1")
                .then();

        response.statusCode(200);

        final byte[] convertedImage = response
                .contentType(ContentType.BINARY)
                .extract().asByteArray();
        final Image redImage = new Image(convertedImage);

        // Files.write(Path.of("/tmp/convertedImage.png"), convertedImage);

        // Ensure we still have the same image dimensions
        assertEquals(originalImage.getWidth(), redImage.getWidth());
        assertEquals(originalImage.getHeight(), redImage.getHeight());

        final int x = 0;
        final int y = 0;

        final Color originalPixel = originalImage.getPixel(x, y);
        final Color colorPixel = redImage.getPixel(x, y);


        // Ensure the pixels were set to specified colors
        assertEquals(Math.floor(originalPixel.getRed() * 0.5), colorPixel.getRed());
        assertEquals(Math.floor(originalPixel.getGreen() * 0.7), colorPixel.getGreen());
        assertEquals(Math.floor(originalPixel.getBlue() * 1.0), colorPixel.getBlue());
    }

    @Test
    public void format_jpg() throws IOException, URISyntaxException {
        final File file = getFile("image/image.png");
        final byte[] image = Files.readAllBytes(file.toPath());

        final ValidatableResponse response = given().contentType(ContentType.BINARY)
                .when()
                .body(image)
                .post("/format/jpg")
                .then();

        response.statusCode(200);

        final byte[] convertedImage = response
                .contentType(ContentType.BINARY)
                .extract().asByteArray();

        final Path resultPath = Path.of("/tmp/convertedImage.jpg");
        Files.write(resultPath, convertedImage);
        final String resultMimeType = Files.probeContentType(resultPath);

        assertEquals("image/jpeg", resultMimeType);
    }
}
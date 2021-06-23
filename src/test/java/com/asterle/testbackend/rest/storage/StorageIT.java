package com.asterle.testbackend.rest.storage;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasItems;

import com.asterle.testbackend.rest.storage.model.File;
import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:applicationIT.properties")
public class StorageIT {

    @LocalServerPort
    private int port;

    @PostConstruct
    public void init() {
        // here we setup the default URL and API base path to use throughout the tests

        RestAssured.baseURI = "http://localhost:" + port;
        RestAssured.basePath = "/storage";
    }

    private final Gson gson = new Gson();

    private java.io.File getFile(final String fileName) throws URISyntaxException {
        final ClassLoader classLoader = getClass().getClassLoader();

        final URL resource = classLoader.getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("File not found: " + fileName);
        }

        return new java.io.File(resource.toURI());
    }

    @Test
    public void createFile_binary() throws IOException, URISyntaxException {
        final java.io.File file = getFile("storage/image.png");
        final byte[] fileContent = Files.readAllBytes(file.toPath());

        final File storageFile = new com.asterle.testbackend.rest.storage.model.File(
                "imageFile", fileContent);

        final String json = gson.toJson(storageFile);

        final ValidatableResponse response = given().contentType(ContentType.JSON)
                .when()
                .body(json)
                .post()
                .then();

        response.statusCode(200);
    }

    @Test
    public void createFile_binaries() throws IOException, URISyntaxException {
        final java.io.File file = getFile("storage/image.png");
        final java.io.File file2 = getFile("storage/image2.jpeg");
        final byte[] fileContent = Files.readAllBytes(file.toPath());
        final byte[] fileContent2 = Files.readAllBytes(file2.toPath());


        final List<File> files = new ArrayList<>();

        files.add(new File("imageFile", fileContent));
        files.add(new File("imageFile2", fileContent2));

        final String json = gson.toJson(files);

        final ValidatableResponse response = given().contentType(ContentType.JSON)
                .when()
                .body(json)
                .post("/many")
                .then();

        response.statusCode(200);
    }

    @Test
    public void readFiles_all() throws IOException, URISyntaxException {
        // Prepare a file to read
        final java.io.File file = getFile("storage/image.png");
        final java.io.File file2 = getFile("storage/image2.jpeg");
        final byte[] fileContent = Files.readAllBytes(file.toPath());
        final byte[] fileContent2 = Files.readAllBytes(file2.toPath());


        final List<File> files = new ArrayList<>();

        files.add(new File("imageFile", fileContent));
        files.add(new File("imageFile2", fileContent2));

        final String json = gson.toJson(files);

        final ValidatableResponse createResponse = given().contentType(ContentType.JSON)
                .when()
                .body(json)
                .post("/many")
                .then();

        createResponse.statusCode(200);

        // Start reading
        final ValidatableResponse readResponse = given().contentType(ContentType.JSON)
                .when()
                .get()
                .then()
                .body("fileName", hasItems("imageFile", "imageFile2"));

        readResponse.statusCode(200);
    }
}
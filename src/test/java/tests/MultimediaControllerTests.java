package tests;

import API.NovamanusAPI;
import Enums.Password;
import Enums.UserEmail;
import io.restassured.config.MultiPartConfig;
import io.restassured.http.ContentType;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.File;
import java.io.IOException;

import static io.restassured.RestAssured.config;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static io.restassured.module.jsv.JsonSchemaValidatorSettings.settings;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MultimediaControllerTests extends BaseTest {
    private static String token;
    private static String imageOriginalName;
    private static String videoOriginalName;
    private static String documentOriginalName;

    @BeforeClass
    public static void setUp() {
        setUpBase();
        token = NovamanusAPI.autchenticationShouldReturnToken(UserEmail.KEGUGUK, Password.DEFAULT_PASSWORD);
    }

    @Test
    public void test01_UploadImage() throws IOException {

        imageOriginalName = given().config(config().multiPartConfig(MultiPartConfig.multiPartConfig().defaultBoundary("abc"))).
                multiPart("files", new File("iphone6s_p5.JPG")).
                when().header("Authorization", "Bearer " + token).log().all().
                post("/api/multimedia/upload").
                then().
                assertThat().body(matchesJsonSchemaInClasspath("UploadResponseSchema.json").using(settings().with().checkedValidation(false))).
                contentType(ContentType.JSON).
                statusCode(200).
                log().all().
                extract().
                path("[0].originalName");
    }

    @Test
    public void test02_Rotate() {
        given().
                when().header("Authorization", "Bearer " + token).
                contentType(ContentType.JSON).
                log().all().
                body(quotes + imageOriginalName + quotes).
                post("/api/multimedia/rotate").
                then().
                statusCode(200).
                log().all();
    }


    @Test
    public void test04_UploadVideo() throws IOException {

        videoOriginalName = given().config(config().multiPartConfig(MultiPartConfig.multiPartConfig().defaultBoundary("abc"))).
                multiPart("files", new File("SampleVideo_1280x720_1mb.mp4")).
                when().header("Authorization", "Bearer " + token).
                post("/api/multimedia/upload/video").
                then().
                assertThat().body(matchesJsonSchemaInClasspath("UploadVideoResponseSchema.json").using(settings().with().checkedValidation(false))).
                contentType(ContentType.JSON).
                log().all().
                statusCode(200).extract().path("[0].originalName");
    }



    @Ignore
    @Test
    public void test06_UploadImageToKeyword() throws IOException {
        String filePath="C:\\Users\\penko.yordanov\\Desktop\\file upload\\";

        given().config(config().multiPartConfig(MultiPartConfig.multiPartConfig().defaultBoundary("abc"))).
                multiPart("files", new File(filePath+"takvindu.jpg")).
                when().header("Authorization", "Bearer " + token).
                post("/api/multimedia/upload/keyword/{keyword}", "s").
                then().
                contentType(ContentType.JSON).
                statusCode(200).
                log().all().
                body("name", containsString(".JPG")).
                body("url", containsString(".JPG")).
                body("type", equalTo(0)).
                extract().response();
    }

    @Test
    public void test07_UploadProfile() throws IOException {

                given().config(config().multiPartConfig(MultiPartConfig.multiPartConfig().defaultBoundary("abc"))).
                        multiPart("files", new File("profile.jpg")).
                        when().header("Authorization", "Bearer " + token).log().all().
                        post("/api/multimedia/upload/profile").
                        then().
                        contentType(ContentType.JSON).
                        statusCode(200).
                        log().all().
                        body("[0].name", containsString(".jpg")).
                        body("[0].url", containsString(".jpg")).
                        body("[0].type", equalTo(4));
    }


    @Test
    public void test10_UploadCoverPicture() throws IOException {

        given().config(config().multiPartConfig(MultiPartConfig.multiPartConfig().defaultBoundary("abc"))).
                multiPart("files", new File("profile_cover.jpg")).
                when().header("Authorization", "Bearer " + token).
                post("/api/multimedia/upload/cover").
                then().
                contentType(ContentType.JSON).
                statusCode(200).
                log().all().
                body("[0].name", containsString(".jpg")).
                body("[0].url", containsString(".jpg")).
                body("[0].type", equalTo(4)).
                extract().response();
    }


    @Test
    public void test11_UploadDocument() throws IOException {

        documentOriginalName = given().config(config().multiPartConfig(MultiPartConfig.multiPartConfig().defaultBoundary("abc"))).
                multiPart("files", new File("src/main/resources/GetCitiesByCountry.json")).
                when().header("Authorization", "Bearer " + token).
                post("/api/multimedia/upload/document").
                then().
                contentType(ContentType.JSON).
                statusCode(200).
                body("[0].name", containsString(".json")).
                body("[0].originalName", equalTo("GetCitiesByCountry.json")).
                body("[0].url", containsString(".json")).
                body("[0].type", equalTo(0)).
                body("[0].size", equalTo(0)).
                log().all().
                extract().path("[0].originalName");


    }

    @Ignore
    @Test
    public void test12_DeleteDocument() {

        given().
                when().
                header("Authorization", "Bearer " + token).
                contentType("application/json").
                header("Host", "novamanus-api-test.azurewebsites.net").
                log().all().
                request().body(quotes + documentOriginalName + quotes).
                post("/api/multimedia/delete/document").
                then().
                statusCode(200);
    }
}

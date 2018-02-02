package tests;

import API.NovamanusAPI;
import Dto.EditProfileDto;
import Dto.PrivacySettingsDto;
import Dto.SimpleAdDto;
import Enums.Password;
import Enums.UserEmail;
import Responses.UserAdsResponse;
import SQL.SQLService;
import io.restassured.config.MultiPartConfig;
import io.restassured.http.ContentType;
import org.junit.*;
import org.junit.runners.MethodSorters;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

import static io.restassured.RestAssured.config;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static io.restassured.module.jsv.JsonSchemaValidatorSettings.settings;
import static org.hamcrest.core.IsEqual.equalTo;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ProfileControllerTests extends BaseTest {
    private static String tokenLScott = "";
    private static String emailJDoe = "john.doe@nm.com";
    private static String emailLScott = "lscott1k@ftc.gov";
    private static Map<String, String> userDetailsJDoe = SQLService.getUserDetails(emailJDoe);
    private static Map<String, String> userDetailsLScott = SQLService.getUserDetails(emailLScott);
    private static String tokenJDoe;
    private static Long milis = System.currentTimeMillis() % 1000;
    private static String userNameLori = userDetailsLScott.get("userName");
    private String quotes = "\"";

    @BeforeClass
    public static void setUp() {
        setUpBase();
        tokenLScott = NovamanusAPI.autchenticationShouldReturnToken(UserEmail.LORISCOTT, Password.DEFAULT_PASSWORD);
        tokenJDoe = NovamanusAPI.autchenticationShouldReturnToken(UserEmail.JOHNDOE,Password.DEFAULT_PASSWORD);
    }

    @Test
    public void test01_VerifyUserShortDetailsTest() {

        given().
                contentType("application/json; charset=UTF-8").
                when().header("Authorization", "Bearer " + tokenLScott).
                get("/api/profile/short").
                then().statusCode(200).  // check that the status code is 200
                contentType(ContentType.JSON).  // check that the content type return from the API is JSON
                body("id", equalTo(9)).
                body("firstName", equalTo("Lori")).
                body("lastName", equalTo("Scott")).
                body("contactId", equalTo(16));
    }

    @Test
    public void test02_VerifyUserDetails() {
        given().
                contentType("application/json; charset=UTF-8").
                when().header("Authorization", "Bearer " + tokenLScott).
                get("/api/profile/byusername/{userName}", userDetailsLScott.get("userName")).
                then().statusCode(200).  // check that the status code is 200
                contentType(ContentType.JSON).  // check that the content type return from the API is JSON
                log().all().
                assertThat().body(matchesJsonSchemaInClasspath("ProfileResponseSchema.json").using(settings().with().checkedValidation(false))).
                body("userName", equalTo(userDetailsLScott.get("userName"))).
                body("email", equalTo(emailLScott)).
                body("firstName", equalTo(userDetailsLScott.get("firstName"))).
                body("lastName", equalTo(userDetailsLScott.get("lastName")));
//                body("location.location", equalTo(userDetailsLScott.get("location")));
    }

    @Test
    public void test03_SetPrivacySettingsForUser() {
        PrivacySettingsDto body = new PrivacySettingsDto(false, false, false);
        given().
                contentType("application/json").
                when().header("Authorization", "Bearer " + tokenLScott).
                body(body).
                post("/api/profile/settings/privacy").
                then().statusCode(200).  // check that the status code is 200
                log().all();
        given().
                contentType("application/json; charset=UTF-8").
                when().header("Authorization", "Bearer " + tokenLScott).
                get("/api/profile/settings").
                then().statusCode(200).  // check that the status code is 200
                contentType(ContentType.JSON).  // check that the content type return from the API is JSON
                log().all().
                body("privacy.showPhone", equalTo(false)).
                body("privacy.showEmail", equalTo(false)).
                body("privacy.showAddress", equalTo(false));
    }


    @Test
    public void test04_VerifyOtherUsersWouldNotSeeContactDetails() {
        //Set privacy setting to false for user Lori Scott
        PrivacySettingsDto body = new PrivacySettingsDto(false, false, false);
        given().
                contentType("application/json").
                when().header("Authorization", "Bearer " + tokenLScott).
                body(body).
                post("/api/profile/settings/privacy").
                then().statusCode(200).  // check that the status code is 200
                log().all();

        //Get user ads
        UserAdsResponse response =
                given().
                        contentType("application/json; charset=UTF-8").
                        when().header("Authorization", "Bearer " + tokenJDoe).
                        param("pageindex", 0).
                        param("pageSize", 8).
                        param("group", 0).
                        get("/api/nmadvertisements/author/{userName}", userNameLori).as(UserAdsResponse.class);

        //Iterate over user ads and verify that user address, phone and email are not in ad details
        for (SimpleAdDto ad : response.items) {
            given().
                    contentType("application/json").
                    log().all().
                    when().
                    header("Authorization", "Bearer " + tokenJDoe).
                    get("/api/nmadvertisements/{adId}", ad.id).
                    then().statusCode(200).  // check that the status code is 200
                    log().all().
                    body("publisher.email", equalTo(null)).
                    body("publisher.phone", equalTo(null)).
//                    body("address.address", equalTo("")).
        body("contact.phone", equalTo(null));
        }

    }

    @Test
    public void test05_GetUserFollowings() {
        ArrayList<Map<String, String>> followings = SQLService.getUserFollowings(1);
        response =
                given().
                        contentType("application/json; charset=UTF-8").
                        when().header("Authorization", "Bearer " + tokenJDoe).
                        param("pageIndex", 0).
                        param("pageSize", 8).
                        get("/api/follow/ivanivanov84/users").
                        then().statusCode(200).  // check that the status code is 200
                        contentType(ContentType.JSON).  // check that the content type return from the API is JSON
//                        assertThat().body(matchesJsonSchemaInClasspath("FollowingResponse.json").using(settings().with().checkedValidation(false))).
                        log().all().
                        extract().response();
        int responseSize = response.getBody().jsonPath().getList(".").size();


        for (int i = 0; i < responseSize; i++) {
            Assert.assertThat(response.getBody().jsonPath().getString("[" + i + "].firstName"), equalTo(followings.get(i).get("firstName")));
            Assert.assertThat(response.getBody().jsonPath().getString("[" + i + "].userName"), equalTo(followings.get(i).get("username")));
            Assert.assertThat(response.getBody().jsonPath().getString("[" + i + "].lastName"), equalTo(followings.get(i).get("lastName")));
        }
    }

    @Test
    public void test06_GetUserFollowers() {
        ArrayList<Map<String, String>> followers = SQLService.getUserFollowers(1);
        response =
                given().
                        contentType("application/json; charset=UTF-8").
                        when().header("Authorization", "Bearer " + tokenJDoe).
                        param("pageIndex", 0).
                        param("pageSize", 12).
                        get("/api/follow/ivanivanov84/followers").
                        then().statusCode(200).  // check that the status code is 200
                        contentType(ContentType.JSON).  // check that the content type return from the API is JSON
//                        assertThat().body(matchesJsonSchemaInClasspath("FollowersResponse.json.json").using(settings().with().checkedValidation(false))).
        log().all().
                        extract().response();

        for (int i = 0; i < followers.size(); i++) {
            Assert.assertThat(response.getBody().jsonPath().getString("[" + i + "].firstName"), equalTo(followers.get(i).get("firstName")));
            if (!response.getBody().jsonPath().getString("[" + i + "].firstName").equals("Lori")){
                Assert.assertThat(response.getBody().jsonPath().getString("[" + i + "].city"), equalTo(followers.get(i).get("city")));
            }
            Assert.assertThat(response.getBody().jsonPath().getString("[" + i + "].lastName"), equalTo(followers.get(i).get("lastName")));

        }
    }

    @Test
    public void test07_VerifyLoadedDetailsEditProfile() {
        given().
                contentType("application/json; charset=UTF-8").
                when().header("Authorization", "Bearer " + tokenJDoe).
                get("/api/profile/edit").
                then().statusCode(200).  // check that the status code is 200
                contentType(ContentType.JSON).  // check that the content type return from the API is JSON
                log().all().
                body("email", equalTo(emailJDoe)).
                body("username", equalTo(userDetailsJDoe.get("userName"))).
                body("contact.firstName", equalTo(userDetailsJDoe.get("firstName"))).
                body("contact.lastName", equalTo(userDetailsJDoe.get("lastName"))).
                body("location.location", equalTo(userDetailsJDoe.get("location"))).
                body("address.placeId", equalTo(userDetailsJDoe.get("placeId")));
    }

    @Test
    public void test08_UpdateProfileImage() {
        String profileImageOriginalName =
                given().config(config().multiPartConfig(MultiPartConfig.multiPartConfig().defaultBoundary("abc"))).
                        multiPart("files", new File("profile.jpg")).
                        when().header("Authorization", "Bearer " + tokenJDoe)
                        .log().all().
                        post("/api/multimedia/upload/profile").
                        then().
                        contentType(ContentType.JSON).
                        statusCode(200).
                        log().all().
                        extract().path("[0].name");

        given().
                contentType("application/json; charset=UTF-8").
                when().header("Authorization", "Bearer " + tokenJDoe).
                body(quotes + profileImageOriginalName + quotes).
                post("/api/profile/editImage").
                then().statusCode(200);  // check that the status code is 200

        given().
                contentType("application/json; charset=UTF-8").
                when().header("Authorization", "Bearer " + tokenJDoe).
                get("/api/profile/{0}", userDetailsJDoe.get("userName")).
                then().statusCode(200).  // check that the status code is 200
                contentType(ContentType.JSON).  // check that the content type return from the API is JSON
                log().all().
                body("image.name", equalTo(profileImageOriginalName));
    }

    @Test
    public void test09_UpdateProfileCover() {
        String profileCoverImageOriginalName =
                given().config(config().multiPartConfig(MultiPartConfig.multiPartConfig().defaultBoundary("abc"))).
                        multiPart("files", new File("profile_cover.jpg")).
                        when().header("Authorization", "Bearer " + tokenJDoe)
                        .log().all().
                        post("/api/multimedia/upload/cover").
                        then().
                        contentType(ContentType.JSON).
                        statusCode(200).
                        log().all().
                        extract().path("[0].name");

        given().
                contentType("application/json; charset=UTF-8").
                when().header("Authorization", "Bearer " + tokenJDoe).
                body(quotes + profileCoverImageOriginalName + quotes).
                post("/api/profile/editCover").
                then().statusCode(200);  // check that the status code is 200

        given().
                contentType("application/json; charset=UTF-8").
                when().header("Authorization", "Bearer " + tokenJDoe).
                get("/api/profile/{0}", userDetailsJDoe.get("userName")).
                then().statusCode(200).  // check that the status code is 200
                contentType(ContentType.JSON).  // check that the content type return from the API is JSON
                log().all().
                body("cover.name", equalTo(profileCoverImageOriginalName));
    }

    @Test
    public void test10_UpdateProfile() {


        EditProfileDto editProfileDto =
                given().
                        header("Authorization", "Bearer " + tokenJDoe).
                        get("/api/profile/edit").as(EditProfileDto.class);

        //Set new profile details
        editProfileDto.contact.firstName = userDetailsJDoe.get("firstName") + milis;
        editProfileDto.contact.lastName = userDetailsJDoe.get("lastName") + milis;
        editProfileDto.contact.phone = userDetailsJDoe.get("phone") + milis;
        editProfileDto.username = userDetailsJDoe.get("userName") + milis;

        given().
                contentType("application/json; charset=UTF-8").
                when().header("Authorization", "Bearer " + tokenJDoe).
                body(editProfileDto).
                post("/api/profile/edit").
                then().statusCode(200);  // check that the status code is 200

        given().
                contentType("application/json; charset=UTF-8").
                when().header("Authorization", "Bearer " + tokenJDoe).
                get("/api/profile/edit").
                then().statusCode(200).  // check that the status code is 200
                contentType(ContentType.JSON).  // check that the content type return from the API is JSON
                log().all().
                body("username", equalTo(editProfileDto.username)).
                body("contact.firstName", equalTo(editProfileDto.contact.firstName)).
                body("contact.lastName", equalTo(editProfileDto.contact.lastName)).
                body("contact.phone", equalTo(editProfileDto.contact.phone));

    }


    @AfterClass
    public static void tearDown() {
        // Restore privacy settings
        PrivacySettingsDto body = new PrivacySettingsDto(true, true, true);
        given().
                contentType("application/json").
                when().header("Authorization", "Bearer " + tokenLScott).
                body(body).
                post("/api/profile/settings/privacy").
                then().statusCode(200).  // check that the status code is 200
                log().all();

        //Restore profile details
        EditProfileDto editProfileDto =
                given().
                        header("Authorization", "Bearer " + tokenJDoe).
                        get("/api/profile/edit").as(EditProfileDto.class);

        editProfileDto.contact.setFirstName("John");
        editProfileDto.contact.setLastName("Doe");
        editProfileDto.contact.setPhone("+44895123456");
        editProfileDto.username = ("John1Doe1861");


        given().
                contentType("application/json; charset=UTF-8").
                when().header("Authorization", "Bearer " + tokenJDoe).
                body(editProfileDto).
                post("/api/profile/edit").
                then().statusCode(200);  // check that the status code is 200
    }

}

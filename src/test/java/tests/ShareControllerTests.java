package tests;


import API.NovamanusAPI;
import Dto.Receivers;
import Dto.ShareEmailParameters;
import Dto.ShareUserParameters;
import Enums.Password;
import Enums.UserEmail;
import com.github.javafaker.Faker;
import io.restassured.response.Response;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;
import java.util.HashMap;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ShareControllerTests extends BaseTest {

    private static String tokenArnoldo;
    private Faker faker = new Faker();
    private static String tokenLori;
    private static String shareMessage;
    private static final String EMAILLORI = "lscott1k@ftc.gov";
    private static final String USERNAME = "ivanivanov84";
    private static final String USERFULLNAME = "Ivan Ivanov";
    private static final String KEYWORD = "white";
    private static final String PLACEID = "ChIJsVUTOTeLpRIRILCBPm6UAAQ";
    private static final String SENDER = "Arnoldo Goyette";
    private static final String USERNAMELORI = "LoriScott861";
    private static final int USERID = 1;

    @BeforeClass
    public static void setUp() {
        setUpBase();
        tokenArnoldo = NovamanusAPI.autchenticationShouldReturnToken(UserEmail.ARNOLDOGOYETTE, Password.DEFAULT_PASSWORD);
        tokenLori = NovamanusAPI.autchenticationShouldReturnToken(UserEmail.LORISCOTT, Password.DEFAULT_PASSWORD);
    }

    @Test
    public void test01_ShareAdToUser() {
        //Get first ad from the Feed
        shareMessage = faker.lorem().sentence(10);

        int adId = given().
                contentType("application/json").
                log().all().
                when().
                header("Authorization", "Bearer " + tokenArnoldo).
                param("pageIndex", 0).
                param("pageSize", 12).
                param("countryID", "").
                param("countryName", "Norway").
                param("pricefrom", "").
                param("priceto", "").
                param("searchedUserId", "").
                param("areaId", "").
                param("adQuery", "").
                param("categories", 0, 1, 2, 3, 4, 6).
                param("order", 0).
                get("/api/feed/load").
                then().log().all().
                statusCode(200).  // check that the status code is 200
                extract().response().jsonPath().getInt("ads[0].id");

//Search for user
        Response response = given().
                contentType("application/json; charset=UTF-8").
                header("Authorization", "Bearer " + tokenArnoldo).
                param("q", "Lori%20Scott").
                param("country", "BG").
                urlEncodingEnabled(false).
                log().all().
                when().
                get("/api/user/find").
                then().statusCode(200).  // check that the status code is 200
                log().all().
                extract().response();
        ArrayList<HashMap<String, Object>> map = response.jsonPath().get();


        ShareUserParameters body = new ShareUserParameters();
        body.setMessage(shareMessage);
        body.setUsernames(USERNAMELORI);

        //Share ad to user
        given().
                contentType("application/json").
                log().all().
                when().
                header("Authorization", "Bearer " + tokenArnoldo).
                body(body).
                post("/api/share/Ad/{adID}/Chat", adId).
                then().statusCode(200).  // check that the status code is 200
                log().all();

        //Assert user received notification
        given().
                contentType("application/json").log().all().
                param("notificationFilter", 0).
                param("olderThan", "").
                param("takeCount", 5).
                when().
                header("Authorization", "Bearer " + tokenLori).
                get("/api/notification/get").
                then().
                statusCode(200).
                log().all().
                body("items[0].params.message", equalTo(shareMessage)).
                body("items[0].advertisementId", equalTo(adId)).
                body("items[0].params.sender", equalTo(SENDER));
    }


    @Test
    public void test02_ShareUserToAnotherUser() {
        shareMessage = faker.lorem().sentence(10);

        ShareUserParameters body = new ShareUserParameters();
        body.setMessage(shareMessage);
        body.setUsernames(USERNAMELORI);

        //Share user
        given().
                contentType("application/json").
                log().all().
                when().
                header("Authorization", "Bearer " + tokenArnoldo).
                body(body).
                post("/api/share/Profile/{user2Share}/Chat", USERNAME).
                then().statusCode(200).  // check that the status code is 200
                log().all();

        //Assert user received notification
        given().
                contentType("application/json").log().all().
                param("notificationFilter", 0).
                param("olderThan", "").
                param("takeCount", 5).
                when().
                header("Authorization", "Bearer " + tokenLori).
                get("/api/notification/get").
                then().
                statusCode(200).
                log().all().
                body("items[0].params.message", equalTo(shareMessage)).
                body("items[0].topicText", equalTo(USERFULLNAME)).
                body("items[0].userId", equalTo(USERID)).
                body("items[0].username", equalTo(USERNAME)).
                body("items[0].params.sender", equalTo(SENDER));
    }

    @Test
    public void test03_SharePlaceToAnotherUser() {
        shareMessage = faker.lorem().sentence(10);

        ShareUserParameters body = new ShareUserParameters();
        body.setMessage(shareMessage);
        body.setUsernames(USERNAMELORI);

        //Share user
        given().
                contentType("application/json").
                log().all().
                when().
                header("Authorization", "Bearer " + tokenArnoldo).
                body(body).
                post("/api/share/Place/{placeId}/Chat", PLACEID).
                then().statusCode(200).  // check that the status code is 200
                log().all();

    }

    @Test
    public void test04_ShareKeywordToAnotherUser() {
        shareMessage = faker.lorem().sentence(10);

        ShareUserParameters body = new ShareUserParameters();
        body.setMessage(shareMessage);
        body.setUsernames(USERNAMELORI);

        //Share user
        given().
                contentType("application/json").
                log().all().
                when().
                header("Authorization", "Bearer " + tokenArnoldo).
                body(body).
                post("/api/share/Keyword/{keyword}/Chat", KEYWORD).
                then().statusCode(200).  // check that the status code is 200
                log().all();
    }

    @Test
    public void test04_ShareAdToEmail() {
        //Get first ad from the Feed
        shareMessage = faker.lorem().sentence(10);

        int adId = given().
                contentType("application/json").
                log().all().
                when().
                header("Authorization", "Bearer " + tokenArnoldo).
                param("pageIndex", 0).
                param("pageSize", 12).
                param("countryID", "").
                param("countryName", "Norway").
                param("pricefrom", "").
                param("priceto", "").
                param("searchedUserId", "").
                param("areaId", "").
                param("adQuery", "").
                param("categories", 0, 1, 2, 3, 4, 6).
                param("order", 0).
                get("/api/feed/load").
                then().log().all().
                statusCode(200).  // check that the status code is 200
                extract().response().jsonPath().getInt("ads[0].id");

//Search for user
        Response response = given().
                contentType("application/json; charset=UTF-8").
                header("Authorization", "Bearer " + tokenArnoldo).
                param("q", EMAILLORI).
                param("country", "BG").
                urlEncodingEnabled(false).
                log().all().
                when().
                get("/api/user/findEmails").
                then().statusCode(200).  // check that the status code is 200
                log().all().
                extract().response();
        ArrayList<HashMap<String, Object>> map = response.jsonPath().get();
        Receivers receiver = new Receivers();
        receiver.setEmail(map.get(0).get("email"));
        receiver.setFullName(map.get(0).get("firstName") + " " + map.get(0).get("lastName"));
        receiver.setImage(map.get(0).get("image"));
        receiver.setId((map.get(0).get("id")));

        ShareEmailParameters body = new ShareEmailParameters();
        body.setEmailAddress(map.get(0).get("email").toString());
        body.setMessage(shareMessage);

        //Share ad to user
        given().
                contentType("application/json").
                log().all().
                when().
                header("Authorization", "Bearer " + tokenArnoldo).
                body(body).
                post("/api/share/Ad/{adID}/Email", adId).
                then().statusCode(200).  // check that the status code is 200
                log().all();

    }

    @Test
    public void test05_ShareUserToAnotherUserByEmail() {
        shareMessage = faker.lorem().sentence(10);

        Response response = given().
                contentType("application/json; charset=UTF-8").
                header("Authorization", "Bearer " + tokenArnoldo).
                param("q", EMAILLORI).
                param("country", "BG").
                urlEncodingEnabled(false).
                log().all().
                when().
                get("/api/user/findEmails").
                then().statusCode(200).  // check that the status code is 200
                log().all().
                extract().response();
        ArrayList<HashMap<String, Object>> map = response.jsonPath().get();
        Receivers receiver = new Receivers();
        receiver.setEmail(map.get(0).get("email"));
        receiver.setFullName(map.get(0).get("firstName") + " " + map.get(0).get("lastName"));
        receiver.setImage(map.get(0).get("image"));
        receiver.setId((map.get(0).get("id")));

        ShareEmailParameters body = new ShareEmailParameters();
        body.setMessage(shareMessage);
        body.setEmailAddress(map.get(0).get("email").toString());

        given().
                contentType("application/json").
                log().all().
                when().
                header("Authorization", "Bearer " + tokenArnoldo).
                body(body).
                post("/api/share/Profile/{username}/Email", USERNAME).
                then().statusCode(200).  // check that the status code is 200
                log().all();

    }

    @Test
    public void test06_SharePlaceToAnotherUserByEmail() {
        shareMessage = faker.lorem().sentence(10);

        ShareEmailParameters body = new ShareEmailParameters();
        body.setMessage(shareMessage);
        body.setEmailAddress(EMAILLORI);

        given().
                contentType("application/json").
                log().all().
                when().
                header("Authorization", "Bearer " + tokenArnoldo).
                body(body).
                post("/api/share/Place/{placeID}/Email", PLACEID).
                then().statusCode(200).  // check that the status code is 200
                log().all();

    }

    @Test
    public void test07_ShareKeywordToAnotherUserByEmail() {
        shareMessage = faker.lorem().sentence(10);

        Response response = given().
                contentType("application/json; charset=UTF-8").
                header("Authorization", "Bearer " + tokenArnoldo).
                param("q", EMAILLORI).
                param("country", "BG").
                urlEncodingEnabled(false).
                log().all().
                when().
                get("/api/user/findEmails").
                then().statusCode(200).  // check that the status code is 200
                log().all().
                extract().response();
        ArrayList<HashMap<String, Object>> map = response.jsonPath().get();
        Receivers receiver = new Receivers();
        receiver.setEmail(map.get(0).get("email"));
        receiver.setFullName(map.get(0).get("firstName") + " " + map.get(0).get("lastName"));
        receiver.setImage(map.get(0).get("image"));
        receiver.setId((map.get(0).get("id")));

        ShareEmailParameters body = new ShareEmailParameters();
        body.setMessage(shareMessage);
        body.setEmailAddress(map.get(0).get("email").toString());

        given().
                contentType("application/json").
                log().all().
                when().
                header("Authorization", "Bearer " + tokenArnoldo).
                body(body).
                post("/api/share/Keyword/{keyword}/Email", KEYWORD).
                then().statusCode(200).  // check that the status code is 200
                log().all();

    }


}

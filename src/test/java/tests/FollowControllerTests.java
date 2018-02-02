package tests;


import API.NovamanusAPI;
import Enums.Password;
import Enums.UserEmail;
import io.restassured.response.Response;
import org.junit.*;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;
import java.util.HashMap;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FollowControllerTests extends BaseTest {
    private static String tokenHaralampi = "";
    private static final String UserFirstName = "Ivan";
    private static final String UserLastName = "Ivanov";
    private static final String FollowUserFN = "Haralampi";
    private static final String FollowUserLN = "Popov";
    private static final String FollowUserName = "HaralampiPopov902";
    private static final String placeIdVarna = "ChIJodfzqotTpEARfIulcRyUJ1c";
    private static final String KeywordToFollow = "shoes";
    private static final int followID = 0;
    private final String quotes = "\"";


    @BeforeClass
    public static void setUp() {
        setUpBase();
        tokenHaralampi = NovamanusAPI.autchenticationShouldReturnToken(UserEmail.HARALAMPI, Password.DEFAULT_PASSWORD);
    }

    @Test
    public void test01_getFollowedItems() {
        given().
                contentType("application/json; charset=UTF-8").
                header("Authorization", "Bearer " + tokenHaralampi).
                log().all().
                when().
                get("/api/follow/getfolloweditems").
                then().statusCode(200).  // check that the status code is 200
                log().all().
                body(containsString("Horten | Norway")).log().all();
    }

    @Test
    public void test02_GetSuggestions() {
        given().
                contentType("application/json; charset=UTF-8").
                header("Authorization", "Bearer " + tokenHaralampi).
                param("pageSize", 10).
                param("term", UserFirstName + " " + UserLastName).
                log().all().
                when().
                get("/api/follow/suggest").
                then().statusCode(200).  // check that the status code is 200
                log().all().
                body("[0].name", equalTo(UserFirstName + " " + UserLastName)).
                log().all();
    }

    @Test
    public void test03_SearchForUniversityShouldReturnSuggestions() {
        given().
                contentType("application/json; charset=UTF-8").
                header("Authorization", "Bearer " + tokenHaralampi).
                param("pageSize", 10).
                param("term", "Technical University of Denmark").
                log().all().
                when().
                get("/api/follow/suggest").
                then().statusCode(200).  // check that the status code is 200
                log().all().
                body(containsString("Technical University of Denmark, Lyngby, Denmark")).
                log().all();
    }

    @Test
    public void test04_SearchForCollegeShouldReturnSuggestions() {
        given().
                contentType("application/json; charset=UTF-8").
                header("Authorization", "Bearer " + tokenHaralampi).
                param("pageSize", 10).
                param("term", "Imperial college").
                log().all().
                when().
                get("/api/follow/suggest").
                then().statusCode(200).  // check that the status code is 200
                log().all().
                body(containsString("Imperial College London")).
                log().all();
    }

    @Test
    public void test05_SearchForHighSchoolShouldNotReturnSuggestions() {
        Response response = given().
                contentType("application/json; charset=UTF-8").
                header("Authorization", "Bearer " + tokenHaralampi).
                param("pageSize", 10).
                param("term", "regent high school").
                log().all().
                when().
                get("/api/follow/suggest").
                then().statusCode(200).  // check that the status code is 200
                log().all().
                body(not(containsString("regent high school"))).
                log().all().extract().response();
        Assert.assertThat(response.body().asString(), equalTo("[]"));
    }

    @Test
    public void test06_SearchForPrimarySchoolShouldNotReturnSuggestions() {
        Response response = given().
                contentType("application/json; charset=UTF-8").
                header("Authorization", "Bearer " + tokenHaralampi).
                param("pageSize", 10).
                param("term", "Rosendale Primary School").
                log().all().
                when().
                get("/api/follow/suggest").
                then().statusCode(200).  // check that the status code is 200
                log().all().
                body(not(containsString("Rosendale Primary School"))).
                log().all().extract().response();
        Assert.assertThat(response.body().asString(), equalTo("[]"));
    }

    @Test
    public void test07_SearchMunicipalityShouldNOTReturnSuggestions() {
        Response response = given().
                contentType("application/json; charset=UTF-8").
                header("Authorization", "Bearer " + tokenHaralampi).
                param("pageSize", 10).
                param("term", "Hvaler").
                log().all().
                when().
                get("/api/follow/suggest").
                then().statusCode(200).  // check that the status code is 200
                log().all().
                extract().response();

        Assert.assertThat(response.body().asString(), equalTo("[]"));
    }
    @Test
    public void test08_SearchForCityShouldReturnSuggestions() {
        Response response =
                given().
                contentType("application/json; charset=UTF-8").
                header("Authorization", "Bearer " + tokenHaralampi).
                param("pageSize", 10).
                param("term", "Tønsberg").
                log().all().
                when().
                get("/api/follow/suggest").
                then().statusCode(200).  // check that the status code is 200
                log().all().
                extract().response();


        ArrayList<HashMap<String, Object>> map = response.jsonPath().get();
        for (HashMap<String, Object> aMap : map) {
            if (aMap.get("name").equals("Tønsberg|Norway")) {
                Assert.assertThat(aMap.get("objectData"), equalTo("ChIJHekCWy63RkYRnfbV6wUsfA8"));
            }

        }
    }

    @Ignore
    @Test
    public void test09_SearchForVillageShouldNOTReturnSuggestions() {
        Response response = given().
                contentType("application/json; charset=UTF-8").
                header("Authorization", "Bearer " + tokenHaralampi).
                param("pageSize", 10).
                param("term", "Skytta").
                log().all().
                when().
                get("/api/follow/suggest").
                then().statusCode(200).  // check that the status code is 200
                log().all().
                body(not(containsString("Skytta"))).
                log().all().extract().response();
        Assert.assertThat(response.body().asString(), equalTo("[]"));
    }

    @Test
    public void test10_SearchForFylkeShouldNOTReturnSuggestions() {
        Response response = given().
                contentType("application/json; charset=UTF-8").
                header("Authorization", "Bearer " + tokenHaralampi).
                param("pageSize", 10).
                param("term", "Vestfold").
                log().all().
                when().
                get("/api/follow/suggest").
                then().statusCode(200).  // check that the status code is 200
                log().all().
                body(not(containsString("Vestfold"))).
                body(not(containsString("ChIJq311jTi3RkYREHPaqYwyXss"))).
                log().all().extract().response();
        Assert.assertThat(response.body().asString(), equalTo("[]"));
    }

    @Test
    public void test11_FollowUser() {
        response = given().
                contentType("application/json; charset=UTF-8").
                header("Authorization", "Bearer " + tokenHaralampi).
                log().all().
                when().
                get("/api/follow/getfolloweditems").
                then().statusCode(200).  // check that the status code is 200
                log().all().extract().response();

        ArrayList<HashMap<String, Object>> map = response.jsonPath().get();
        for (HashMap<String, Object> aMap : map) {
            if (aMap.get("name").equals(FollowUserFN + " " + FollowUserLN)) {
                given().
                        contentType("application/json; charset=UTF-8").
                        header("Authorization", "Bearer " + tokenHaralampi).
                        log().all().
                        when().
//                        body(body).
                        get("/api/profile/unfollow/{0}",FollowUserName).
                        then().statusCode(200).  // check that the status code is 200
                        log().all();
            }
        }

        given().
                contentType("application/json; charset=UTF-8").
                header("Authorization", "Bearer " + tokenHaralampi).
                log().all().
                when().
                get("/api/profile/follow/{0}",FollowUserName).
                then().statusCode(200).  // check that the status code is 200
                log().all().
                extract().body().asString();

        //Get followed items should return followed user
        given().
                contentType("application/json; charset=UTF-8").
                header("Authorization", "Bearer " + tokenHaralampi).
                log().all().
                when().
                get("/api/follow/getfolloweditems").
                then().statusCode(200).  // check that the status code is 200
                log().all().
                body(containsString(FollowUserName)).
                log().all();
    }

    @Ignore
    @Test
    public void test12_FollowUserWithSpecialCharacters() {
        //Get all followed items
        String userName="PenkøDimitrow582";
        response = given().
                contentType("application/json; charset=UTF-8").
                header("Authorization", "Bearer " + tokenHaralampi).
                log().all().
                when().
                get("/api/profile/{userName}",userName).
                then().statusCode(200).  // check that the status code is 200
                log().all().extract().response();

        if(response.jsonPath().getBoolean("isFollowed")){
            given().
                    contentType("application/json; charset=UTF-8").
                    header("Authorization", "Bearer " + tokenHaralampi).
                    log().all().
                    when().
                    get("/api/profile/unfollow/{userName}",FollowUserName).
                    then().statusCode(200).  // check that the status code is 200
                    log().all();

            given().
                    contentType("application/json; charset=UTF-8").
                    header("Authorization", "Bearer " + tokenHaralampi).
                    log().all().
                    when().
                    get("/api/profile/{userName}",userName).
                    then().statusCode(200).  // check that the status code is 200
                    log().all().body("isFollowed",equalTo(false));
        }else{
            given().
                    contentType("application/json; charset=UTF-8").
                    header("Authorization", "Bearer " + tokenHaralampi).
                    log().all().
                    when().
//                    body(body).
                    get("/api/profile/follow/{userName}",userName).
                    then().statusCode(200).  // check that the status code is 200
                    log().all();
            given().
                    contentType("application/json; charset=UTF-8").
                    header("Authorization", "Bearer " + tokenHaralampi).
                    log().all().
                    when().
                    get("/api/profile/{userName}",userName).
                    then().statusCode(200).  // check that the status code is 200
                    log().all().body("isFollowed",equalTo(true));

        }

    }

    @Test
    public void test12_UnfollowUser() {
        given().
                contentType("application/json; charset=UTF-8").
                header("Authorization", "Bearer " + tokenHaralampi).
                log().all().
                when().
                get("/api/profile/unfollow/{userName}",FollowUserName).
                then().statusCode(200).  // check that the status code is 200
                log().all();

        //Get followed items should not return followed user
        given().
                contentType("application/json; charset=UTF-8").
                header("Authorization", "Bearer " + tokenHaralampi).
                log().all().
                when().
                get("/api/follow/getfolloweditems").
                then().statusCode(200).  // check that the status code is 200
                log().all().
                body(not(containsString(UserFirstName + " " + UserLastName))).
                log().all();
    }

    @Test
    public void test13_FollowArea() {

        //Check if area is already followed. If it is already followed, unfollow it.

        response = given().
                contentType("application/json; charset=UTF-8").
                header("Authorization", "Bearer " + tokenHaralampi).
                log().all().
                when().
                get("/api/follow/getfolloweditems").
                then().statusCode(200).  // check that the status code is 200
                log().all().extract().response();

        ArrayList<HashMap<String, Object>> map = response.jsonPath().get();
        for (HashMap<String, Object> aMap : map) {
            if (aMap.get("name").equals("Varna|Bulgaria")) {
                given().
                        contentType("application/json; charset=UTF-8").
                        header("Authorization", "Bearer " + tokenHaralampi).
                        log().all().
                        when().
                        delete("/api/follow/area/{placeID}",placeIdVarna).
                        then().statusCode(200).  // check that the status code is 200
                        log().all();
            }
        }


            given().
                    contentType("application/json; charset=UTF-8").
                    header("Authorization", "Bearer " + tokenHaralampi).
                    log().all().
                    when().
                    body(quotes+placeIdVarna+quotes).
                    post("/api/follow/area").
                    then().statusCode(200).  // check that the status code is 200
                    log().all();

            //Get followed items should return followed user
            given().
                    contentType("application/json; charset=UTF-8").
                    header("Authorization", "Bearer " + tokenHaralampi).
                    log().all().
                    when().
                    get("/api/follow/getfolloweditems").
                    then().statusCode(200).  // check that the status code is 200
                    log().all().
                    body(containsString(placeIdVarna)).
                    body(containsString("Varna | Bulgaria")).
                    log().all();

    }

    @Test
    public void test14_UnfollowArea() {
        given().
                contentType("application/json; charset=UTF-8").
                header("Authorization", "Bearer " + tokenHaralampi).
                log().all().
                when().
                delete("/api/follow/area/{placeId}",placeIdVarna).
                then().statusCode(200).  // check that the status code is 200
                log().all();

        //Get followed items should not return followed user
        given().
                contentType("application/json; charset=UTF-8").
                header("Authorization", "Bearer " + tokenHaralampi).
                log().all().
                when().
                get("/api/follow/getfolloweditems").
                then().statusCode(200).  // check that the status code is 200
                log().all().
                body(not(containsString(placeIdVarna))).
                log().all();
    }

    @Test
    public void test15_FollowKeyword() {
        given().
                contentType("application/json; charset=UTF-8").
                header("Authorization", "Bearer " + tokenHaralampi).
                log().all().
                when().
                body(quotes+KeywordToFollow+quotes).
                post("/api/follow/keyword").
                then().statusCode(200).  // check that the status code is 200
                log().all();

        //Get followed items should return followed user
        given().
                contentType("application/json; charset=UTF-8").
                header("Authorization", "Bearer " + tokenHaralampi).
                log().all().
                when().
                get("/api/follow/getfolloweditems").
                then().statusCode(200).  // check that the status code is 200
                log().all().
                body(containsString(KeywordToFollow)).
                log().all();
    }

    @Test
    public void test16_UnfollowKeyword() {
        given().
                contentType("application/json; charset=UTF-8").
                header("Authorization", "Bearer " + tokenHaralampi).
                log().all().
                when().
                delete("/api/follow/keyword/{keyword}",KeywordToFollow).
                then().statusCode(200).  // check that the status code is 200
                log().all();

        //Get followed items should not return followed user
        given().
                contentType("application/json; charset=UTF-8").
                header("Authorization", "Bearer " + tokenHaralampi).
                log().all().
                when().
                get("/api/follow/getfolloweditems").
                then().statusCode(200).  // check that the status code is 200
                log().all().
                body(not(containsString(KeywordToFollow))).
                log().all();
    }

    @Test
    public void test17_FollowSchool() {
        given().
                contentType("application/json; charset=UTF-8").
                header("Authorization", "Bearer " + tokenHaralampi).
                log().all().
                when().
                body(quotes+"ChIJEYxtM2FynkcRnfFb3ERNoHk"+quotes).
                post("/api/follow/area").
                then().statusCode(200).  // check that the status code is 200
                log().all();
        //Get followed items should return followed school
        given().
                contentType("application/json; charset=UTF-8").
                header("Authorization", "Bearer " + tokenHaralampi).
                log().all().
                when().
                get("/api/follow/getfolloweditems").
                then().statusCode(200).  // check that the status code is 200
                log().all().
                body(containsString("Technische Universität München")).
                log().all();
    }

    @Test
    public void test18_UnfollowSchool() {
        given().
                contentType("application/json; charset=UTF-8").
                header("Authorization", "Bearer " + tokenHaralampi).
                log().all().
                when().
                delete("/api/follow/area/{placeId}","ChIJEYxtM2FynkcRnfFb3ERNoHk").
                then().statusCode(200).  // check that the status code is 200
                log().all();

        //Get followed items should not return followed user
        given().
                contentType("application/json; charset=UTF-8").
                header("Authorization", "Bearer " + tokenHaralampi).
                log().all().
                when().
                get("/api/follow/getfolloweditems").
                then().statusCode(200).  // check that the status code is 200
                log().all().
                body(not(containsString("Technical University of Munich"))).
                log().all();
    }


    @Ignore("Need to be refactored")
    @Test
    public void test19_GetReccomendedFollowsShouldReturnNearbyCities() {
//        String tokenHaralampi = NovamanusAPI.autchenticationShouldReturnToken("hrers3434@mailinator.com", "123456234");
        Response response =
                given().
                        contentType("application/json; charset=UTF-8").
                        header("Authorization", "Bearer " + tokenHaralampi).
                        param("placeId", "ChIJ9S1u30uFqkARv-ilupx_T8E").
                        log().all().
                        when().
                        get("/api/follow/recommend").
                        then().statusCode(200).  // check that the status code is 200
                        log().all().
//                        body(containsString("Tønsberg, Norway")).
        extract().response();
        ArrayList<HashMap<String, Object>> map = response.jsonPath().get();
        int match = 0;
        for (HashMap<String, Object> aMap : map) {
            if (aMap.get("name").toString().equals("Tønsberg, Norway")) {
                match++;
            }
        }
        Assert.assertThat(match, equalTo(1));

    }



}

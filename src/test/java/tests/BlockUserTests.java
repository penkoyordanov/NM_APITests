package tests;

import API.NovamanusAPI;
import Enums.Password;
import Enums.UserEmail;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.core.IsEqual.equalTo;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BlockUserTests extends BaseTest {
    private static String tokenLori;
    private static String tokenPenko;
    private static String blockedUsername="PenkoYordanov740";
    private static final int BLOCKEDUSERID =30;
    private static final int IDLORI =9;

    @BeforeClass
    public static void setUp() {
        setUpBase();
        tokenLori = NovamanusAPI.autchenticationShouldReturnToken(UserEmail.LORISCOTT, Password.DEFAULT_PASSWORD);
        tokenPenko = NovamanusAPI.autchenticationShouldReturnToken(UserEmail.PENKO,Password.DEFAULT_PASSWORD);
    }

    private Boolean isBloked() {
        response = given().
                contentType("application/json").log().all().
                when().
                header("Authorization", "Bearer " + tokenLori).
                param("userID", BLOCKEDUSERID).
                get("/api/block/HaveYouBlockedThatUser").
                then().statusCode(200).  // check that the status code is 200
                log().all().extract().response();
        return Boolean.valueOf(response.asString());
    }

    private void unblock(int userID) {
        given().
                contentType("application/json").log().all().
                body(userID).
                when().
                header("Authorization", "Bearer " + tokenLori).
                post("/api/block/unblockuser").
                then().statusCode(200).  // check that the status code is 200
                log().all();
    }


    @Ignore
    @Test
    public void test01_BlockUser() {

        if (isBloked()) {
            unblock(BLOCKEDUSERID);
        } else {
            given().
                    contentType("application/json").log().all().
                    body(quotes+blockedUsername+quotes).
                    when().
                    header("Authorization", "Bearer " + tokenLori).
                    post("/api/block/blockuser").
                    then().statusCode(200).  // check that the status code is 200
                    log().all();
        }

    }
    @Ignore
    @Test
    public void test02_TryToBlockSameUserShouldReturnAlert() {
        given().
                contentType("application/json").log().all().
                body(quotes+blockedUsername+quotes).
                when().
                header("Authorization", "Bearer " + tokenLori).
                post("/api/block/blockuser").
                then().statusCode(400).  // check that the status code is 200
                log().all().
                body("message", equalTo("User "+blockedUsername+" is already blocked by user with ID "+ IDLORI +"!"));
    }
    @Ignore
    @Test
    public void test03_HaveYOuBlockedUserShouldReturnTrue() {
        given().
                contentType("application/json").log().all().
                when().
                header("Authorization", "Bearer " + tokenLori).
                param("userID", BLOCKEDUSERID).
                get("/api/block/HaveYouBlockedThatUser").
                then().statusCode(200).  // check that the status code is 200
                log().all().
                body(containsString("true"));
    }
    @Ignore
    @Test
    public void test04_HasThatUserBlockedYouTrue() {
        given().
                contentType("application/json").log().all().
                when().
                header("Authorization", "Bearer " + tokenPenko).
                param("userID", IDLORI).
                get("/api/block/HasThatUserBlockedYou").
                then().statusCode(200).  // check that the status code is 200
                log().all().
                body(containsString("true"));
    }
    @Ignore
    @Test
    public void test05_UnblockUser() {
        given().
                contentType("application/json").log().all().
                body(30).
                when().
                header("Authorization", "Bearer " + tokenLori).
                post("/api/block/unblockuser").
                then().statusCode(200).  // check that the status code is 200
                log().all();
    }
    @Ignore
    @Test
    public void test06_HaveYOuBlockedUserShouldReturnFalse() {
        given().
                contentType("application/json").log().all().
                when().
                header("Authorization", "Bearer " + tokenLori).
                param("userID", 30).
                get("/api/block/HaveYouBlockedThatUser").
                then().statusCode(200).  // check that the status code is 200
                log().all().
                body(containsString("false"));
    }
    @Ignore
    @Test
    public void test07_HasThatUserBlockedYouFalse() {
        given().
                contentType("application/json").log().all().
                when().
                header("Authorization", "Bearer " + tokenPenko).
                param("userID", IDLORI).
                get("/api/block/HasThatUserBlockedYou").
                then().statusCode(200).  // check that the status code is 200
                log().all().
                body(containsString("false"));
    }
}

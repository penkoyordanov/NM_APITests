package tests;


import API.NovamanusAPI;
import Enums.Password;
import Enums.UserEmail;
import Enums.Usernames;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SettingsControllerTests extends BaseTest {

    private static final String LORI_SCOTT_USERNAME = Usernames.USERNAME_LORI.getUsername();
    private static final String LORI_SCOTT_ADDRESS = "Camí Pui d'Olivesa, AD600 Sant Julià de Lòria, Andorra";
    private static final String LORI_FNAME = "Lori";
    private static final String LORI_LNAME = "Scott";
    private static final String LORI_PHONE = "+866694088828";
    private static final int LORI_USERID = 9;
    private static String tokenLori = "";
    @BeforeClass
    public static void setUp() {
        setUpBase();
        tokenLori = NovamanusAPI.autchenticationShouldReturnToken(UserEmail.LORISCOTT, Password.DEFAULT_PASSWORD);
    }

    @Test
    public void test01_getProfileSettings(){
        given().
                contentType("application/json; charset=UTF-8").
                header("Authorization", "Bearer " + tokenLori).
                log().all().
                when().
                get("/api/profile/settings").
                then().statusCode(200).  // check that the status code is 200
                log().all().
                body("locationDto.locationDto",equalTo(LORI_SCOTT_ADDRESS)).
                body("privacy.showPhone",equalTo(true)).
                body("privacy.showEmail",equalTo(true)).
                body("privacy.showAddress",equalTo(true)).
                body("username",equalTo(LORI_SCOTT_USERNAME)).
                body("email",equalTo(UserEmail.LORISCOTT.getUserEmail())).
                body("contact.firstName",equalTo(LORI_FNAME)).
                body("contact.lastName",equalTo(LORI_LNAME)).
                body("contact.phone",equalTo(LORI_PHONE)).
                log().all();

    }

    @Test
    public void test02_getNotificationSettings(){
        given().
                contentType("application/json; charset=UTF-8").
                header("Authorization", "Bearer " + tokenLori).
                log().all().
                when().
                get("/api/settings/NotificationSettings").
                then().statusCode(200).  // check that the status code is 200
                log().all().
                body("userId",equalTo(LORI_USERID)).
                body("general_NewFollower",equalTo(true)).
                log().all();

    }

    @Test
    public void test03_setGetNotificationFollowKeywordFalse(){
        given().
                contentType("application/json; charset=UTF-8").
                header("Authorization", "Bearer " + tokenLori).
                log().all().
                when().
                body(11).
                post("/api/settings/NotificationSettings").
                then().statusCode(200).  // check that the status code is 200
                log().all();

        given().
                contentType("application/json; charset=UTF-8").
                header("Authorization", "Bearer " + tokenLori).
                log().all().
                when().
                get("/api/settings/NotificationSettings").
                then().statusCode(200).  // check that the status code is 200
                log().all().
                body("userId",equalTo(LORI_USERID)).
                body("general_NewMessage",equalTo(false)).
                log().all();

    }

    @AfterClass
    public static void tearDown() {
        given().
                contentType("application/json; charset=UTF-8").
                header("Authorization", "Bearer " + tokenLori).
                when().
                body(11).
                post("/api/settings/NotificationSettings");

    }

}

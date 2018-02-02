package tests;

import API.NovamanusAPI;
import Dto.SignInDto;
import Enums.Password;
import Enums.UserEmail;
import SQL.SQLService;
import io.restassured.response.Response;
import org.junit.*;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;
import java.util.HashMap;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.startsWith;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserControllerTests extends BaseTest {
    private static String initialPassword = Password.DEFAULT_PASSWORD.getPassword();
    private static String email = UserEmail.HARIBARI.getUserEmail();
    private static String newPassword = "123456";
    static String token ="";

    @BeforeClass
    public static void setUp() {
        setUpBase();
        token = NovamanusAPI.autchenticationShouldReturnToken(UserEmail.HARIBARI, Password.DEFAULT_PASSWORD);
    }

    @Ignore
    @Test
    public void test01_IsEmailUsedShouldReturnFalseIfEmailAlreadyHaveAccessCode() {
        given().
                contentType("application/json; charset=UTF-8").
                header("Authorization", "Bearer " + token).
                param("email", UserEmail.TESTUSR.getUserEmail()).
                urlEncodingEnabled(false).
                log().all().
                when().
                get("/api/user/isEmailUsedAndUserHasAccess").
                then().statusCode(200).  // check that the status code is 200
                body(containsString("true")).log().all();

    }

    @Test
    public void test02_IsEmailUsedShouldReturnFalseIfEmailIsNotRegistered() {
        long mils = System.currentTimeMillis() % 1000;
        email = "fakeUser" + mils + "@gmail.com";
        given().
                contentType("application/json; charset=UTF-8").
                header("Authorization", "Bearer " + token).
                param("email", email).
                urlEncodingEnabled(false).
                log().all().
                when().
                get("/api/user/isEmailUsedAndUserHasAccess").
                then().statusCode(200).  // check that the status code is 200
                body(containsString("false")).
                log().all();

    }

    @Test
    public void test03_IsEmailUsedShouldReturnFalseIfEmailIsUsed() {
        long mils = System.currentTimeMillis() % 1000;
        email = "fakeUser" + mils + "@gmail.com";
        given().
                contentType("application/json; charset=UTF-8").
                param("email", email).
                urlEncodingEnabled(false).
                log().all().
                when().
                get("/api/user/isEmailUsed").
                then().statusCode(200).  // check that the status code is 200
                body(containsString("false")).
                log().all();

    }

    @Test
    public void test04_IsEmailUsedShouldReturnTrueIfEmailIsUsed() {
        given().
                contentType("application/json; charset=UTF-8").
                param("email", UserEmail.TESTUSR.getUserEmail()).
                urlEncodingEnabled(false).
                log().all().
                when().
                get("/api/user/isEmailUsed").
                then().statusCode(200).  // check that the status code is 200
                body(containsString("true")).
                log().all();

    }

    @Ignore ("Always returns true")
    @Test
    public void test05_IsPhoneUsedShouldReturnTrueIfPhoneIsUsed() {
        given().
                contentType("application/json; charset=UTF-8").
                param("phone", "+4797675801").
                urlEncodingEnabled(false).
                log().all().
                when().
                get("/api/user/isEmailUsed").
                then().statusCode(200).  // check that the status code is 200
                body(containsString("true")).
                log().all();

    }

    @Test
    public void test06_IsPhoneUsedShouldReturnFalseIfPhoneIsNotUsed() {
        given().
                contentType("application/json; charset=UTF-8").
                param("phone", "+4797675801567878").
                urlEncodingEnabled(false).
                log().all().
                when().
                get("/api/user/isEmailUsed").
                then().statusCode(200).  // check that the status code is 200
                body(containsString("false")).
                log().all();

    }

    @Test
    public void test07_IsEmailUsedShouldReturnFalseIfEmailIsUsedByAnother() {
        long mils = System.currentTimeMillis() % 1000;
        email = "fakeUser" + mils + "@gmail.com";
        given().
                contentType("application/json; charset=UTF-8").
                header("Authorization", "Bearer " + token).
                param("email", email).
                urlEncodingEnabled(false).
                log().all().
                when().
                get("/api/user/IsEmailUsedByOther").
                then().statusCode(200).  // check that the status code is 200
                body(containsString("false")).
                log().all();

    }


    @Test
    public void test08_IsEmailUsedShouldReturnTrueIfEmailIsUsedByAnother() {
        given().
                contentType("application/json; charset=UTF-8").
                header("Authorization", "Bearer " + token).
                param("email", UserEmail.LORISCOTT.getUserEmail()).
                urlEncodingEnabled(false).
                log().all().
                when().
                get("/api/user/IsEmailUsedByOther").
                then().statusCode(200).  // check that the status code is 200
                body(containsString("true")).
                log().all();

    }

    @Test
    public void test09_IsUserNameUsedByOtherReturnsTrue() {
        given().
                contentType("application/json; charset=UTF-8").
                header("Authorization", "Bearer " + token).
                param("username", "BarumBarumF493").
                urlEncodingEnabled(false).
                log().all().
                when().
                get("/api/user/IsUserNameUsedByOther").
                then().statusCode(200).  // check that the status code is 200
                body(containsString("true")).
                log().all();

    }

    @Test
    public void test10_IsUserNameUsedByOtherReturnsFalseForUniquUserName() {
        given().
                contentType("application/json; charset=UTF-8").
                header("Authorization", "Bearer " + token).
                param("username", "BarumBarumF493"+"123456").
                urlEncodingEnabled(false).
                log().all().
                when().
                get("/api/user/IsUserNameUsedByOther").
                then().statusCode(200).  // check that the status code is 200
                body(containsString("false")).
                log().all();

    }

    @Test
    public void test11_IsUserNameUsedByOtherReturnsFalseForOwnUserName() {
        given().
                contentType("application/json; charset=UTF-8").
                header("Authorization", "Bearer " + token).
                param("username", "rtrttrtr857").
                urlEncodingEnabled(false).
                log().all().
                when().
                get("/api/user/IsUserNameUsedByOther").
                then().statusCode(200).  // check that the status code is 200
                body(containsString("false")).
                log().all();

    }

    @Test
    public void test12_CheckPasswordShouldReturnTrueIfEnteredPasswordIsForTheSameUser() {
        given().
                contentType("application/json; charset=UTF-8").
                header("Authorization", "Bearer " + token).
                param("password", initialPassword).
                urlEncodingEnabled(false).
                log().all().
                when().
                get("/api/user/checkpassword").

                then().statusCode(200).  // check that the status code is 200
                body(containsString("true")).
                log().all();

    }

    @Test
    public void test13_CheckPasswordShouldReturnFalseIfEnteredPasswordIsWrongForTheUser() {
        given().
                contentType("application/json; charset=UTF-8").
                header("Authorization", "Bearer " + token).
                param("initialPassword", "123467").
                urlEncodingEnabled(false).
                log().all().
                when().
                get("/api/user/IsEmailUsedByOther").
                then().statusCode(200).  // check that the status code is 200
                body(containsString("false")).
                log().all();

    }

    @Test
    public void test14_ChangePassword() {

        given().
                contentType("application/json; charset=UTF-8").
                header("Authorization", "Bearer " + token).
                urlEncodingEnabled(false).
                log().all().
                when().
                body("{\"currentPassword\":\"" + initialPassword + "\",\"newPassword\":\"" + newPassword + "\"}").
                post("/api/user/changepassword").
                then().statusCode(200).  // check that the status code is 200
                log().all();

        SignInDto signInReq = new SignInDto();
        signInReq.email = "nkvyrslx@sharklasers.com";
        signInReq.password = newPassword;


        token = given().
                contentType("application/json; charset=UTF-8").
                body(signInReq).
                post("/api/account/login").
                then().
                statusCode(200).
                body(containsString("token")).extract().path("token.token");
    }

    @Test
    public void test14_findEmailShouldReturnTrueIfEmailIsOfUser() {
        Response response=given().
                contentType("application/json; charset=UTF-8").
                header("Authorization", "Bearer " + token).
                param("q", "haral").
                urlEncodingEnabled(false).
                log().all().
                when().
                get("/api/user/findEmails").
                then().statusCode(200).  // check that the status code is 200
                body(containsString("haralampi@nm.com")).
                log().all().
                extract().response();
    }
    @Test
    public void test15_findUserShouldReturnSetOfUsers() {
        Response response=given().
                contentType("application/json; charset=UTF-8").
                header("Authorization", "Bearer " + token).
                param("q", "Kenneth").
                param("country", "NO").
                urlEncodingEnabled(false).
                log().all().
                when().
                get("/api/user/find").
                then().statusCode(200).  // check that the status code is 200
                body(containsString("Kenneth")).
                body(containsString("Hauge")).
                log().all().
                extract().response();

        ArrayList<HashMap<String,Object>> map=response.jsonPath().get();
        for (HashMap<String, Object> aMap : map) {
            Assert.assertThat(aMap.get("firstName").toString(), startsWith("Kenneth"));
        }
    }


    @AfterClass
    public static void tearDown() {
        SQLService.restorePassword(email);
    }
}

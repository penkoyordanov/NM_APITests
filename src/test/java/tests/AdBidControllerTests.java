package tests;

import API.NovamanusAPI;
import Enums.Password;
import Enums.UserEmail;
import org.junit.BeforeClass;
import org.junit.Test;

import static io.restassured.RestAssured.given;

public class AdBidControllerTests extends BaseTest{
    static String tokenLori;
    private static final String APIKEY="lcgnnXNzgeP4EcBqTODL8Oog10whRjmA5aYQoc3B7eF5wETrHq1iEnfdMngvBRZ";
    @BeforeClass
    public static void setUp() {
        setUpBase();
        tokenLori = NovamanusAPI.autchenticationShouldReturnToken(UserEmail.LORISCOTT, Password.DEFAULT_PASSWORD);
    }

    @Test
    public void test01_ProcessBidAds(){
        given().
                contentType("application/json; charset=UTF-8").
//                header("Authorization", "Bearer " + tokenLori).
                header("ApiKey", APIKEY).
                log().all().
                when().
                get("/api/nmadvertisements/process").
                then().
                statusCode(200).  // check that the status code is 200
                log().all();
    }
}

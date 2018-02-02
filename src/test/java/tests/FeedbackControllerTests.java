package tests;

import API.NovamanusAPI;
import Enums.Password;
import Enums.UserEmail;
import helpers.TestDataFaker;
import io.restassured.config.MultiPartConfig;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.IOException;

import static io.restassured.RestAssured.config;
import static io.restassured.RestAssured.given;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FeedbackControllerTests extends BaseTest{
    private static String tokenIvan;
    @BeforeClass
    public static void setUp() {
        setUpBase();
        tokenIvan = NovamanusAPI.autchenticationShouldReturnToken(UserEmail.TESTUSR, Password.TESTUSR_PASSWORD);
    }

    @Test
    public void test01_SendFeedback() throws IOException {

        given().config(config().multiPartConfig(MultiPartConfig.multiPartConfig().defaultBoundary("abc"))).
                multiPart("subject", TestDataFaker.getTitle()).
                multiPart("description", TestDataFaker.getDescription()).
                when().header("Authorization", "Bearer " + tokenIvan).log().all().
                post("/api/feedback").
                then().
                statusCode(200).
                log().all();
    }
}

package tests;

import API.NovamanusAPI;
import Enums.Password;
import Enums.UserEmail;
import SQL.SQLService;
import io.restassured.http.ContentType;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.IsEqual.equalTo;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BusinessProfileControllerTests extends BaseTest {
    static int businessProfileID=314;
    private static Map<String, String> businessDetails = SQLService.getBusinessDetails(businessProfileID);
    private static String tokenIvan;

    @BeforeClass
    public static void setUp() {
        setUpBase();
        tokenIvan = NovamanusAPI.autchenticationShouldReturnToken(UserEmail.TESTUSR, Password.TESTUSR_PASSWORD);
    }

    @Test
    public void test01_verifyBusinessProfileDetails() {
        given().
                contentType("application/json; charset=UTF-8").
                when().header("Authorization", "Bearer " + tokenIvan).
                param("profileId", businessProfileID).
                get("/api/business/GetBusinessProfile").
                then().statusCode(200).  // check that the status code is 200
                contentType(ContentType.JSON).  // check that the content type return from the API is JSON
                log().all().
                body("id", equalTo(businessProfileID)).
                body("vat", equalTo(businessDetails.get("vat"))).
                body("name", equalTo(businessDetails.get("name"))).
                body("email", equalTo(businessDetails.get("email"))).
                body("address.address", equalTo(businessDetails.get("address")));
    }


}

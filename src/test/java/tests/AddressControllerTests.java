package tests;

import API.NovamanusAPI;
import Dto.LocationDto;
import Enums.Password;
import Enums.UserEmail;
import io.restassured.http.ContentType;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import static io.restassured.RestAssured.given;

public class AddressControllerTests extends BaseTest {

    static String token ="";
    @BeforeClass
    public static void setUp() {
        setUpBase();
        token= NovamanusAPI.autchenticationShouldReturnToken(UserEmail.TESTUSR, Password.TESTUSR_PASSWORD);

    }

    @Test
    public void VerifyAddress() {

        response = given().
                log().all().
                contentType("application/json; charset=UTF-8").
                when().
                header("Authorization", "Bearer " + token).
                get("/api/user/getdefaultaddress").
                then().statusCode(200).  // check that the status code is 200
                contentType(ContentType.JSON).  // check that the content type return from the API is JSON
                extract().response(); // extract the response

        String userAddress = "ul. \"Indzhe voyvoda\" 7, 1309 Sofia, Bulgaria";
        Assert.assertEquals("User address is not as expected: " + userAddress, userAddress,getValueFromJSONResponse(response, "addresses[0].address"));
        String ADDRESSPLACEID = "ChIJfVmbqk6FqkARgtm9_snqvBM";
        Assert.assertEquals("Address placeID is not as expected: " + ADDRESSPLACEID, ADDRESSPLACEID,getValueFromJSONResponse(response, "addresses[0].placeId"));
        String areaCountryCode = "BG";
        Assert.assertEquals("Address countryCode is not as expected: " + areaCountryCode, areaCountryCode,getValueFromJSONResponse(response, "addresses[0].countryCode"));
        String areaName = "Sofia, Bulgaria";

        LocationDto address = new LocationDto(response);
        System.out.println("Success");
    }
}

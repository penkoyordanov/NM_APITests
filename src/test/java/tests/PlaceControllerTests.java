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
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static io.restassured.module.jsv.JsonSchemaValidatorSettings.settings;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PlaceControllerTests extends BaseTest {
    private static String tokenJohnDoe;

    @BeforeClass
    public static void setUp() {
        setUpBase();
        tokenJohnDoe = NovamanusAPI.autchenticationShouldReturnToken(UserEmail.JOHNDOE, Password.DEFAULT_PASSWORD);
    }

    @Test
    public void test01_GetAllCountries() {

        given().
                contentType("application/json").
                log().all().
                when().
                header("Authorization", "Bearer " + tokenJohnDoe).
                get("/api/place/countries").
                then().statusCode(200).  // check that the status code is 200
                body(containsString("Andorra")).
                assertThat().body(matchesJsonSchemaInClasspath("GetAllCountriesReponseSchema.json").using(settings().with().checkedValidation(false))).
                log().all();
    }

    @Test
    public void test02_GetCitiesByCountry() {

        given().
                contentType("application/json").
                log().all().
                when().
                header("Authorization", "Bearer " + tokenJohnDoe).
                param("countryCode", "NO").
                get("/api/place/cities").
                then().statusCode(200).  // check that the status code is 200
                body(containsString("Oslo")).
                body(not(containsString("Sofia"))).
//                assertThat().body(matchesJsonSchemaInClasspath("GetCitiesByCountry.json").using(settings().with().checkedValidation(false))).
        log().all();
    }

    @Ignore
    @Test
    public void test03_GetAreaForPlace() {
        String placeID = "ChIJ9Xsxy4KGqkARYF6_aRKgAAQ";
        given().
                contentType("application/json").
                log().all().
                when().
                header("Authorization", "Bearer " + tokenJohnDoe).
                get("/api/place/getAreaForPlace/{placeID}", placeID).
                then().statusCode(200).  // check that the status code is 200
                log().all();
    }
}

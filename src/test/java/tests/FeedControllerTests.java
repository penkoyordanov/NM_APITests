package tests;

import API.NovamanusAPI;
import Dto.SimpleAdDto;
import Enums.Password;
import Enums.UserEmail;
import Responses.GetFeedAdsResponse;
import io.restassured.response.Response;
import org.junit.*;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;
import java.util.HashMap;

import static io.restassured.RestAssured.given;
import static junit.framework.TestCase.fail;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FeedControllerTests extends BaseTest {
    private static String tokenJohnDoe;
    private static String tokenIvan;

    @BeforeClass
    public static void setUp() {
        setUpBase();
        tokenJohnDoe = NovamanusAPI.autchenticationShouldReturnToken(UserEmail.JOHNDOE,Password.DEFAULT_PASSWORD);
        tokenIvan = NovamanusAPI.autchenticationShouldReturnToken(UserEmail.TESTUSR, Password.TESTUSR_PASSWORD);
    }

    @Test
    public void test01_GetAdsOnFeed() {


        given().
                contentType("application/json").
                log().all().
                when().
                header("Authorization", "Bearer " + tokenJohnDoe).
                param("pageIndex", 0).
                param("pageSize", 12).
                param("country", "").
                param("pricefrom", "").
                param("priceto", "").
                param("searchedUserId", "").
                param("areaId", "").
                param("adQuery", "").
                param("categories", 0, 1, 2, 3, 4, 6).
                param("order", 0).
                get("/api/feed/load").
                then().statusCode(200).  // check that the status code is 200
//                assertThat().body(matchesJsonSchemaInClasspath("GetFeedAdsResponseSchema.json").using(settings().with().checkedValidation(false))).
                body(not(containsString("ad to be deleted"))).
                log().all();
    }

    @Test
    public void test02_GetAdsByCategory() {

        for (int i = 0; i < 7; i++) {
            Response response = given().
                    contentType("application/json").
                    log().all().
                    when().
                    header("Authorization", "Bearer " + tokenJohnDoe).
                    param("pageSize", 12).
                    param("pageIndex", 0).
                    param("country", "").
                    param("pricefrom", "").
                    param("searchedUserId", "").
                    param("areaId", "").
                    param("adQuery", "").
                    param("categories", i).
                    param("order", 0).
                    get("/api/feed/load").
                    then().statusCode(200).log().all().
                    extract().response();  // check that the status code is 200

            GetFeedAdsResponse getFeedAdsResponse = response.as(GetFeedAdsResponse.class);
            for (int a = 0; a < getFeedAdsResponse.ads.length; a++) {
                Assert.assertThat(getFeedAdsResponse.ads[a].getCategory(), equalTo(i));

            }
        }
    }

    @Test
    public void test03_GetAdsByCountryOnFeed() {
        Response response = given().
                contentType("application/json").
                log().all().
                when().
                header("Authorization", "Bearer " + tokenJohnDoe).
                param("pageIndex", 0).
                param("pageSize", 12).
                param("countryCode", "NO").
                param("pricefrom", "").
                param("priceto", "").
                param("areaPlaceId", "").
                param("adQuery", "").
                param("categories", 0, 1, 2, 3, 4, 6).
                param("order", 0).
                get("/api/feed/load").
                then().log().all().extract().response();

        GetFeedAdsResponse getFeedAdsResponse = response.getBody().as(GetFeedAdsResponse.class);

        for (SimpleAdDto ad : getFeedAdsResponse.ads) {
            Assert.assertThat(ad.countryCode, equalTo("NO"));
        }
    }

    @Test
    public void test04_GetAdsInSofia() {

        Response response = given().
                contentType("application/json").
                log().all().
                when().
                header("Authorization", "Bearer " + tokenJohnDoe).
                param("pageIndex", 0).
                param("pageSize", 12).
                param("countryID", "").
                param("countryName", "Bulgaria").
                param("pricefrom", "").
                param("priceto", "").
                param("searchedUserId", "").
                param("placeName", "Sofia").
                param("adQuery", "").
                param("categories", 0, 1, 2, 3, 4, 6).
                param("order", 0).
                get("/api/feed/load").
                then().log().all().extract().response();

        GetFeedAdsResponse getFeedAdsResponse = response.getBody().as(GetFeedAdsResponse.class);
        Assert.assertThat(getFeedAdsResponse.count,greaterThan(0));

        for (SimpleAdDto ad : getFeedAdsResponse.ads) {
            Assert.assertThat(ad.getLocation(), equalTo("Sofia"));
        }
    }

    @Ignore("Search by city need to be fixed")
    @Test
    public void test05_GetAdsInHorten() {

        Response response = given().
                contentType("application/json").
                log().all().
                when().
                header("Authorization", "Bearer " + tokenJohnDoe).
                param("pageIndex", 0).
                param("pageSize", 12).
                param("countryID", "").
                param("countryName", "Norway").
                param("pricefrom", "").
                param("priceto", "").
                param("searchedUserId", "").
                param("placeName", "Horten").
                param("adQuery", "").
                param("categories", 0, 1, 2, 3, 4, 6).
                param("order", 0).
                get("/api/feed/load").
                then().log().all().
                extract().response();

        GetFeedAdsResponse getFeedAdsResponse = response.getBody().as(GetFeedAdsResponse.class);
        Assert.assertThat(getFeedAdsResponse.count,greaterThan(0));

        for (SimpleAdDto ad : getFeedAdsResponse.ads) {
            Assert.assertThat(ad.getLocation(), equalTo("Horten"));
        }
    }

    @Test
    public void test06_GetAdsByCountryinPriceRange() {

        Response response = given().
                contentType("application/json").
                log().all().
                when().
                header("Authorization", "Bearer " + tokenJohnDoe).
                param("pageIndex", 0).
                param("pageSize", 12).
                param("countryID", "").
                param("countryName", "Norway").
                param("pricefrom", "50").
                param("priceto", "300").
                param("searchedUserId", "").
                param("areaId", "").
                param("adQuery", "").
                param("categories", 0, 1, 2, 3, 4, 6).
                param("order", 0).
                get("/api/feed/load").
                then().log().all().extract().response();

        GetFeedAdsResponse getFeedAdsResponse = response.getBody().as(GetFeedAdsResponse.class);

        for (SimpleAdDto ad : getFeedAdsResponse.ads) {
            Assert.assertThat(ad.getPrice(), greaterThanOrEqualTo(50l));
            Assert.assertThat(ad.getPrice(), lessThanOrEqualTo(300l));
        }
    }

    @Test
    public void test07_GetAdsHavingKeyword() {
        Response response = given().
                contentType("application/json").
                log().all().
                when().
                header("Authorization", "Bearer " + tokenIvan).
                param("pageIndex", 0).
                param("pageSize", 12).
                param("countryCode", "NO").
                param("pricefrom", "").
                param("priceto", "").
                param("areaPlaceId", "").
                param("adQuery", "iphone").
                param("categories", 0, 1, 2, 3, 4, 6).
                param("order", 0).
                get("/api/feed/load").
                then().
                body(containsString("iphone")).
                log().all().
                extract().response();

        GetFeedAdsResponse getFeedAdsResponse = response.getBody().as(GetFeedAdsResponse.class);

        for (SimpleAdDto ad : getFeedAdsResponse.ads) {
//            Assert.assertThat(ad.getTitle(), containsString("iphone"));

            given().
                    contentType("application/json").
                    log().all().
                    when().
                    header("Authorization", "Bearer " + tokenIvan).
                    get("/api/nmadvertisements/{0}", ad.getId()).
                    then().statusCode(200).  // check that the status code is 200
                    body(containsString("hone"));
        }
    }

    @Test
    public void test08_GetAdsWithinUniversity() {

        Response response = given().
                contentType("application/json").
                log().all().
                when().
                header("Authorization", "Bearer " + tokenIvan).
                param("pageIndex", 0).
                param("pageSize", 12).
                param("countryCode", "").
                param("pricefrom", "").
                param("priceto", "").
                param("areaPlaceId", "ChIJ54xUkSeEqkARedKQ2ZE2Olw").
                param("adQuery", "").
                param("categories", 0, 1, 2, 3, 4, 6).
                param("order", 0).
                get("/api/feed/load").
                then().
                log().all().
                extract().response();

        GetFeedAdsResponse getFeedAdsResponse = response.getBody().as(GetFeedAdsResponse.class);

        for (SimpleAdDto ad : getFeedAdsResponse.ads) {
            Assert.assertThat(ad.getLocation(), containsString("Technical University"));
        }
    }

    @Test
    public void test09_GetSuggestionByAdTitle() {

        Response response=given().
                contentType("application/json").
                log().all().
                when().
                header("Authorization", "Bearer " + tokenIvan).
                param("pageSize", 10).
                param("countryID", "").
                param("placeID", "").
                param("pricefrom", "").
                param("priceto", "").
                param("term", "dolore").
                param("categories", 0, 1, 2, 3, 4, 6).
                get("/api/feed/suggest").
                then().statusCode(200).  // check that the status code is 200
                log().all().extract().response();

        //Get all suggestions matching "dolore"
        ArrayList<HashMap<String, Object>> map = response.jsonPath().get();

        //Iterate over results and get details for each advertisement
        for (HashMap<String, Object> aMap : map) {
            response=given().
                    contentType("application/json").
                    log().all().
                    when().
                    header("Authorization", "Bearer " + tokenIvan).
                    get("/api/nmadvertisements/{0}", aMap.get("objectId")).
                    then().statusCode(200).extract().response();  // check that the status code is 200

            if (response.getBody().jsonPath().getString("title").contains("dolore")) {
                break;
            }else if(response.getBody().jsonPath().getString("description").contains("dolore")){
                break;
            }else {
                fail("\"Dolore\" is not found in ad title and description");
            }

        }
    }

    @Test
    public void test10_GetSuggestionByUserFullName() {

        response=given().
                contentType("application/json").
                log().all().
                when().
                header("Authorization", "Bearer " + tokenIvan).
                param("pageSize", 10).
                param("countryCode", "").
                param("pricefrom", "").
                param("areaPlaceId", "").
                param("priceto", "").
                param("term", "Ivan").
                param("categories", 0, 1, 2, 3, 4, 6).
                get("/api/feed/suggest").
                then().statusCode(200).  // check that the status code is 200
                /*body("[0].name", equalTo("Aleksander Gjessing")).
                body("[0].objectData", equalTo("ivan.ivanov84")).*/
                log().all().extract().response();

        ArrayList<HashMap<String, Object>> map = response.jsonPath().get();

        for (HashMap<String, Object> aMap : map) {
            Assert.assertThat((String) aMap.get("name"), startsWith("Ivan"));
        }

    }


}

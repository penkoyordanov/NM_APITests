package tests;

import API.NovamanusAPI;
import Dto.AdDto;
import Enums.Password;
import Enums.UserEmail;
import Enums.Usernames;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.IsEqual.equalTo;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AdOfferControllerTests extends BaseTest {
    private static String tokenIvan;
    private static String tokenLori;
    private static int makeOfferAdId = 307;
    private static float newBid;
    private static int offerID;
    private static final String LORIUSERNAME= Usernames.USERNAME_LORI.getUsername();

    @BeforeClass
    public static void setUp() {
        setUpBase();
        tokenIvan = NovamanusAPI.autchenticationShouldReturnToken(UserEmail.TESTUSR, Password.TESTUSR_PASSWORD);
        tokenLori = NovamanusAPI.autchenticationShouldReturnToken(UserEmail.LORISCOTT, Password.DEFAULT_PASSWORD);
    }

    @Before
    public void createNewAdIfInitialisInactive() {
        boolean isInactive = given().
                contentType("application/json; charset=UTF-8").
                header("Authorization", "Bearer " + tokenLori).
                log().all().
                when().
                get("/api/nmadvertisements/{0}", makeOfferAdId).
                then().
                statusCode(200).
                log().all().
                extract().path("isInactive");

        //If advertisement is inactive create new advertisement
        if (isInactive) {
            AdDto adDto = new AdDto();
            adDto.setLocationDto(NovamanusAPI.GetContactAddress(tokenLori));
            adDto.setContact(NovamanusAPI.GetContactDetailsByToken(tokenLori));
            adDto.setTitle("Make an offer ad");
            adDto.setCategory(6);
            int condition = 1;
            adDto.setCondition(condition);
            adDto.setMinPrice(20);

            ObjectMapper mapper = new ObjectMapper();
            String body = "";
            //Object to JSON in String
            try {
                body = mapper.writeValueAsString(adDto);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
//            AdDto body=new AdDto();
             given().
                    contentType("application/json").log().all().
                    body(body).
                    when().
                    header("Authorization", "Bearer " + tokenLori).
                    post("/api/nmadvertisements/publish").
                    then().statusCode(200).  // check that the status code is 200
                    log().all();

            makeOfferAdId=given().
                    contentType("application/json").
                    log().all().
                    when().
                    header("Authorization", "Bearer " + tokenLori).
                    queryParam("pageindex", 0).
                    queryParam("pageSize", 8).
                    queryParam("group", 0).
                    get("/api/nmadvertisements/author/{0}",LORIUSERNAME).
                    then().statusCode(200).  // check that the status code is 200
                    log().all().
                    contentType(ContentType.JSON).  // check that the content type return from the API is JSON
                    extract().path("items[0].id");
        }
    }

    @Test
    public void test01_MakeAnOfferOnAd() {

        Response response = given().
                contentType("application/json; charset=UTF-8").
                header("Authorization", "Bearer " + tokenIvan).
                param("takeCount", 4).
                log().all().
                when().
                get("/api/advoffer/{0}", makeOfferAdId).
                then().
                statusCode(200).
                log().all().extract().response();

        if (response.getBody().jsonPath().getInt("totalCount") == 0) {
            newBid = 21;
        } else {
            newBid = response.getBody().jsonPath().getFloat("items[0].price") + 1.0f;
        }


        //Make new offer
        offerID = given().
                contentType("application/json; charset=UTF-8").
                header("Authorization", "Bearer " + tokenIvan).
                urlEncodingEnabled(false).
                log().all().
                when().
                body("{\"adId\":\"" + makeOfferAdId + "\",\"price\":\"" + newBid + "\"}").
                post("/api/advoffer").
                then().statusCode(200).  // check that the status code is 200
                body("data.price", equalTo(newBid)).
                body("data.bidder.firstName", equalTo("Ivan")).
                log().all().
                extract().path("data.id");
    }

    @Test
    public void test02_TryBidWithSamePriceShouldReturnError() {
        given().
                contentType("application/json; charset=UTF-8").
                header("Authorization", "Bearer " + tokenIvan).
                urlEncodingEnabled(false).
                log().all().
                when().
                body("{\"adId\":\"" + makeOfferAdId + "\",\"price\":\"" + newBid + "\"}").
                post("/api/advoffer").
                then().statusCode(200).  // check that the status code is 200
                body("error", equalTo(2)).
                log().all();
    }

    @Test
    public void test03_DeleteOfferOnAd() {
        //Delete an offer
        given().
                contentType("application/json; charset=UTF-8").
                header("Authorization", "Bearer " + tokenLori).
                urlEncodingEnabled(false).
                log().all().
                when().
                delete("/api/advoffer/{0}", offerID).
                then().statusCode(200).  // check that the status code is 200
                log().all();

        given().
                contentType("application/json; charset=UTF-8").
                header("Authorization", "Bearer " + tokenIvan).
                param("takeCount", 4).
                log().all().
                when().
                get("/api/advoffer/{0}", offerID).
                then().
                statusCode(200).
                log().all().
                body(not(containsString(String.valueOf(newBid))));

    }

}

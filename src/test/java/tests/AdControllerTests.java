package tests;

import API.NovamanusAPI;
import Dto.AdDto;
import Dto.AdMultimediaDto;
import Dto.Contact;
import Dto.LocationDto;
import Enums.Enums;
import Enums.Password;
import Enums.UserEmail;
import Responses.GetSavedAdsResponse;
import SQL.SQLService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.TestDataFaker;
import helpers.TestDataGenerator;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.*;
import org.junit.runners.MethodSorters;

import java.util.Date;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertEquals;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AdControllerTests extends BaseTest {
    private static Contact contact;
    private static LocationDto address;

    private static String title;
    private static String description;
    private static AdMultimediaDto adMultimediaDto;
    private static int createdAdID = 0;
    private static AdDto adDto;
    private static String tokenLori;
    private static String tokenIvan;
    private static String LoriUserName = "LoriScott861";
    private static int createdDraftAdID;
    private ObjectMapper mapper = new ObjectMapper();
    private String body;

    @BeforeClass
    public static void setUp() {
        setUpBase();
        tokenLori = NovamanusAPI.autchenticationShouldReturnToken(UserEmail.LORISCOTT, Password.DEFAULT_PASSWORD);
        tokenIvan = NovamanusAPI.autchenticationShouldReturnToken(UserEmail.TESTUSR,Password.TESTUSR_PASSWORD);
        contact = NovamanusAPI.GetContactDetailsByToken(tokenLori);
        address = NovamanusAPI.GetContactAddress(tokenLori);
    }

    private AdDto setupAdDetails() {
        adDto = new AdDto();
        title = TestDataFaker.getTitle();
        description = TestDataFaker.getDescription();
        adDto.setLocationDto(address);
        adDto.setContact(contact);
        adDto.setTitle(title);
        /*adDto.setCategory(Enums.category.SELL.getCategory());
        adDto.setCondition(Enums.itemCondition.NEW.getItemCondition());*/
        adDto.setPrice(200);
        adDto.setDescription(description);
        adDto.setKeywords(new String[]{TestDataFaker.getKeyword(), TestDataFaker.getKeyword()});
        return adDto;
    }


    @Test
    public void test01_PublishAdWithImage() {
        adDto = setupAdDetails();
        adDto.setCategory(Enums.category.SELL.getCategory());
        adDto.setCondition(Enums.itemCondition.NEW.getItemCondition());
        adMultimediaDto = NovamanusAPI.uploadAdMultimedia("iphone6s_p5.JPG", tokenLori);
        adDto.setMedia(adMultimediaDto);

        //Object to JSON in String
        try {
            body = mapper.writeValueAsString(adDto);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        given().
                contentType("application/json").log().all().
                body(body).
                when().
                header("Authorization", "Bearer " + tokenLori).
                post("/api/nmadvertisements/publish").
                then().statusCode(200).  // check that the status code is 200
                log().all();


        createdAdID = given().
                contentType("application/json").
                log().all().
                when().
                header("Authorization", "Bearer " + tokenLori).
                queryParam("pageindex", 0).
                queryParam("pageSize", 8).
                queryParam("group", 0).
                get("/api/nmadvertisements/author/{0}", LoriUserName).
                then().statusCode(200).  // check that the status code is 200
                log().all().
                contentType(ContentType.JSON).  // check that the content type return from the API is JSON
                body("items[0].title", equalTo(title)).
                body("items[0].description", equalTo(description)).
                extract().path("items[0].id");
        System.out.println(createdAdID);
    }

    @Test
    public void test02_getAdvertisementShouldReturnAdDetails() {
        given().
                contentType("application/json").
                log().all().
                when().
                header("Authorization", "Bearer " + tokenLori).
                get("/api/nmadvertisements/{0}", createdAdID).
                then().statusCode(200).  // check that the status code is 200
                body("title", equalTo(title)).
                body("description", equalTo(description)).
                body("isSold", equalTo(false)).
                body("media[0].originalUrl", equalTo("https://novamanus.blob.core.windows.net/nmphoto/" + adMultimediaDto.getOriginalName())).
                body("media[0].originalName", equalTo(adMultimediaDto.getOriginalName())).
                body("media[0].name", equalTo(adMultimediaDto.getOriginalName()));
//                body("media[0].url", equalTo(adMultimediaDto.getUrl())).extract().path("id");
    }

    @Ignore("Obsolete")
    @Test
    public void test03_GetTodayAdsCount() {
        Response response = given().
                contentType("application/json").
                log().all().
                when().
                header("Authorization", "Bearer " + tokenLori).
                get("/api/nmadvertisements/author/{username}/today", LoriUserName).
                then().statusCode(200).  // check that the status code is 200
                log().all().
                extract().response();
        assertEquals(SQLService.getCountTodaysAds(9), response.body().asString());
    }

    @Test
    public void test04_SaveAdToFavourites() {
        given().
                contentType("application/json").
                log().all().
                when().
                header("Authorization", "Bearer " + tokenIvan).
                body("{\"save\":true,\"id\":" + createdAdID + "}").
                post("/api/nmadvertisements/{id}/favourite",createdAdID).
                then().statusCode(200);  // check that the status code is 200

        Response response = given().
                contentType("application/json").
                log().all().
                when().
                header("Authorization", "Bearer " + tokenIvan).
                param("pageindex", 0).
                param("pageSize", 8).
                get("/api/nmadvertisements/saved").
                then().
                statusCode(200).
                extract().response();

        GetSavedAdsResponse getSavedAdsResponse = response.as(GetSavedAdsResponse.class);
        Assert.assertThat(getSavedAdsResponse.items[0].id, equalTo(createdAdID));

    }

    @Test
    public void test06_RemoveAdFromFavourites() {
        given().
                contentType("application/json").
                log().all().
                when().
                header("Authorization", "Bearer " + tokenIvan).
                delete("/api/nmadvertisements/{id}/favourite",createdAdID).
                then().statusCode(200);  // check that the status code is 200

        Response response = given().
                contentType("application/json").
                log().all().
                when().
                header("Authorization", "Bearer " + tokenIvan).
                param("pageindex", 0).
                param("pageSize", 8).
                get("/api/nmadvertisements/saved").
                then().
                statusCode(200).
                extract().response();

    }

    @Test
    public void test08_SellAdvartise() {
        given().
                contentType("application/json").
                log().all().
                when().
                header("Authorization", "Bearer " + tokenLori).
                post("/api/nmadvertisements/{adID}/sell", createdAdID).
                then().statusCode(200);  // check that the status code is 200

        //Assert ad is sold
        given().
                contentType("application/json").
                log().all().
                when().
                header("Authorization", "Bearer " + tokenLori).
                get("/api/nmadvertisements/{0}", createdAdID).
                then().statusCode(200).  // check that the status code is 200
                body("isSold", equalTo(true));
    }

    @Test
    public void test09_UnSoldAdvartise() {
        given().
                contentType("application/json").
                log().all().
                when().
                header("Authorization", "Bearer " + tokenLori).
                delete("/api/nmadvertisements/{adID}/sell", createdAdID).
                then().statusCode(200);  // check that the status code is 200

        //Assert ad is sold
        given().
                contentType("application/json").
                log().all().
                when().
                header("Authorization", "Bearer " + tokenLori).
                get("/api/nmadvertisements/{0}", createdAdID).
                then().statusCode(200).  // check that the status code is 200
                body("isSold", equalTo(false));
    }


    @Test
    public void test10_UpdateAd() {
        adDto = setupAdDetails();
        adDto.setCategory(Enums.category.SELL.getCategory());
        adDto.setCondition(Enums.itemCondition.NEW.getItemCondition());

        //Object to JSON in String
        try {
            body = mapper.writeValueAsString(adDto);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        given().
                contentType("application/json").log().all().
                body(body).
                when().
                header("Authorization", "Bearer " + tokenLori).
                post("/api/nmadvertisements/publish").
                then().statusCode(200).  // check that the status code is 200
                log().all();

        given().
                contentType("application/json").
                log().all().
                when().
                header("Authorization", "Bearer " + tokenLori).
                queryParam("pageindex", 0).
                queryParam("pageSize", 8).
                queryParam("group", 0).
                get("/api/nmadvertisements/author/{0}", LoriUserName).
                then().statusCode(200).  // check that the status code is 200
                log().all().
                contentType(ContentType.JSON).  // check that the content type return from the API is JSON
                body("items[0].title", equalTo(adDto.getTitle())).
                body("items[0].description", equalTo(adDto.getDescription()));
    }

    @Test
    public void test11_RemoveAd() {
        given().
                contentType("application/json").
                log().all().
                when().
                header("Authorization", "Bearer " + tokenLori).
                delete("/api/nmadvertisements/{0}", createdAdID).
                then().statusCode(200);  // check that the status code is 200

        given().
                contentType("application/json").
                log().all().
                when().
                header("Authorization", "Bearer " + tokenLori).
                queryParam("pageindex", 0).
                queryParam("pageSize", 8).
                queryParam("group", 0).
                get("/api/nmadvertisements/author/LoriScott861").
                then().statusCode(200).  // check that the status code is 200
                log().all().
                contentType(ContentType.JSON).  // check that the content type return from the API is JSON
                body("items[0].id", not(equalTo(createdAdID))); // check that the response not contais title of deleted ad
    }

    @Test
    public void test12_RemovedAdShouldBeAvailableInHistory() {
        given().
                contentType("application/json").
                log().all().
                when().
                header("Authorization", "Bearer " + tokenLori).
                queryParam("pageindex", 0).
                queryParam("pageSize", 8).
                queryParam("group", 1).
                get("/api/nmadvertisements/author/LoriScott861").
                then().statusCode(200).  // check that the status code is 200
                log().all().
                contentType(ContentType.JSON).  // check that the content type return from the API is JSON
                body(containsString(String.valueOf(createdAdID))); // check that the response not contais title of deleted ad
    }

    @Ignore
    @Test
    public void test13_TryToSoldRemovedAd() {
        given().
                contentType("application/json").
                log().all().
                when().
                header("Authorization", "Bearer " + tokenLori).
                body("{\"sold\":true,\"id\":" + 550 + "}").
                post("/api/adv/sell").
                then().statusCode(200);  // check that the status code is 200
    }

    @Test
    public void test14_SaveDraftAd() {
        adDto = new AdDto();
        adDto.setLocationDto(address);
        adDto.setContact(contact);
        adDto.setTitle(title);
        int category = 0;
        adDto.setCategory(category);
        int condition = 1;
        adDto.setCondition(condition);
        Integer price = 200;
        adDto.setPrice(price);

        ObjectMapper mapper = new ObjectMapper();
        String body = "";
        //Object to JSON in String
        try {
            body = mapper.writeValueAsString(adDto);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        response = given().
                contentType("application/json").log().all().
                body(body).
                when().
                header("Authorization", "Bearer " + tokenLori).
                post("/api/nmadvertisements").
                then().statusCode(200).  // check that the status code is 200
                log().all().
                extract().response();
        createdDraftAdID = Integer.parseInt(response.asString());

        given().
                contentType("application/json").
                log().all().
                when().
                header("Authorization", "Bearer " + tokenLori).
                queryParam("pageindex", 0).
                queryParam("pageSize", 8).
                queryParam("group", 2).
                get("/api/nmadvertisements/author/{0}", LoriUserName).
                then().statusCode(200).  // check that the status code is 200
                log().all().
                contentType(ContentType.JSON).  // check that the content type return from the API is JSON
                body("items[0].title", equalTo(title));
        System.out.println(createdDraftAdID);
    }

    @Test
    public void test15_deleteDraftAd() {
        given().
                contentType("application/json").
                log().all().
                when().
                header("Authorization", "Bearer " + tokenLori).
                delete("/api/nmadvertisements/{0}", createdDraftAdID).
                then().statusCode(200);  // check that the status code is 200

        given().
                contentType("application/json").
                log().all().
                when().
                header("Authorization", "Bearer " + tokenLori).
                queryParam("pageindex", 0).
                queryParam("pageSize", 8).
                queryParam("group", 2).
                get("/api/nmadvertisements/author/LoriScott861").
                then().statusCode(200).  // check that the status code is 200
                log().all().
                contentType(ContentType.JSON).  // check that the content type return from the API is JSON
                body(not(containsString(title))); // check that the response not contais title of deleted ad
    }

    @Test
    public void test16_GetAdStatistics() {
        int adId = 1516;
        Map<String, String> adStats = SQLService.getAdStatistics(adId);
        given().
                contentType("application/json").
                log().all().
                when().
                header("Authorization", "Bearer " + tokenLori).
                get("/api/nmadvertisements/{id}/stats", adId).
                then().statusCode(200).  // check that the status code is 200
                body("viewedCount", equalTo(Integer.parseInt(adStats.get("Views")))).
                body("savedCount", equalTo(Integer.parseInt(adStats.get("Saved")))).
                body("commentsCount", equalTo(Integer.parseInt(adStats.get("Comments")))).
                body("emailedCount", equalTo(Integer.parseInt(adStats.get("Mailed"))));
                /*body("sharedCount", equalTo(Integer.parseInt(adStats.get("Shared"))))*/
    }
    @Test
    public void test17_PublishAdCategoryBid() {
        Date date=new Date();
        date.getTime();
        adDto = setupAdDetails();
        adDto.setCategory(Enums.category.BID.getCategory());
        adDto.setMinPrice(200);
        adDto.setBidEndDate(TestDataGenerator.getCurrentDate());
        adDto.setBidEndTime(TestDataGenerator.getTimeOneHourInAdvance());


        //Object to JSON in String
        try {
            body = mapper.writeValueAsString(adDto);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        given().
                contentType("application/json").log().all().
                body(body).
                when().
                header("Authorization", "Bearer " + tokenLori).
                post("/api/nmadvertisements/publish").
                then().statusCode(200).  // check that the status code is 200
                log().all();


        createdAdID = given().
                contentType("application/json").
                log().all().
                when().
                header("Authorization", "Bearer " + tokenLori).
                queryParam("pageindex", 0).
                queryParam("pageSize", 8).
                queryParam("group", 0).
                get("/api/nmadvertisements/author/{0}", LoriUserName).
                then().statusCode(200).  // check that the status code is 200
                log().all().
                contentType(ContentType.JSON).  // check that the content type return from the API is JSON
                body("items[0].title", equalTo(title)).
                body("items[0].description", equalTo(description)).
                extract().path("items[0].id");
        System.out.println(createdAdID);
    }

}

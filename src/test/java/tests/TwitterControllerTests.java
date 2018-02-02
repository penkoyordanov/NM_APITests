package tests;

import API.NovamanusAPI;
import Enums.Password;
import Enums.UserEmail;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import java.net.MalformedURLException;
import java.net.URL;

import static io.restassured.RestAssured.given;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TwitterControllerTests  extends BaseTest{
    private static String tokenPenko = "";
    WebDriver driver;

    @BeforeClass
    public static void setUp() {
        setUpBase();
        tokenPenko = NovamanusAPI.autchenticationShouldReturnToken(UserEmail.PENKO, Password.DEFAULT_PASSWORD);
    }

    @Test
    public void test01_AuthorizeUser(){
        String authorizeUri = given().
                contentType("application/json").
                log().all().
                when().
                header("Authorization", "Bearer " + tokenPenko).
                get("/api/twitter/authorizeurl").
                then().statusCode(200).
//                log().all().
                extract().asString();
        System.out.println(authorizeUri);
        driver=new HtmlUnitDriver();
        try {
             URL url=new URL(authorizeUri);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        driver.get(authorizeUri);
        String authenticity_token=driver.findElement(By.xpath("html/body/div[2]/div/form/div/input[1]")).getAttribute("value");

        System.out.println(authenticity_token);
    }

    /*@Test
    public void test01_ShareAdInTwitter(){

        Response response = given().
                contentType("application/json").
                log().all().
                when().
                header("Authorization", "Bearer " + tokenPenko).
                param("pageSize", 12).
                param("pageIndex", 0).
                param("country", "").
                param("pricefrom", "").
                param("searchedUserId", "").
                param("areaId", "").
                param("adQuery", "").
                param("categories", 0,1,2,3,4,6).
                param("order", 0).
                get("/api/feed/get").
                then().statusCode(200).log().all().
                extract().response();  // check that the status code is 200
        GetFeedAdsResponse getFeedAdsResponse = response.getBody().as(GetFeedAdsResponse.class);
        int adId=getFeedAdsResponse.ads[0].id;
        String publicUrl=getFeedAdsResponse.ads[0].getPublicUrl();

        given().
                contentType("application/json; charset=UTF-8").
                header("Authorization", "Bearer " + tokenPenko).
                log().all().
                when().
                queryParam("adid",adId).
                body("Hey, check out this ad test ad, over at @NovaManus! Check it out at "+publicUrl).
                post("/api/twitter/tweetadvertisement").
                then().statusCode(200).  // check that the status code is 200
                log().all();
    }*/



}

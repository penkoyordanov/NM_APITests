package tests;

import Dto.AdDto;
import Dto.Contact;
import Dto.LocationDto;
import com.google.gson.Gson;
import helpers.TestDataFaker;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(value = Parameterized.class)
public class PublishAdTests extends BaseTest {
    private static Response response;
    private static String token = "";
    private Contact contact;
    private LocationDto locationDto;

    private String title;
    private int category;
    private int condition;
    private Integer price;
    private Integer minPrice;
    private String description;
    private boolean allowComments;
    private String[] keywords;

    @Parameterized.Parameters
    public static List<Object[]> adTestData(){
        return Arrays.asList(new Object[][] {
                //Title,category,Condition,Price,MinPrice,AllowComments,Description
                {TestDataFaker.getTitle(),0,0,TestDataFaker.getPriceInt(),0,true,TestDataFaker.getDescription()}, //category: sell, condition: new, price
                {TestDataFaker.getTitle(),1,1,0,0,true,TestDataFaker.getDescription()}, //category: wanted, condition: used, No price
                {TestDataFaker.getTitle(),2,0,0,0,true,TestDataFaker.getDescription()}, //category: swap, condition: new, No price
                {TestDataFaker.getTitle(),3,0,0,0,true,TestDataFaker.getDescription()}, //category: give away, condition: new, No price
                {TestDataFaker.getTitle(),4,0,TestDataFaker.getPriceInt(),0,true,TestDataFaker.getDescription()}, //category: rent, condition: new, price
                {TestDataFaker.getTitle(),5,0,0,0,true,TestDataFaker.getDescription()}, //category: info board, condition: new, No price
                {TestDataFaker.getTitle(),6,0,0,0,true,TestDataFaker.getDescription()}, //category: make an offer, condition: new, without min price
                {TestDataFaker.getTitle(),6,0,0,TestDataFaker.getPriceInt(),true,TestDataFaker.getDescription()} //category: make an offer, condition: new, min price
        });

    }

    public PublishAdTests(String title, int category, int condition, Integer price, Integer minPrice,boolean allowComments,String description) {
        this.title = title;
        this.category=category;
        this.condition=condition;
        this.price=price;
        this.minPrice=minPrice;
        this.allowComments=allowComments;
        this.description=description;
        this.keywords=new String[]{TestDataFaker.getKeyword(),TestDataFaker.getKeyword()};
    }


    @BeforeClass
    public static void setUp() {
        setUpBase();
    }

    @Before
    public void GetUserShortDetailsAndAddress(){
        RestAssured.baseURI = String.format("%s/api/profile/short", baseURL);
        response = given().
                contentType("application/json; charset=UTF-8").
                when().header("Authorization", "Bearer " + token).
                get().
                then().  // check that the content type return from the API is JSON
                extract().response(); // extract the response
        contact=new Contact(response);

        RestAssured.baseURI = String.format("%s/api/user/getuseraddresses", baseURL);
        response = given().
                contentType("application/json; charset=UTF-8").
                when().header("Authorization", "Bearer " + token).
                get().
                then().statusCode(200).  // check that the status code is 200
                contentType(ContentType.JSON).  // check that the content type return from the API is JSON
                extract().response(); // extract the response

        locationDto = new LocationDto(response);
    }


    @Test
    public void publishAdDataDriven() {
       AdDto adDto =new AdDto();
        adDto.setLocationDto(locationDto);
        adDto.setContact(contact);

        adDto.setTitle(this.title);
        adDto.setCategory(this.category);
        adDto.setCondition(this.condition);
        adDto.setPrice(this.price);
        adDto.setMinPrice(this.minPrice);
        adDto.setDescription(description);
        adDto.setAllowComments(this.allowComments);

        adDto.setKeywords(this.keywords);
        Gson gson = new Gson();
        RestAssured.baseURI = String.format("%s/api/adv/publish", baseURL);
        System.out.println("Publish object"+ gson.toJson(adDto));
        response=given().
                contentType("application/json").log().all().
                body(gson.toJson(adDto)).
                when().
                header("Authorization", "Bearer " + token).
                post().
                then().statusCode(200).  // check that the status code is 200
                contentType(ContentType.JSON).  // check that the content type return from the API is JSON
                extract().response(); // extract the response


        //Assertions about entered Advertisement details (Title, Condition,category,Price,Keywords, AllowComment)
        assertEquals("category is not as expected: ",  Integer.toString(adDto.getCategory()),getValueFromJSONResponse(response, "category"));

        if(this.category!=4&&this.category!=5){
            assertEquals("Condition is not as expected: ", Integer.toString(adDto.getCondition()),getValueFromJSONResponse(response, "condition"));
        }else{
            assertNull("Condition is not as expected: ", getValueFromJSONResponse(response, "condition"));
        }

        assertEquals("Title is not as expected: ", title,getValueFromJSONResponse(response, "title"));
        if(this.category==0||this.category==4){
            assertEquals("Price is not as expected: ", Integer.toString(price)+".0",getValueFromJSONResponse(response, "price"));
        }else{
            assertNull("Price is not null: ", getValueFromJSONResponse(response, "price"));
        }

        if(this.minPrice!=0){
            assertEquals("Min price is not as expected: ", Integer.toString(minPrice)+".0",getValueFromJSONResponse(response, "minPrice"));
        }else{
            assertNull("MinPrice is not null: ", getValueFromJSONResponse(response, "minPrice"));
        }

        assertEquals("AllowComments is not as expected: ", String.valueOf(allowComments),getValueFromJSONResponse(response, "allowComments"));
        assertEquals("Description is not as expected: ", description, getValueFromJSONResponse(response, "description"));
        assertEquals("Keyword1 is not as expected: ", keywords[0],getValueFromJSONResponse(response, "keywords[0]"));
        assertEquals("Keyword2 is not as expected: ", keywords[1],getValueFromJSONResponse(response, "keywords[1]"));

        //Assertions about contact person in created advertisement
        assertEquals("Contact firstName is not as expected: ",contact.getFirstName(),getValueFromJSONResponse(response, "contact.firstName"));
        assertEquals("Contact lastName is not as expected: ",contact.getLastName(),getValueFromJSONResponse(response, "contact.lastName"));

        //Assertions about publisher in created advertisement
        assertEquals("Publisher email is not as expected: ",contact.getEmail(),getValueFromJSONResponse(response, "publisher.email"));
        assertEquals("Publisher firstName is not as expected: ",contact.getFirstName(),getValueFromJSONResponse(response, "publisher.firstName"));
        assertEquals("Publisher lastName is not as expected: ",contact.getLastName(),getValueFromJSONResponse(response, "publisher.lastName"));
        assertEquals("Publisher phone is not as expected: ",contact.getPhone(),getValueFromJSONResponse(response, "publisher.phone"));
        assertEquals("Publisher id is not as expected: ",String.valueOf(contact.getId()),getValueFromJSONResponse(response, "publisher.id"));

        //Assertions about point of sale of created advertisement
        assertEquals("PlaceID is not as expected: ", locationDto.getPlaceId(),getValueFromJSONResponse(response, "locationDto.placeId"));
        assertEquals("LocationDto is not as expected: ", locationDto.getAddress(),getValueFromJSONResponse(response, "locationDto.locationDto"));
        assertEquals("CountryCode is not as expected: ", locationDto.getCountryCode(),getValueFromJSONResponse(response, "locationDto.countryCode"));

    }

}

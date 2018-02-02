package tests;

import API.NovamanusAPI;
import API.StripeAPI;
import Dto.BusinessProfileDto;
import Dto.BusinessProfileType;
import Dto.LocationDto;
import Dto.MultimediaDto;
import Enums.Password;
import Enums.UserEmail;
import Requests.BusinessRegisterRequest;
import Requests.FollowRequestDto;
import SQL.SQLService;
import com.github.javafaker.Faker;
import com.stripe.model.Token;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.*;
import org.junit.runners.MethodSorters;

import java.util.Locale;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.core.IsEqual.equalTo;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BusinessControllerTests extends BaseTest {
    private Faker faker = new Faker(Locale.ENGLISH);
    private BusinessRegisterRequest requestBody;
    private String token;
    private String phone;
    private String email;
    private String companyName;
    private final String COMPANYADDRESS = "ul. \"Indzhe voyvoda\", 1309 Sofia, Bulgaria";
    private int vat;
    private int companyNumber = faker.number().numberBetween(1, 999);

    @BeforeClass
    public static void setUp() {
        setUpBase();
    }

    @Before
    public void setUpTestData() {
        UserEmail.LATESTUSER.setUserEmail(SQLService.getLatestUser());
        token = NovamanusAPI.autchenticationShouldReturnToken(UserEmail.LATESTUSER, Password.DEFAULT_PASSWORD);
        requestBody = new BusinessRegisterRequest();

        this.vat = faker.number().numberBetween(10000000, 999999999);
        this.phone = String.valueOf(faker.number().numberBetween(10000000, 999999999));
        this.email = faker.internet().emailAddress();
        this.companyName = faker.company().name() + companyNumber;

        String FOLLOWPLACEID = "ChIJ5RoD1oKGqkARb01ZnqenHd8";
        requestBody.setFollowData(new FollowRequestDto[]{new FollowRequestDto(FOLLOWPLACEID, 1)});
        requestBody.setProfile(new BusinessProfileDto());
        requestBody.getProfile().setName(companyName);
        requestBody.getProfile().setEmail(email);
        requestBody.getProfile().setPhone(phone);
        requestBody.getProfile().setVat(vat);
        String COMPANYADDRESSPLACEID = "ChIJ066YXEmFqkARJpLzPWNmV18";
        String COMPANYCOUNTRYCODE = "bg";
        requestBody.getProfile().setAddress(new LocationDto(COMPANYADDRESS, COMPANYADDRESSPLACEID, COMPANYCOUNTRYCODE));
        requestBody.getProfile().setImage(new MultimediaDto());
        requestBody.getProfile().setBusinessProfileType(new BusinessProfileType());

    }

    @Test
    public void test01_ResponseShouldReturnTrueIfVATRegistered() {

        given().
                contentType("application/json").
                log().all().
                when().
                param("vat", "de 123456789").
                get("/api/business/isVatTaken").
                then().statusCode(200).  // check that the status code is 200
                body(containsString("true")).
                log().all();
    }

    @Test
    public void test02_ResponseShouldReturnFalseIfVATnotRegistered() {

        given().
                contentType("application/json").
                log().all().
                when().
                param("vat", "de 12345693456").
                get("/api/business/isVatTaken").
                then().statusCode(200).  // check that the status code is 200
                body(containsString("false")).
                log().all();
    }

    @Test
    public void test03_VISAPaymentSucceedBusinessProfileRegistered() {
        //Make a payment and get a token
        Token StripeToken = StripeAPI.createToken(4000056655665556L, 12, 2018, 234, "Penko");


        requestBody.getProfile().setStripeToken(StripeToken.getId());
        requestBody.getProfile().setTotalToPay(29);


        int businessID=given().
                contentType("application/json").
                header("Authorization", "Bearer " + token).
                log().all().
                when().
                body(requestBody).
                post("/api/business/RegisterBusiness").
                then().statusCode(200).  // check that the status code is 200
                body("vat", equalTo(String.valueOf(this.vat))).
                body("name", equalTo(companyName)).
                body("phone", equalTo(phone)).
                body("address.address", equalTo(COMPANYADDRESS)).
                body("id", greaterThan(0)).
                log().all().
                extract().path("id");

        given().
                contentType("application/json; charset=UTF-8").
                when().header("Authorization", "Bearer " + token).
                param("profileId", businessID).
                get("/api/business/GetBusinessProfile").
                then().statusCode(200).  // check that the status code is 200
                contentType(ContentType.JSON).  // check that the content type return from the API is JSON
                log().all().
                body("id", equalTo(businessID)).
                body("vat", equalTo(String.valueOf(this.vat))).
                body("name", equalTo(this.companyName)).
                body("email", equalTo(this.email)).
                body("address.address", equalTo(COMPANYADDRESS));
    }

    @Test
    public void test04_MastercardPaymentSucceedBusinessProfileRegistered() {
        //Make a payment and get a token
        Token StripeToken = StripeAPI.createToken(5555555555554444L, 12, 2018, 234, "Penko");


        requestBody.getProfile().setStripeToken(StripeToken.getId());
        requestBody.getProfile().setTotalToPay(29);


        given().
                contentType("application/json").
                header("Authorization", "Bearer " + token).
                log().all().
                when().
                body(requestBody).
                post("/api/business/RegisterBusiness").
                then().statusCode(200).  // check that the status code is 200
                body("vat", equalTo(String.valueOf(this.vat))).
                body("name", equalTo(companyName)).
                body("phone", equalTo(phone)).
                body("address.address", equalTo(COMPANYADDRESS)).
                body("id", greaterThan(0)).
                log().all();
    }

    @Test
    public void test05_AEPaymentSucceedBusinessProfileRegistered() {
        //Make a payment and get a token
        Token StripeToken = StripeAPI.createToken(378282246310005L, 12, 2018, 234, "Penko");


        requestBody.getProfile().setStripeToken(StripeToken.getId());
        requestBody.getProfile().setTotalToPay(29);


        given().
                contentType("application/json").
                header("Authorization", "Bearer " + token).
                log().all().
                when().
                body(requestBody).
                post("/api/business/RegisterBusiness").
                then().statusCode(200).  // check that the status code is 200
                body("vat", equalTo(String.valueOf(this.vat))).
                body("name", equalTo(companyName)).
                body("phone", equalTo(phone)).
                body("address.address", equalTo(COMPANYADDRESS)).
                body("id", greaterThan(0)).
                log().all();
    }

    @Test
    public void test06_CreditCardDeclinedBusinessProfileNotRegisterd() {
        //Make a payment and get a token
        Token StripeToken = StripeAPI.createToken(4000000000000002L, 12, 2018, 234, "Penko");

        requestBody.getProfile().setStripeToken(StripeToken.getId());
        requestBody.getProfile().setTotalToPay(29);


        given().
                contentType("application/json").
                header("Authorization", "Bearer " + token).
                log().all().
                when().
                body(requestBody).
                post("/api/business/RegisterBusiness").
                then().statusCode(200).  // check that the status code is 200
                body("vat", equalTo(String.valueOf(this.vat))).
                body("name", equalTo(companyName)).
                body("phone", equalTo(phone)).
                body("address.address", equalTo(COMPANYADDRESS)).
                body("paymentError", equalTo("Your card was declined.")).
                body("id", equalTo(0)).
                log().all();
    }

    @Test
    public void test07_CreditCardDeclinedWrongCVCBusinessProfileNotRegisterd() {
        //Make a payment and get a token
        Token StripeToken = StripeAPI.createToken(4000000000000127L, 12, 2018, 234, "Penko");

        requestBody.getProfile().setStripeToken(StripeToken.getId());
        requestBody.getProfile().setTotalToPay(29);

        given().
                contentType("application/json").
                header("Authorization", "Bearer " + token).
                log().all().
                when().
                body(requestBody).
                post("/api/business/RegisterBusiness").
                then().statusCode(200).  // check that the status code is 200
                body("vat", equalTo(String.valueOf(vat))).
                body("name", equalTo(companyName)).
                body("phone", equalTo(phone)).
                body("address.address", equalTo(COMPANYADDRESS)).
                body("paymentError", equalTo("Your card's security code is incorrect.")).
                body("id", equalTo(0)).
                log().all();
    }

    @Test
    public void test08_CreditCardDeclinedDueToExpiration_BusinessProfileNotRegisterd() {
        //Make a payment and get a token
        Token StripeToken = StripeAPI.createToken(4000000000000069L, 12, 2018, 234, "Penko");

        requestBody.getProfile().setStripeToken(StripeToken.getId());
        requestBody.getProfile().setTotalToPay(29);

        given().
                contentType("application/json").
                header("Authorization", "Bearer " + token).
                log().all().
                when().
                body(requestBody).
                post("/api/business/RegisterBusiness").
                then().statusCode(200).  // check that the status code is 200
                body("vat", equalTo(String.valueOf(vat))).
                body("name", equalTo(companyName)).
                body("phone", equalTo(phone)).
                body("address.address", equalTo(COMPANYADDRESS)).
                body("paymentError", equalTo("Your card has expired.")).
                body("id", equalTo(0)).
                log().all();
    }

    @Test
    public void test09_CreditCardDeclinedDueToProcessingErr_BusinessProfileNotRegisterd() {
        //Make a payment and get a token
        Token StripeToken = StripeAPI.createToken(4000000000000119L, 12, 2018, 234, "Penko");

        requestBody.getProfile().setStripeToken(StripeToken.getId());
        requestBody.getProfile().setTotalToPay(29);

        given().
                contentType("application/json").
                header("Authorization", "Bearer " + token).
                log().all().
                when().
                body(requestBody).
                post("/api/business/RegisterBusiness").
                then().statusCode(200).  // check that the status code is 200
                body("vat", equalTo(String.valueOf(vat))).
                body("name", equalTo(companyName)).
                body("phone", equalTo(phone)).
                body("address.address", equalTo(COMPANYADDRESS)).
                body("paymentError", equalTo("An error occurred while processing your card. Try again in a little bit.")).
                body("id", equalTo(0)).
                log().all();
    }

    @Test
    public void test10_CreditCardDeclinedDueToFraudelent_BusinessProfileNotRegisterd() {
        //Make a payment and get a token
        Token StripeToken = StripeAPI.createToken(4100000000000019L, 12, 2018, 234, "Penko");

        requestBody.getProfile().setStripeToken(StripeToken.getId());
        requestBody.getProfile().setTotalToPay(29);

        given().
                contentType("application/json").
                header("Authorization", "Bearer " + token).
                log().all().
                when().
                body(requestBody).
                post("/api/business/RegisterBusiness").
                then().statusCode(200).  // check that the status code is 200
                body("vat", equalTo(String.valueOf(vat))).
                body("name", equalTo(companyName)).
                body("phone", equalTo(phone)).
                body("address.address", equalTo(COMPANYADDRESS)).
                body("paymentError", equalTo("Your card was declined.")).
                body("id", equalTo(0)).
                log().all();
    }

    @Test
    public void test12_IsEmailAvailableShouldReturnFalseForAlreadyUsedEmail() {

        Response response = given().
                contentType("application/json").
                queryParam("email", "lobon@go2usa.info").
                log().all().
                when().
                get("/api/business/isEmailAvailable").
                then().  // check that the status code is 200
                log().all().extract().response();
        Assert.assertEquals(response.getBody().asString(), "false");
    }

    @Test
    public void test13_IsEmailAvailableShouldReturnTrueForNotUsedEmail() {

        Response response = given().
                contentType("application/json").
                queryParam("email", email).
                log().all().
                when().
                get("/api/business/isEmailAvailable").
                then().  // check that the status code is 200
                log().all().extract().response();
        Assert.assertEquals(response.getBody().asString(), "true");
    }
}

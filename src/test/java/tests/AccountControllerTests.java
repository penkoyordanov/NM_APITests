package tests;

import API.NovamanusAPI;
import Dto.NewPassDto;
import Dto.RequestDto;
import Dto.SignInDto;
import Enums.Password;
import Enums.UserEmail;
import Requests.RegisterRequest;
import SQL.SQLService;
import com.github.fge.jsonschema.cfg.ValidationConfiguration;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.github.javafaker.Faker;
import helpers.RepeatRule;
import io.restassured.http.ContentType;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.junit.*;
import org.junit.runners.MethodSorters;

import java.util.Locale;
import java.util.Map;

import static com.github.fge.jsonschema.SchemaVersion.DRAFTV3;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static io.restassured.module.jsv.JsonSchemaValidatorSettings.settings;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AccountControllerTests extends BaseTest {


    private static Map<String, String> userDetails = SQLService.getUserDetails("test@novamanus.com");
    private static String forgottenPasswordEmail = "obndheuc@sharklasers.com";
    private String iivanovEmail = "test@novamanus.com";
    private String iivanovpass = "123456";
    private static String requestUID = "";
    private static String tokenIvan = "";
    static Faker faker = new Faker(Locale.ENGLISH);
    private String quotes = "\"";
    @Rule
    public RepeatRule repeatRule = new RepeatRule();

    @BeforeClass
    public static void setUp() {
        setUpBase();
        tokenIvan = NovamanusAPI.autchenticationShouldReturnToken(UserEmail.TESTUSR, Password.TESTUSR_PASSWORD);

    }

    @Test
    public void test01_ValidSignInShouldReturnResponseWithToken() {
        SignInDto signInReq = new SignInDto();
        signInReq.email = iivanovEmail;
        signInReq.password = iivanovpass;

        JsonSchemaValidator.settings = settings().with().jsonSchemaFactory(
                JsonSchemaFactory.newBuilder().setValidationConfiguration(ValidationConfiguration.newBuilder().setDefaultVersion(DRAFTV3).freeze()).freeze()).
                and().with().checkedValidation(false);

        given().
                contentType("application/json; charset=UTF-8").
                log().all().
                body(signInReq).
                when().
                post("/api/account/login").
                then().
                assertThat().body(matchesJsonSchemaInClasspath("login-schema.json").using(settings().with().checkedValidation(false))).
                log().all().
                statusCode(200).  // check that the status code is 200
                contentType(ContentType.JSON).  // check that the content type return from the API is JSON
                body(containsString("token"));
    }

    @Test
    public void test02_TokenShouldContainValidClaims() {
        SignInDto signInReq = new SignInDto();
        signInReq.email = iivanovEmail;
        signInReq.password = iivanovpass;


        Response response = given().
                contentType("application/json; charset=UTF-8").
                log().all().
                body(signInReq).
                when().
                post("/api/account/login").
                then().
                log().all().
                statusCode(200).  // check that the status code is 200
                contentType(ContentType.JSON).  // check that the content type return from the API is JSON
                extract().response();

        String jwt = response.jsonPath().getString("token.token");
        Map<String, Object> decodedJwt = NovamanusAPI.decodeJwt(jwt);
        Assert.assertEquals("UserEmail is not contained in jwt", userDetails.get("userName"), decodedJwt.get("unique_name"));
        Assert.assertEquals("ID not contained in jwt", Integer.parseInt(userDetails.get("userID")), decodedJwt.get("ID"));
    }

    @Test
    public void test04_TryingToSignInWithInvalidEmailShouldReturnWarningMessage() {
        SignInDto signInReq = new SignInDto();
        signInReq.email = "test";
        signInReq.password = "1234";


        given().
                contentType("application/json; charset=UTF-8").log().all().
                body(signInReq).
                when().
                post("/api/account/login").
                then().
                statusCode(400).  // check that the status code is 400
                body(containsString("Email is not valid email address!"));
    }

    @Test
    public void test04_TryingToSignInWithWrongEmailShouldReturnStatusCode204() {
        SignInDto signInReq = new SignInDto();
        signInReq.email = "test@abv.bg";
        signInReq.password = "1234";


        given().
                contentType("application/json; charset=UTF-8").log().all().
                body(signInReq).
                when().
                post("/api/account/login").
                then().
                statusCode(204);  // check that the status code is 204
    }


    @Test
    public void test06_SendForgotPasswordEmail() {

        given().
                contentType("application/json; charset=UTF-8").
                body(quotes + forgottenPasswordEmail + quotes).log().all().
                post("/api/account/sendForgotPasswordEmail").
                then().
                statusCode(200);  // check that the status code is 200
    }

    @Test
    public void test07_getNewPasswordRequest() {

        given().
                contentType("application/json; charset=UTF-8").
                body(quotes + forgottenPasswordEmail + quotes).log().all().
                post("/api/account/sendForgotPasswordEmail").
                then().
                statusCode(200);  // check that the status code is 200

        //Get UID generated by the sendForgotPasswordCall
        requestUID = SQLService.getFPUIDByEmail(forgottenPasswordEmail);

        given().
                contentType("application/json; charset=UTF-8").
                body(quotes + requestUID + quotes).
                log().all().
                post("/api/account/getPasswordRequest").
                then().
                statusCode(200).  // check that the status code is 200
                body("isValid", is(true));

    }

    @Test
    public void test08_setNewPasswordByPasswordRequestID() {
        String newPassword = "1234562345";

        NewPassDto body = new NewPassDto();
        body.uniqueId = requestUID;
        body.password = newPassword;

        given().
                contentType("application/json; charset=UTF-8").
                body(body).
                log().all().
                post("/api/account/setNewPassword").
                then().
                statusCode(200);  // check that the status code is 200

        SignInDto signInReq = new SignInDto();
        signInReq.email = forgottenPasswordEmail;
        signInReq.password = newPassword;

        given().
                contentType("application/json; charset=UTF-8").
                body(signInReq).
                post("/api/account/login").
                then().
                statusCode(200).
                body(containsString("token"));

    }

    @Test
//    @Repeat(1000)
    public void test09_registerUser() {

        String firstName = faker.name().firstName().replaceAll("[']", "");
        String lastName = faker.name().lastName().replaceAll("[']", "");
        String email = firstName.toLowerCase() + "." + lastName.toLowerCase() + "@mailinator.com";

        RequestDto requestDto = new RequestDto(email, firstName + " " + lastName);
        String requestGuid = given().
                contentType("application/json; charset=UTF-8").
                body(requestDto).log().all().
                when().
                post("/api/account/request").
                then().statusCode(200).log().all().  // check that the status code is 200
                extract().response().asString();
        requestGuid = requestGuid.replace("\"", "");



        String verificationCode = SQLService.getVerificationCode(requestGuid);

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.user.email = email;
        registerRequest.user.password = "123456234";
        registerRequest.user.contact.firstName = firstName;
        registerRequest.user.contact.lastName = lastName;
        registerRequest.user.address.address = "ul. \"Indzhe voyvoda\" 7, 1309 Sofia, Bulgaria";
        registerRequest.user.address.placeId = "ChIJfVmbqk6FqkARgtm9_snqvBM";
        registerRequest.user.address.countryCode = "bg";
        registerRequest.requestGuid = requestGuid;
        registerRequest.verificationCode = verificationCode;

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        given().
                contentType("application/json; charset=UTF-8").
                when().log().all().
                body(registerRequest).
                post("/api/account/register").
                then().statusCode(200).log().all().  // check that the status code is 200
                body(containsString("true")).log().all();


        //Assert user is able to login
        SignInDto signInReq = new SignInDto();
        signInReq.email = email;
        signInReq.password = "123456234";

        tokenIvan = given().
                contentType("application/json; charset=UTF-8").
                body(signInReq).
                post("/api/account/login").
                then().
                statusCode(200).
                body(containsString("token")).
                extract().path("token.token");

        given().
                contentType("application/json; charset=UTF-8").
                header("Authorization", "Bearer " + tokenIvan).
                get("/api/profile/short").
                then().
                log().all().
                statusCode(200).
                body("email", equalTo(email)).
                body("firstName", equalTo(registerRequest.user.contact.getFirstName())).
                body("lastName", equalTo(registerRequest.user.contact.getLastName()));
    }

    public static void main(String[] args) {
        for (int i = 0; i < 1000; i++) {
            String firstName = faker.name().firstName().replaceAll("[']", "");
            String lastName = faker.name().lastName().replaceAll("[']", "");
            String email = firstName.toLowerCase() + "." + lastName.toLowerCase() + "@test4.com";
            System.out.println(email);
        }
    }


}

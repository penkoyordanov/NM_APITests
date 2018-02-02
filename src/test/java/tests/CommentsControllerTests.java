package tests;

import API.NovamanusAPI;
import Enums.Password;
import Enums.UserEmail;
import Requests.CommentRequest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.*;
import org.junit.runners.MethodSorters;

import java.util.Date;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static io.restassured.module.jsv.JsonSchemaValidatorSettings.settings;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.core.IsNot.not;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CommentsControllerTests extends BaseTest {
    private static int createdCommentId;
    private static String commentText;
    private static int advId = 191;
    private static String tokenIvan;

    @BeforeClass
    public static void setUp() {
        setUpBase();
        tokenIvan = NovamanusAPI.autchenticationShouldReturnToken(UserEmail.TESTUSR, Password.TESTUSR_PASSWORD);
    }

    @Test
    public void test01_CreateCommentOnAdShouldReturnNewComment() {
        commentText = "TestComment" + new Date();


        CommentRequest body = new CommentRequest();
        body.advertisementId = advId;
        body.commentText = commentText;

        Response response = given().
                contentType("application/json; charset=UTF-8").
                header("Authorization", "Bearer " + tokenIvan).
                body(body).
                when().
                post("/api/advcomment").
                then().statusCode(200).  // check that the status code is 200
                contentType(ContentType.JSON).  // check that the content type return from the API is JSON
                assertThat().body(matchesJsonSchemaInClasspath("CreateCommentResponseSchema.json").using(settings().with().checkedValidation(false))).
                log().all().extract().response();

        createdCommentId = response.jsonPath().getInt("id");
        Assert.assertEquals("Posted comment not returned in the response", getValueFromJSONResponse(response, "text"), body.commentText);
    }

    @Test
    public void test02_GetCommentsForAdShouldContainCreatedComment() {


        given().
                log().all().
                contentType("application/json; charset=UTF-8").
                header("Authorization", "Bearer " + tokenIvan).
                when().
                get("api/advcomment/{0}", advId).
                then().statusCode(200).  // check that the status code is 200
                contentType(ContentType.JSON).  // check that the content type return from the API is JSON
                log().all().
                body(containsString(commentText));
    }


    @Test
    public void test03_DeleteComment() {


        given().
                log().all().
                contentType("application/json; charset=UTF-8").
                header("Authorization", "Bearer " + tokenIvan).
                when().
                delete("api/advcomment/{0}", createdCommentId).
                then().statusCode(200);  // check that the status code is 200
    }

    @Test
    public void test04_GetCommentsForAdShouldNotContainDeletedComment() {


        given().
                log().all().
                contentType("application/json; charset=UTF-8").
                header("Authorization", "Bearer " + tokenIvan).
                when().
                get("api/advcomment/{0}", advId).
                then().statusCode(200).  // check that the status code is 200
                contentType(ContentType.JSON).  // check that the content type return from the API is JSON
                log().all().
                body(not(containsString(commentText)));
//                body("items.text",not(equalTo(commentText)));
    }

    @Ignore
    @Test
    public void test05_CreateCommentOnAdWithUploadImageShouldReturnNewComment() {

        long milis = System.currentTimeMillis() % 1000;
        CommentRequest body = new CommentRequest();
        body.advertisementId = advId;
        body.commentText = "TestComment" + milis;
        Map files = NovamanusAPI.uploadDocumentShouldReturnMap("iphone6s_p5.JPG", tokenIvan);
        body.setFiles(files);

        Response response = given().
                log().all().
                contentType("application/json; charset=UTF-8").
                header("Authorization", "Bearer " + tokenIvan).
                body(body).
                when().
                post("/api/advcomment").
                then().statusCode(200).  // check that the status code is 200
                contentType(ContentType.JSON).  // check that the content type return from the API is JSON
                assertThat().body(matchesJsonSchemaInClasspath("CommentWIthUploadResponseSchema.json").using(settings().with().checkedValidation(false))).
                log().all().extract().response();

        Assert.assertEquals("Posted comment not returned in the response", getValueFromJSONResponse(response, "text"), body.commentText);
        Assert.assertEquals("Filename not returned in the response", getValueFromJSONResponse(response, "files[0].name"), files.get("name"));
        Assert.assertEquals("File url comment not returned in the response", getValueFromJSONResponse(response, "files[0].url"), files.get("url"));
    }
}

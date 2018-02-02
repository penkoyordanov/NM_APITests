package tests;

import API.NovamanusAPI;
import Enums.Password;
import Enums.UserEmail;
import Responses.GetConversationsResponse;
import SQL.SQLService;
import io.restassured.response.Response;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ConversationControllerTests extends BaseTest {
    private static String tokenIvan;
    private static int conversationId;

    @BeforeClass
    public static void setUp() {

        setUpBase();
        tokenIvan = NovamanusAPI.autchenticationShouldReturnToken(UserEmail.TESTUSR, Password.TESTUSR_PASSWORD);
    }

    @Test
    public void test02_GetSingleConversation() {
        GetConversationsResponse conversationsResponse = given().
                contentType("application/json").
                log().all().
                when().
                header("Authorization", "Bearer " + tokenIvan).
                queryParam("filterTerm", "").
                queryParam("pageIndex", 0).
                queryParam("pageSize", 20).
                get("/api/conversation/get").as(GetConversationsResponse.class);
        Random r = new Random();
        int randomNum = r.nextInt(conversationsResponse.items.length);
        conversationId = conversationsResponse.items[randomNum].id;

        GetConversationsResponse response = given().
                contentType("application/json").
                log().all().
                when().
                header("Authorization", "Bearer " + tokenIvan).
                queryParam("filterTerm", "").
                queryParam("pageIndex", 0).
                queryParam("pageSize", 20).
                get("/api/conversation/get").as(GetConversationsResponse.class);


        Response conversations = given().
                contentType("application/json").
                log().all().
                when().
                header("Authorization", "Bearer " + tokenIvan).
                queryParam("id", conversationId).
                queryParam("dateRef", "").
                queryParam("takeCount", 10).
                get("/api/conversation/getHistory").
                then().
                statusCode(200).
                extract().response();
        ArrayList<HashMap<String, Object>> map = conversations.jsonPath().get();

        for (int i = 0; i < map.size(); i++) {
            Assert.assertThat(map.get(i).get("text"), equalTo(SQLService.getMessageDetails((Integer) map.get(i).get("id")).get("Text")));
        }

    }

    @Test
    public void test03_SetConversationAsSeen() {
        given().
                contentType("application/json").
                log().all().
                when().
                header("Authorization", "Bearer " + tokenIvan).
                body(conversationId).
                post("/api/conversation/setAsSeen").
                then().log().all().
                statusCode(200);
    }
}

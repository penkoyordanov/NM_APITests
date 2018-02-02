package tests;

import API.NovamanusAPI;
import Dto.Invite;
import Enums.Password;
import Enums.UserEmail;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static io.restassured.RestAssured.given;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class InviteControllerTests extends BaseTest {
    static String receiver;
    static String tokenIvan;

    @BeforeClass
    public static void setUp() {
        setUpBase();
        tokenIvan = NovamanusAPI.autchenticationShouldReturnToken(UserEmail.TESTUSR, Password.TESTUSR_PASSWORD);
    }

    @Test
    public void test01_InviteFriend(){

        Invite body=new Invite();
        receiver="haralampi"+System.currentTimeMillis() % 1000+"@abv.bg";
        body.setReceivers(receiver);

        given().
                contentType("application/json; charset=UTF-8").
                header("Authorization", "Bearer " + tokenIvan).
                body(body).log().all().
                when().
                post("/api/invite/email").
                then().statusCode(200).log().all();  // check that the status code is 200

    }


}

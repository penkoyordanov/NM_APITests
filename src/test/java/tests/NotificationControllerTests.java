package tests;

import API.NovamanusAPI;
import Enums.Password;
import Enums.UserEmail;
import Responses.NotificationsGetResponse;
import io.restassured.response.Response;
import org.junit.*;
import org.junit.runners.MethodSorters;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.core.IsEqual.equalTo;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class NotificationControllerTests extends BaseTest {
    private static String tokenLori;

    @BeforeClass
    public static void setUp() {
        setUpBase();
        tokenLori = NovamanusAPI.autchenticationShouldReturnToken(UserEmail.LORISCOTT, Password.DEFAULT_PASSWORD);
    }

    @Ignore
    @Test
    public void test01_GetLastFiveNotifications() {
        Response response =
                given().
                        contentType("application/json").log().all().
                        param("notificationFilter", 0).
                        param("olderThan", "").
                        param("takeCount", 5).
                        when().
                        header("Authorization", "Bearer " + tokenLori).
                        get("/api/notification/get").
                        then().
                        statusCode(200).
                        log().all().
//                        assertThat().body(matchesJsonSchemaInClasspath("NotificationsResponseSchema.json").using(settings().with().checkedValidation(false))).
                        extract().response();
        NotificationsGetResponse notificationsGetResponse=response.as(NotificationsGetResponse.class);

        int totalViewed=0;
        for(int i=0;i<notificationsGetResponse.items.length;i++){
            if(notificationsGetResponse.items[i].viewDate==null)
                totalViewed++;
        }
        Assert.assertThat(notificationsGetResponse.items.length,greaterThan(1));
        Assert.assertThat(notificationsGetResponse.totalCount,equalTo(totalViewed));

    }

    @Test
    public void test02_SetNotificationsAsViewed() {
        given().
                contentType("application/json").log().all().
                when().
                header("Authorization", "Bearer " + tokenLori).
                post("/api/notification/setAllAsViewed").
                then().
                statusCode(200);

        Response response =
                given().
                        contentType("application/json").log().all().
                        param("notificationFilter", 0).
                        param("olderThan", "").
                        param("takeCount", 5).
                        when().
                        header("Authorization", "Bearer " + tokenLori).
                        get("/api/notification/get").
                        then().
                        statusCode(200).
                        log().all().
                        extract().response();
        NotificationsGetResponse notificationsGetResponse = response.as(NotificationsGetResponse.class);

        for (int i = 0; i < notificationsGetResponse.items.length; i++) {
            Assert.assertNotNull(notificationsGetResponse.items[i].viewDate);
        }
    }
    @Test
    public void test03_SetNotificationsAsRead() {
        int notificationID=0;
        Response response =
                given().
                        contentType("application/json").log().all().
                        param("notificationFilter", 0).
                        param("olderThan", "").
                        param("takeCount", 5).
                        when().
                        header("Authorization", "Bearer " + tokenLori).
                        get("/api/notification/get").
                        then().
                        statusCode(200).
                        log().all().
                        extract().response();
        NotificationsGetResponse notificationsGetResponse = response.as(NotificationsGetResponse.class);
        for (int i = 0; i < notificationsGetResponse.items.length; i++) {
            if(notificationsGetResponse.items[i].readDate==null)
                notificationID=notificationsGetResponse.items[i].id;
        }
        //Mark notification as read
        given().
                contentType("application/json").log().all().
                when().
                header("Authorization", "Bearer " + tokenLori).
                body(notificationID).
                post("/api/notification/setAsRead").
                then().
                statusCode(200);

        response =
                given().
                        contentType("application/json").log().all().
                        param("notificationFilter", 0).
                        param("olderThan", "").
                        param("takeCount", 5).
                        when().
                        header("Authorization", "Bearer " + tokenLori).
                        get("/api/notification/get").
                        then().
                        statusCode(200).
                        log().all().
                        extract().response();
        notificationsGetResponse=response.as(NotificationsGetResponse.class);

        for (int i=0;i<notificationsGetResponse.items.length;i++){
            if(notificationsGetResponse.items[i].id==notificationID)
                Assert.assertNotNull(notificationsGetResponse.items[i].readDate);
        }


    }
}

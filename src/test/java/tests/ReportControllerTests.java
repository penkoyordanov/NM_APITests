package tests;

import API.NovamanusAPI;
import Dto.ReportDto;
import Enums.Password;
import Enums.UserEmail;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static io.restassured.RestAssured.given;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ReportControllerTests extends BaseTest {
    private static String token = "";
    private static final String USERNAME2REPORT="celinegrant620";
    private static final int AD2REPORT=284;

    @BeforeClass
    public static void setUp() {
        setUpBase();
        token = NovamanusAPI.autchenticationShouldReturnToken(UserEmail.LORISCOTT, Password.DEFAULT_PASSWORD);
    }

    @Test
    public void test01_ReportUser(){
        given().
                contentType("application/json").
                log().all().
                when().
                header("Authorization", "Bearer " + token).
                post("/api//profile/report/{username}",USERNAME2REPORT).
                then().statusCode(200).  // check that the status code is 200
                log().all();
    }

    @Test
    public void test02_ReportAdvertisement(){
        ReportDto body=new ReportDto();
        body.adid=AD2REPORT;
        body.reportedadreason=0;
        given().
                contentType("application/json").
                log().all().
                when().
                header("Authorization", "Bearer " + token).
                body(body).
                post("/api/report/reportad").
                then().statusCode(200).  // check that the status code is 200
                log().all();
    }


}

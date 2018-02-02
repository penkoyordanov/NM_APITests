package API;

import Dto.AdMultimediaDto;
import Dto.Contact;
import Dto.LocationDto;
import Dto.SignInDto;
import Enums.Password;
import Enums.UserEmail;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.config.MultiPartConfig;
import io.restassured.response.Response;
import org.apache.commons.codec.binary.Base64;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.config;
import static io.restassured.RestAssured.given;

public class NovamanusAPI {
    private static AdMultimediaDto obj=null;

    public static String autchenticationShouldReturnToken(UserEmail email, Password password) {
        SignInDto signInReq = new SignInDto();
        signInReq.email = email.getUserEmail();
        signInReq.password = password.getPassword();

        Response response =
                given().
                        contentType("application/json; charset=UTF-8").
                        body(signInReq).
                        post("/api/account/login").
                        then().
                        extract().response();
        return response.getBody().jsonPath().getString("token.token");

    }

    public static AdMultimediaDto uploadAdMultimedia(String fileName, String token) {
        String response = given().
                config(config().multiPartConfig(MultiPartConfig.multiPartConfig().defaultBoundary("abc"))).
                multiPart("files", new File(fileName)).
                when().header("Authorization", "Bearer " + token).
                post("/api/multimedia/upload").asString();
        response=response.replaceAll("[\\[\\]]","");
        ObjectMapper mapper = new ObjectMapper();
        try {
            obj = mapper.readValue(response, AdMultimediaDto.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return obj;
    }

    public static Map<String, String> uploadDocumentShouldReturnMap(String fileName, String token) {
        Map<String, String> uploadResponse = new HashMap<>();

        Response response =
                given().
                        config(config().multiPartConfig(MultiPartConfig.multiPartConfig().defaultBoundary("abc"))).
                        multiPart("files", new File(fileName)).
                        when().header("Authorization", "Bearer " + token).
                        post("/api/multimedia/upload/document").
                        then().
                        statusCode(200).extract().response();
        uploadResponse.put("originalName", response.getBody().jsonPath().getString("originalName").replaceAll("\\[", "").replaceAll("\\]", ""));
        uploadResponse.put("name", response.getBody().jsonPath().getString("name").replaceAll("\\[", "").replaceAll("\\]", ""));
        uploadResponse.put("url", response.getBody().jsonPath().getString("url").replaceAll("\\[", "").replaceAll("\\]", ""));
        return uploadResponse;
    }

    public static Map<String, Object> decodeJwt(String jwt) {
        String[] segments = jwt.split("\\.");
        String base64String = segments[1];
        byte[] valueDecoded = Base64.decodeBase64(base64String);
        String token = new String(valueDecoded);

        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = new HashMap<>();
        try {
            map = mapper.readValue(token, Map.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    public static Contact GetContactDetailsByToken(String token) {
        Response response = given().
                contentType("application/json; charset=UTF-8").
                when().header("Authorization", "Bearer " + token).
                get("/api/profile/short").
                then().  // check that the content type return from the API is JSON
                extract().response(); // extract the response
        return new Contact(response);
    }


    public static void main(String[] args) {
    }

    public static LocationDto GetContactAddress(String token) {
        Response response = given().
                contentType("application/json; charset=UTF-8").
                when().header("Authorization", "Bearer " + token).
                get("/api/user/getdefaultaddress").
                then().
                extract().response(); // extract the response

        return new LocationDto(response);
    }

}

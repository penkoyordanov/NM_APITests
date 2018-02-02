package tests;

import com.github.fge.jsonschema.cfg.ValidationConfiguration;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import io.restassured.RestAssured;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;

import static com.github.fge.jsonschema.SchemaVersion.DRAFTV3;
import static io.restassured.module.jsv.JsonSchemaValidatorSettings.settings;

public class BaseTest {
    static String baseURL = "https://novamanus-api-test.azurewebsites.net";
    protected static Response response;
    protected String quotes = "\"";


    protected static void setUpBase() {

        // This will set base URL for all the tests
        RestAssured.baseURI = baseURL;

        // We want all the details for failed tests
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        JsonSchemaValidator.settings = settings().with().jsonSchemaFactory(
                JsonSchemaFactory.newBuilder().setValidationConfiguration(ValidationConfiguration.newBuilder().setDefaultVersion(DRAFTV3).freeze()).freeze()).
                and().with().checkedValidation(false);

    }

    String getValueFromJSONResponse(Response response, String path) {
        return response.getBody().jsonPath().getString(path);

    }

}

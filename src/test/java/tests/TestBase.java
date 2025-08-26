package tests;


import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;


/** Base class for API tests against Restful-Booker */
public class TestBase {

    @BeforeAll
    static void setUp() {
        RestAssured.baseURI = System.getProperty(
                "baseUrl", "https://restful-booker.herokuapp.com"
        );
    }


}
package specs;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static helpers.CustomAllureListener.withCustomTemplates;
import static io.restassured.RestAssured.with;
import static io.restassured.filter.log.LogDetail.ALL;
import static io.restassured.http.ContentType.JSON;

public class RequestResponseSpecs {

    public static RequestSpecification registerAndLoginRequestSpec = with()
            .filter(withCustomTemplates())
            .contentType(JSON)
            .log().all();


    public static ResponseSpecification loginResponseSpec200 = new ResponseSpecBuilder()
            .expectStatusCode(200)
            .log(ALL)
            .build();

    public static ResponseSpecification responseSpec201 = new ResponseSpecBuilder()
           .expectStatusCode(201)
           .log(ALL)
           .build();
}

package specs;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static helpers.CustomAllureListener.withCustomTemplates;

/**
 * Shared Request/Response specifications (Vanilla style) for Restful-Booker.
 */
public class RequestResponseSpecs {

    /** JSON request spec with Allure templates and verbose logging. */
    public static RequestSpecification registerAndLoginRequestSpec = new RequestSpecBuilder()
            .setContentType(ContentType.JSON)
            .log(LogDetail.METHOD)
            .log(LogDetail.URI)
            .log(LogDetail.BODY)
            .addFilter(withCustomTemplates())
            .build();

    /** 200 OK */
    public static ResponseSpecification responseSpec200 = new ResponseSpecBuilder()
            .expectStatusCode(200)
            .log(LogDetail.ALL)
            .build();

    /** 201 Created (ping/delete) */
    public static ResponseSpecification responseSpec201 = new ResponseSpecBuilder()
            .expectStatusCode(201)
            .log(LogDetail.ALL)
            .build();

    /** 204 No Content (kept for compatibility; not used by Restful-Booker) */
    public static ResponseSpecification responseSpec204 = new ResponseSpecBuilder()
            .expectStatusCode(204)
            .log(LogDetail.ALL)
            .build();

    /** 400 Bad Request */
    public static ResponseSpecification responseSpec400 = new ResponseSpecBuilder()
            .expectStatusCode(400)
            .log(LogDetail.ALL)
            .build();

    /** 401 Unauthorized */
    public static ResponseSpecification unauthorizedResponseSpec401 = new ResponseSpecBuilder()
            .expectStatusCode(401)
            .log(LogDetail.ALL)
            .build();

    /** 403 Forbidden */
    public static ResponseSpecification forbiddenResponseSpec403 = new ResponseSpecBuilder()
            .expectStatusCode(403)
            .log(LogDetail.ALL)
            .build();

    /** 404 Not Found */
    public static ResponseSpecification responseSpec404 = new ResponseSpecBuilder()
            .expectStatusCode(404)
            .log(LogDetail.ALL)
            .build();
}

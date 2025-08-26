package tests;

import api.ApiSteps;
import io.qameta.allure.Owner;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;

/** Health check for /ping endpoint (expects 201). */

@DisplayName("GET /ping returns 201 (service alive)")
@Owner("Anosov Aleksandr")
@Tag("Booking")
public class HealthCheckTest extends TestBase {

    @Test

    @Severity(SeverityLevel.BLOCKER)
    void pingReturns201() {
        int status = new ApiSteps().ping().extract().statusCode();
        org.assertj.core.api.Assertions.assertThat(status).isEqualTo(HttpStatus.SC_CREATED);
    }
}


package tests;

import api.ApiSteps;
import io.qameta.allure.Owner;
import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/** Auth tests for Restful-Booker. */
@Tag("Auth")
@DisplayName("Tests for /auth token")
@Owner("Anosov Aleksandr")
@Feature("Authorization")
public class UserLoginTest extends TestBase {

    @Test
    @DisplayName("Create auth token with default credentials")
    @Owner("Anosov Aleksandr")
    @Description("POST /auth with admin/password123 returns a non-empty token.")
    @Severity(SeverityLevel.CRITICAL)
    void userAuthorizationTest() {
        ApiSteps.registerUser();
        assertThat(ApiSteps.getToken()).isNotBlank();
    }
}

package helpers;

import api.ApiSteps;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class LoginExtension implements BeforeEachCallback {
    @Override
    public void beforeEach(ExtensionContext extensionContext) {
        ApiSteps.registerUser(); // Register the user first
        ApiSteps.setCookiesInBrowser(ApiSteps.getToken()); // Then get token and set cookies
    }
}
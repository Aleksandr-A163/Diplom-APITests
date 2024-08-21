package helpers;

import api.AuthApi;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class LoginExtension implements BeforeEachCallback {
    @Override
    public void beforeEach(ExtensionContext extensionContext) {
        AuthApi.registerUser(); // Register the user first
        AuthApi.setCookiesInBrowser(AuthApi.getToken()); // Then get token and set cookies
    }
}
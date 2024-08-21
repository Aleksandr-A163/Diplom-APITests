package pages;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;

public class ProfilePage {
    public final SelenideElement deleteButton = $("#delete-record-undefined"),
                confirmButton = $("#closeSmallModal-ok"),
                bookRow = $(".rt-tbody").$(".rt-tr-group"),
                noRowsFoundLabel = $(".profile-wrapper").$(".rt-noData");

    @Step("Removing banners")
    public ProfilePage removeBanner() {
        executeJavaScript("$('#fixedban').remove()");
        executeJavaScript("$('footer').remove()");
        return this;
    }

    @Step("Open profile page")
    public ProfilePage openProfilePage() {
        open("/profile");
        bookRow.shouldHave(text("Eloquent JavaScript, Second Edition"));
        return this;
    }

    @Step("Check the book is in the profile")
    public ProfilePage checkBookIsInProfile(String isbn) {
        $("a[href*='/profile?book=" + isbn + "']").should(exist);
        return this;
    }

    @Step("Delete the book from the profile")
    public ProfilePage deleteBook() {
        deleteButton.click();
        confirmButton.click();
        Selenide.confirm();
        return this;
    }

    @Step("Check the book list is empty")
    public ProfilePage checkBooksListIsEmpty() {
        noRowsFoundLabel.shouldHave(text("No rows found"));
        return this;
    }
}
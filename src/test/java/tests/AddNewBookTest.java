package tests;
import api.ApiSteps;
import helpers.WithLogin;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pages.ProfilePage;


@Tag("Book")
@DisplayName("Tests for adding a book in the Store Application")
@Owner("Anosov Aleksandr")
@Story("Book add actions")
@Feature("Book Store")
public class AddNewBookTest extends TestBase {
    final ProfilePage profilePage = new ProfilePage();
    final ApiSteps addNewBook = new ApiSteps();

    @Test
    @DisplayName("Add a random book to user profile")
    @Owner("Anosov Aleksandr")
    @WithLogin
    public void addBookToCustomerProfileBooksListTest() {
        addNewBook.addRandomBook();
        String isbn = addNewBook.getIsbn();  // Получаем значение ISBN
        profilePage.openProfilePage()
            .removeBanner()
            .checkBookIsInProfile(isbn);  // Передаем ISBN для проверки
    }

    @Test
    @DisplayName("Attempt to add a book with invalid token")
    @Owner("Anosov Aleksandr")
    @WithLogin
    public void addBookToCustomerProfileWithInvalidTokenTest() {
        addNewBook.addBookWithInvalidToken();  // Вызов метода для добавления книги с некорректным токеном
    }
}
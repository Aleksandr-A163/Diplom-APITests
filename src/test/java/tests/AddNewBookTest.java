package tests;
import helpers.WithLogin;
import io.qameta.allure.Owner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pages.ProfilePage;


@Tag("API")
@DisplayName("Tests for Book Store Application")
@Owner("Anosov Aleksandr")
public class AddNewBookTest extends TestBase {
    final ProfilePage profilePage = new ProfilePage();
    final AddNewBook addNewBook = new AddNewBook();

    @Test
    @DisplayName("Add a random book to user profile")
    @Owner("Anosov Aleksandr")
    @WithLogin
    public void addBookToCustomerProfileBooksListTest() {
        addNewBook.addListOfBook();
        String isbn = addNewBook.getIsbn();  // Получаем значение ISBN
        profilePage.openProfilePage()
            .removeBanner()
            .checkBookIsInProfile(isbn);  // Передаем ISBN для проверки
    }
}
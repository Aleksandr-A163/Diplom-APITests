package tests;
import helpers.WithLogin;
import io.qameta.allure.Owner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pages.ProfilePage;


@Tag("API")
@DisplayName("Tests for Book Store Application")
public class DeleteBookTest extends TestBase {

    final ProfilePage profilePage = new ProfilePage();
    final AddNewBookTest addNewBook = new AddNewBookTest ();

    @Test
    @DisplayName("Delete a book from user profile")
    @Owner("Anosov Aleksandr")
    @WithLogin
    void deleteBookFromProfileBooksListTest() {
        addNewBook.addListOfBook();
        profilePage.openProfilePage()
            .removeBanner()
            .deleteBook()
            .checkBooksListIsEmpty();
    }
}
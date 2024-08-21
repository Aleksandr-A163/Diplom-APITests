package tests;
import helpers.WithLogin;
import io.qameta.allure.Owner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pages.ProfilePage;


@Tag("book")
@DisplayName("Tests for Book Store Application")
public class BooksStoreTests extends TestBase {

    final ProfilePage profilePage = new ProfilePage();
    final AddNewBook addNewBook = new AddNewBook ();

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
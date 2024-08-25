package tests;
import api.ApiSteps;
import config.TestData;
import helpers.WithLogin;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Story;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pages.ProfilePage;

@DisplayName("Tests for deleting a book in the Store Application")
@Owner("Anosov Aleksandr")
@Story("Book delete actions")
@Feature("Book Store")
@Tag("Book")
public class DeleteBookTest extends TestBase {

    final ProfilePage profilePage = new ProfilePage();
    final ApiSteps apiSteps = new ApiSteps();

    @Test
    @DisplayName("Delete a book from user profile")
    @Owner("Anosov Aleksandr")
    @WithLogin
    public void deleteBookFromProfileBooksListTest() {
        apiSteps.addRandomBook();
        String isbn = apiSteps.getIsbn();
            ApiSteps.deleteBook(isbn);
            profilePage.openProfilePage()
            .removeBanner()
            .checkBooksListIsEmpty();
    }

    @Test
    @DisplayName("Attempt to delete a non-existent book from user profile")
    @Owner("Anosov Aleksandr")
    @WithLogin
    public void deleteNonExistentBookFromProfileTest() {
        // Добавляем книгу с использованием случайного ISBN
        apiSteps.addRandomBook();
        String addedIsbn = apiSteps.getIsbn();

        // Получаем другой случайный ISBN, который не равен добавленному
        String nonExistentIsbn;
        do {
            nonExistentIsbn = TestData.getRandomIsbn();
        } while (nonExistentIsbn.equals(addedIsbn));

        // Пытаемся удалить книгу с несуществующим ISBN
        ApiSteps.deleteNonExistentBook(nonExistentIsbn);
    }
}
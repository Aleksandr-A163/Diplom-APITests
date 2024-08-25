package tests;
import api.ApiSteps;
import config.TestData;
import helpers.WithLogin;
import io.qameta.allure.Owner;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import pages.ProfilePage;


@Tag("API")
@DisplayName("Tests for Book Store Application")
@Owner("Anosov Aleksandr")
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
            apiSteps.deleteBookByIsbn(isbn);
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
        apiSteps.deleteNonExistentBookByIsbn(nonExistentIsbn);
    }
}
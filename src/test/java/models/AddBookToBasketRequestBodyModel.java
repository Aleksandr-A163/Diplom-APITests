package models;

import lombok.Data;
import java.util.ArrayList;

@Data
public class AddBookToBasketRequestBodyModel {

    private String userId;
    private ArrayList<CollectionOfIsbns> collectionOfIsbns;

    public void setIsbn(String value) {
        if (value != null && !value.isEmpty()) {
            CollectionOfIsbns isbn = new CollectionOfIsbns();
            isbn.setIsbn(value);
            ArrayList<CollectionOfIsbns> isbnData = new ArrayList<>();
            isbnData.add(isbn);
            this.collectionOfIsbns = isbnData;
        } else {
            throw new IllegalArgumentException("ISBN cannot be null or empty");
        }
    }

    @Data
    public static class CollectionOfIsbns {
        String isbn;
    }
}
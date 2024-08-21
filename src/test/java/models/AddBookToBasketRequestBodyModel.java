package models;

import lombok.Data;

import java.util.ArrayList;


@Data
public class AddBookToBasketRequestBodyModel {

    private String userId;
    private ArrayList<CollectionOfIsbns> collectionOfIsbns;

    public void setIsbn(String value) {
        CollectionOfIsbns isbn = new CollectionOfIsbns();
        isbn.setIsbn(value);
        ArrayList<CollectionOfIsbns> isbnData = new ArrayList<>();
        isbnData.add(isbn);
        this.collectionOfIsbns = isbnData;
    }


    @Data
    public static class CollectionOfIsbns {
        String isbn;
    }

}
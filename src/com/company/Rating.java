package com.company;

public class Rating {

    Double rating;
    Book bookId;
    User userId;

    Rating(User userId, Book bookId, Double rating) {
        this.rating = rating;
        this.bookId = bookId;
        this.userId = userId;
    }
}

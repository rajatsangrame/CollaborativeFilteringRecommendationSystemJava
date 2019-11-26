package com.company;

public class Book {

    String name;
    int bookId;

    Book(String name, int id) {
        this.name = name;
        this.bookId = id;
    }

    @Override
    public boolean equals(Object o) {
        Book book = (Book) o;
        return this.bookId == book.bookId;
    }
}

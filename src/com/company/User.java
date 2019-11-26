package com.company;

public class User {

    String userName;
    int id;

    User(String userName, int id) {
        this.userName = userName;
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        User user = (User) o;
        return this.id == user.id;
    }
}

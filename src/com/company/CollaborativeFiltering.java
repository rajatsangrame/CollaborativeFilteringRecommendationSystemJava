package com.company;

import java.util.*;
import java.util.Map.Entry;

/*
 * Ref Problem: https://blog.knoldus.com/machinex-cosine-similarity-for-item-based-collaborative-filtering/
 */
public class CollaborativeFiltering {

    private static Map<Book, HashMap<User, Double>> mUserData = new HashMap<>();
    private static Map<Book, HashMap<Book, Double>> mSimilarityTable = new HashMap<>();

    public static void main(String[] args) {

        //region Fill mUserData with available data
        for (Rating rat : mRatingList) {

            if (!mUserData.containsKey(rat.bookId)) {
                HashMap<User, Double> rating = new HashMap<>();
                rating.put(rat.userId, rat.rating);
                mUserData.put(rat.bookId, rating);

            } else {

                HashMap<User, Double> rating = mUserData.get(rat.bookId);
                rating.put(rat.userId, rat.rating);
                mUserData.put(rat.bookId, rating);
            }
        }
        //endregion

        for (Entry<Book, HashMap<User, Double>> bookCol2 : mUserData.entrySet()) {

            for (Entry<Book, HashMap<User, Double>> bookCol1 : mUserData.entrySet()) {

                if (bookCol1.getKey().bookId == bookCol2.getKey().bookId) {
                    // Todo: Add rating == 1 for this case
                    continue;
                }

                System.out.println(String.format("%s vs %s", bookCol2.getKey().name, bookCol1.getKey().name));

                HashMap<User, Double> bookCol1Values = bookCol1.getValue();
                HashMap<User, Double> bookCol2Values = bookCol2.getValue();

                List<Double> book2Rating = new ArrayList<>();
                List<Double> book1Rating = new ArrayList<>();

                for (Entry<User, Double> uBook2 : bookCol2Values.entrySet()) {

                    for (Entry<User, Double> uBook1 : bookCol1Values.entrySet()) {

                        if (uBook2.getKey().equals(uBook1.getKey())) {

                            book2Rating.add(uBook2.getValue());
                            book1Rating.add(uBook1.getValue());
                            break;
                        }
                    }
                }

                // cos(v1,v2) = (5*2 + 3*3 + 1*3) / sqrt[(25+9+1) * (4+9+9)] = 0.792

                Double n = 0.0;
                Double d = 0.0;
                Double aaa = 0.0;
                Double bbb = 0.0;

                for (int i = 0; i < book1Rating.size(); i++) {
                    n = n + (book1Rating.get(i) * book2Rating.get(i));
                }
                for (int i = 0; i < book1Rating.size(); i++) {
                    aaa = aaa + Math.pow(book1Rating.get(i), 2.0);
                    bbb = bbb + Math.pow(book2Rating.get(i), 2.0);
                }
                d = Math.sqrt(aaa * bbb);
                Double theta = n / d;

                System.out.println(theta);

                if (!mSimilarityTable.containsKey(bookCol2.getKey())) {

                    HashMap<Book, Double> similarity = new HashMap<>();
                    similarity.put(bookCol1.getKey(), theta);
                    mSimilarityTable.put(bookCol2.getKey(), similarity);

                } else {

                    HashMap<Book, Double> similarity = mSimilarityTable.get(bookCol2.getKey());
                    similarity.put(bookCol1.getKey(), theta);
                    mSimilarityTable.put(bookCol2.getKey(), similarity);
                }
            }
        }

        System.out.println(String.format("Similarity Table Size %s", mSimilarityTable.size()));

        // Test Recommendation
        User amy = mUserList.get(1);
        Book forestGump = mBookList.get(2);

        User david = mUserList.get(4);
        Book pulpFiction = mBookList.get(1);

        User bradly = mUserList.get(5);
        Book godfather = mBookList.get(3);

        Double result = getRecommendationForUser(david, pulpFiction);

    }

    private static Double getRecommendationForUser(User user, Book book) {

        Map<Book, Double> userData = new HashMap<>();

        for (Entry<Book, HashMap<User, Double>> data : mUserData.entrySet()) {

            HashMap<User, Double> mapData = data.getValue();

            if (mapData.containsKey(user)) {
                userData.put(data.getKey(), mapData.get(user));
            }
        }

        Map<Book, Double> similarityForUserData = new HashMap<>();

        HashMap<Book, Double> bookData = mSimilarityTable.get(book);

        for (Entry<Book, Double> itr : userData.entrySet()) {

            for (Entry<Book, Double> itr2 : bookData.entrySet()) {

                if (itr2.getKey().equals(itr.getKey())) {

                    similarityForUserData.put(itr.getKey(), itr2.getValue());
                }
            }
        }

        // (4*0.792 + 5*0.8) / (0.792+ 0.8) = 4.5
        Double n = 0.0;
        Double d = 0.0;

        for (Entry<Book, Double> itr : userData.entrySet()) {

            for (Entry<Book, Double> itr2 : similarityForUserData.entrySet()) {

                if (itr2.getKey().equals(itr.getKey())) {

                    n = n + (itr.getValue() * itr2.getValue());
                    d = d + itr2.getValue();
                }
            }
        }

        Double result = n / d;
        System.out.println(String.format("Recommendation for %s for %s is %s",
                user.userName, book.name, result));

        return result;
    }

    //region  Books
    private static List<Book> mBookList = Arrays.asList(
            new Book("Dummy", 0),
            new Book("Pulp Fiction", 1),
            new Book("Forrest Gump", 2),
            new Book("The Godfather", 3)
    );
    //endregion

    //region Users
    private static List<User> mUserList = Arrays.asList(
            new User("Dummy", 0),
            new User("Amy", 1),
            new User("Calvin", 2),
            new User("Robert", 3),
            new User("David", 4),
            new User("Bradley", 5)
    );
    //endregion

    //region Rating
    private static List<Rating> mRatingList = Arrays.asList(
            new Rating(mUserList.get(1), mBookList.get(1), 4.0),
            new Rating(mUserList.get(1), mBookList.get(3), 5.0),
            new Rating(mUserList.get(2), mBookList.get(1), 5.0),
            new Rating(mUserList.get(3), mBookList.get(1), 3.0),
            new Rating(mUserList.get(2), mBookList.get(2), 3.0),
            new Rating(mUserList.get(3), mBookList.get(2), 4.0),
            new Rating(mUserList.get(3), mBookList.get(3), 1.0),
            new Rating(mUserList.get(4), mBookList.get(2), 2.0),
            new Rating(mUserList.get(5), mBookList.get(1), 1.0),
            new Rating(mUserList.get(4), mBookList.get(3), 2.0),
            new Rating(mUserList.get(5), mBookList.get(2), 3.0));

    //endregion
}
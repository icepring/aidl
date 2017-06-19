// IBookManagerInterface.aidl
package com.tym.aidl;
import com.tym.aidl.Book;

// Declare any non-default types here with import statements

interface IBookManagerInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);

    List<Book> getBooks();

    void addBook(in Book book);
}

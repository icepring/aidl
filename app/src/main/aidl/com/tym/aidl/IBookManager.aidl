// IBookManagerInterface.aidl
package com.tym.aidl;
import com.tym.aidl.Book;
import com.tym.aidl.IOnNewBookArrivedListener;

// Declare any non-default types here with import statements

interface IBookManager{
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);

    List<Book> getBooks();

    void addBook(in Book book);

    void registerOnNewBookArrived(in IOnNewBookArrivedListener listener);

    void unregisterOnNewBookArrived(in IOnNewBookArrivedListener listener);
}

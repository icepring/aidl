// IDeleteBook.aidl
package com.tym.aidl;
import com.tym.aidl.Book;

// Declare any non-default types here with import statements

interface IDeleteBook {
    void deleteAll();
    void deleteOne(in Book book);
}

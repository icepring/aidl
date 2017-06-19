package com.tym.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @Author Jliuer
 * @Date 2017/06/19/17:09
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class BookManagerService extends Service {

    private CopyOnWriteArrayList<Book> mBookList = new CopyOnWriteArrayList<>();

    private Binder bookBinder = new IBookManagerInterface.Stub() {
        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public List<Book> getBooks() throws RemoteException {
            return mBookList;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            mBookList.add(book);
            Log.e("BookManagerService",book.toString());
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return bookBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mBookList.add(new Book("tym", "青春"));
        mBookList.add(new Book("tym", "青春Ⅱ"));
        mBookList.add(new Book("tym", "逝去的青春"));
    }
}

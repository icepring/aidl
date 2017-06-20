package com.tym.aidl;

import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * @Author Jliuer
 * @Date 2017/06/19/17:09
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class BookManagerService extends Service {

    public static final String PERMISSION="com.tym.aidl.permission.ACCESS_BOOK_SERVICE";

    private CopyOnWriteArrayList<Book> mBookList = new CopyOnWriteArrayList<>();

    private RemoteCallbackList<IOnNewBookArrivedListener> mBookListener = new RemoteCallbackList<>();

    private AtomicBoolean mServiceIsDestroy = new AtomicBoolean();

    private Subscription mBookSubscription;

    private Binder bookBinder = new IBookManager.Stub() {
        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public List<Book> getBooks() throws RemoteException {
            for (Book book : mBookList) {
                Log.e("BookManagerService", book.toString());
            }
            return mBookList;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            mBookList.add(book);
            Log.e("BookManagerService", book.toString());
        }

        @Override
        public void registerOnNewBookArrived(IOnNewBookArrivedListener listener) throws RemoteException {
            mBookListener.register(listener);
            Log.e("注册", "s");
        }

        @Override
        public void unregisterOnNewBookArrived(IOnNewBookArrivedListener listener) throws RemoteException {
            mBookListener.unregister(listener);
            Log.e("解除注册", "s");
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
//        int permission=checkCallingOrSelfPermission(PERMISSION);
//        if (permission== PackageManager.PERMISSION_DENIED){
//            return null;
//        }
        mServiceIsDestroy.set(true);
        newBookArrive();
        return bookBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mBookList.add(new Book("tym", "青春"));
        mBookList.add(new Book("tym", "青春Ⅱ"));
        mBookList.add(new Book("tym", "逝去的青春"));
    }

    @Override
    public boolean onUnbind(Intent intent) {
        mServiceIsDestroy.set(false);
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        mServiceIsDestroy.set(false);
        super.onDestroy();
    }

    private void newBookArrive() {
        mBookSubscription = Observable.interval(5, TimeUnit.SECONDS)
                .filter(new Func1<Long, Boolean>() {
                    @Override
                    public Boolean call(Long aLong) {
                        return mServiceIsDestroy.get();
                    }
                })
                .flatMap(new Func1<Long, Observable<Integer>>() {
                    @Override
                    public Observable<Integer> call(Long aLong) {
                        return Observable.range(0, mBookListener.beginBroadcast());
                    }
                })
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Book book = new Book("tym", "第" + integer + "本书");
                        mBookList.add(book);
                        Log.e("Service::新书推送", book.toString());
                        IOnNewBookArrivedListener iOnNewBookArrivedListener = mBookListener.getBroadcastItem(integer);
                        try {
                            iOnNewBookArrivedListener.onNewBookArrived(book);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        mBookListener.finishBroadcast();
                    }
                });
    }
}

package com.tym.aidl;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.List;

import rx.Observable;
import rx.Scheduler;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int DEATHCODE = 0;

    private IBookManager mIBookManager;

    private Intent mBookServiceIntent;

    private Button addBook, queryBooks, register, unregister;

    private List<Book> mBookList;

    private BinderPool mBinderPool;

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mIBookManager = IBookManager.Stub.asInterface(service);
            try {
                mIBookManager.asBinder().linkToDeath(mDeathRecipient, DEATHCODE);
                mIBookManager.registerOnNewBookArrived(mIOnNewBookArrivedListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mIBookManager = null;
        }
    };

    private IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            if (mIBookManager == null) {
                return;
            }
            mIBookManager.asBinder().unlinkToDeath(mDeathRecipient, DEATHCODE);
            mIBookManager = null;
            // TODO 在这里可以重连
            bindService(mBookServiceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBookServiceIntent = new Intent(this, BookManagerService.class);
//        bindService(mBookServiceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
        addBook = (Button) findViewById(R.id.add_book);
        queryBooks = (Button) findViewById(R.id.query_books);
        addBook.setOnClickListener(this);
        queryBooks.setOnClickListener(this);
        register = (Button) findViewById(R.id.register_book);
        unregister = (Button) findViewById(R.id.unregister_books);
        register.setOnClickListener(this);
        unregister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.add_book:
//                    mIBookManager.addBook(new Book("tym", "添加"));
                    deleteBook();
                    break;
                case R.id.query_books:
                    mBookList = mIBookManager.getBooks();
                    break;
                case R.id.register_book:
                    mIBookManager.registerOnNewBookArrived(mIOnNewBookArrivedListener);
                    break;
                case R.id.unregister_books:
                    mIBookManager.unregisterOnNewBookArrived(mIOnNewBookArrivedListener);
                    break;
                default:

            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        if (mIBookManager != null && mIBookManager.asBinder().isBinderAlive()) {
            try {
                mIBookManager.unregisterOnNewBookArrived(mIOnNewBookArrivedListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        unbindService(mServiceConnection);
        super.onDestroy();
    }

    private void deleteBook() {
        Observable.just(1)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        mBinderPool = BinderPool.getInstance(MainActivity.this, true);
                        IBinder deleteBook = mBinderPool.queryBinder(BinderPool.BINDER_DELETE);
                        IDeleteBook dleteBookImpl = DeleteBookImpl.asInterface(deleteBook);
                        try {
                            dleteBookImpl.deleteOne(new Book("tym", "delete"));
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }

    private IOnNewBookArrivedListener mIOnNewBookArrivedListener = new IOnNewBookArrivedListener.Stub() {

        @Override
        public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

        }

        @Override
        public void onNewBookArrived(Book book) throws RemoteException {
            Log.e("onNewBookArrived", book.toString());
        }

    };
}

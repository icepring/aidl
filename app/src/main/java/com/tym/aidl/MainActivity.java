package com.tym.aidl;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int DEATHCODE = 0;

    private IBookManagerInterface mIBookManagerInterface;

    private Intent mBookServiceIntent;

    private Button addBook, queryBooks;

    private List<Book> mBookList;

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mIBookManagerInterface = IBookManagerInterface.Stub.asInterface(service);
            try {
                mIBookManagerInterface.asBinder().linkToDeath(mDeathRecipient, DEATHCODE);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mIBookManagerInterface = null;
        }
    };

    private IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            if (mIBookManagerInterface == null) {
                return;
            }
            mIBookManagerInterface.asBinder().unlinkToDeath(mDeathRecipient, DEATHCODE);
            mIBookManagerInterface = null;
            // TODO 在这里可以重连
            bindService(mBookServiceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBookServiceIntent = new Intent(this, BookManagerService.class);
        bindService(mBookServiceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);
        addBook = (Button) findViewById(R.id.add_book);
        queryBooks = (Button) findViewById(R.id.query_books);
        addBook.setOnClickListener(this);
        queryBooks.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.add_book:
                    mIBookManagerInterface.addBook(new Book("tym", "添加"));
                    break;
                case R.id.query_books:
                    mBookList = mIBookManagerInterface.getBooks();
                    for (Book book:mBookList){
                        Log.e("BookManagerService",book.toString());
                    }
                    break;
                default:

            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        unbindService(mServiceConnection);
        super.onDestroy();
    }
}

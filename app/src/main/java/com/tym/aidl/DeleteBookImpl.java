package com.tym.aidl;

import android.os.RemoteException;
import android.util.Log;

/**
 * @Author Jliuer
 * @Date 2017/06/20/14:48
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class DeleteBookImpl extends IDeleteBook.Stub {

    @Override
    public void deleteAll() throws RemoteException {

    }

    @Override
    public void deleteOne(Book book) throws RemoteException {
        Log.e("DleteBookImpl",book.toString());
    }
}

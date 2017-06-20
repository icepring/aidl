package com.tym.aidl;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import java.util.concurrent.CountDownLatch;

/**
 * @Author Jliuer
 * @Date 2017/06/20/14:31
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class BinderPool {

    private static final String TAG = "BinderPool";
    private static final int BINDER_DEATH = 0;

    public static final int BINDER_NONE = -1;
    public static final int BINDER_DROP = 0;
    public static final int BINDER_DELETE = 1;

    private CountDownLatch mConnectBinderPoolCountDownLatch;
    private static BinderPool instance;
    private Context mContext;
    private IBinderPool mBinderPool;

    private IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            mBinderPool.asBinder().unlinkToDeath(mDeathRecipient, BINDER_DEATH);
            mBinderPool = null;
            connectBinderPoolService();
        }
    };

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBinderPool = IBinderPool.Stub.asInterface(service);
            try {
                mBinderPool.asBinder().linkToDeath(mDeathRecipient, BINDER_DEATH);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            mConnectBinderPoolCountDownLatch.countDown();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private BinderPool(Context context, boolean startService) {
        mContext = context.getApplicationContext();
        if (startService)
            connectBinderPoolService();
    }

    public static BinderPool getInstance(Context context, boolean startService) {
        if (instance == null) {
            synchronized (BinderPool.class) {
                if (instance == null) {
                    instance = new BinderPool(context, startService);
                }
            }
        }
        return instance;
    }

    public IBinder queryBinder(int binderCode) {
        IBinder binder = null;
        try {
            if (mBinderPool != null) {
                binder = mBinderPool.queryBinder(binderCode);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return binder;
    }

    private void connectBinderPoolService() {
        mConnectBinderPoolCountDownLatch = new CountDownLatch(1);
        Intent intent = new Intent(mContext, BinderPoolService.class);
        mContext.bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
        try {
            mConnectBinderPoolCountDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static class IBinderPoolImpl extends IBinderPool.Stub {
        @Override
        public IBinder queryBinder(int questCode) throws RemoteException {
            IBinder binder = null;
            switch (questCode) {
                case BINDER_NONE:
                    binder = null;
                    break;
                case BINDER_DROP:
                    binder = new DropRateImpl();
                    break;
                case BINDER_DELETE:
                    binder = new DeleteBookImpl();
                    break;
                default:
            }
            return binder;
        }
    }
}

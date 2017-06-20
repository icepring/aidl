package com.tym.aidl;

import android.os.RemoteException;
/**
 * @Author Jliuer
 * @Date 2017/06/20/14:49
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class DropRateImpl extends IDropRate.Stub {
    @Override
    public int checkDropRate(String pck) throws RemoteException {
        return 2333;
    }
}

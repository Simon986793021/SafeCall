package com.wind.safecall.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.android.internal.telephony.ITelephony;
import com.wind.safecall.contentprovider.MyContentProvider;

import org.w3c.dom.ls.LSInput;

import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 * Created by zhangcong on 2017/4/10.
 */

public class BlackNumService extends Service {
    private TelephonyManager tm;
    private MyPhoneStateListener listener;
    private NotificationManager nm;
    @Override
    public void onCreate() {
        super.onCreate();
        tm= (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        listener=new MyPhoneStateListener();
        tm.listen(listener,PhoneStateListener.LISTEN_CALL_STATE);
        Log.i("WIND_Simon","Service");
       // nm= (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }
    private final class MyPhoneStateListener extends PhoneStateListener{
        //private long startTime = 0;
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            // TODO Auto-generated method stub
            super.onCallStateChanged(state, incomingNumber);
            switch (state) {
                case TelephonyManager.CALL_STATE_RINGING:
                    String array[]={"blacknum"};
                    Cursor cursor=getContentResolver().query(MyContentProvider.uri,array,null,null,null);
                    int blacknumindex=cursor.getColumnIndex("blacknum");
                    Log.i(">>>",blacknumindex+"");
                    cursor.moveToFirst();
                    ArrayList<String> list=new ArrayList<>();
                    while (!cursor.isAfterLast())
                    {
                        String blacknum=cursor.getString(blacknumindex);
                        list.add(blacknum);
                        cursor.moveToNext();
                        Log.i(">>>",">>>>");
                        Log.i("Simon",list.toString());
                    }
                    if (list!=null&&list.contains(incomingNumber))
                    {
                        endCall();
                        return;
                    }
                    //判断来电黑名单是否开启
//                    boolean isblackstart = sp.getBoolean("isblacknumber", false);
//                    if(isblackstart){
//                        boolean isBlackNumber = blackNumberDao.isBlackNumber(incomingNumber);
//                        if(isBlackNumber){
//
//                        }


                    //startTime = System.currentTimeMillis();

                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    break;
                case TelephonyManager.CALL_STATE_IDLE:
                    break;

                default:
                    break;
            }
        }

    }
    //挂断电话
    private void endCall(){
        try {
            Class<?> clazz = Class.forName("android.os.ServiceManager");
            Method method = clazz.getMethod("getService", String.class);
            IBinder ibinder = (IBinder) method.invoke(null, Context.TELEPHONY_SERVICE);
            ITelephony iTelephony = ITelephony.Stub.asInterface(ibinder);
            iTelephony.endCall();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}

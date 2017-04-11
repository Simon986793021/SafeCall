package com.wind.safecall.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.wind.safecall.service.BlackNumService;


/**
 * Created by zhangcong on 2017/4/10.
 */

public class BootCompletedReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent){
        Log.i(">>>>>>>>","已经开机");
        Intent intent1=new Intent(context, BlackNumService.class);
        context.startService(intent1);
    }
}

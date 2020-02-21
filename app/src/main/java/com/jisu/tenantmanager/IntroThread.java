package com.jisu.tenantmanager;

import android.os.Handler;
import android.os.Message;

public class IntroThread extends Thread {

    private Handler mHandler;

    public IntroThread(Handler handler) {
        mHandler = handler;
    }
    @Override
    public void run() {
        Message msg = new Message();

        try {
            Thread.sleep(3000);
            msg.what = 1;
            mHandler.sendEmptyMessage(msg.what);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

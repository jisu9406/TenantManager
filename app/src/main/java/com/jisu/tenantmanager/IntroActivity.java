package com.jisu.tenantmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        IntroThread introThread = new IntroThread(mHandler);
        introThread.start();
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message message){
            if(message.what==1) {
                FragmentManager mFragmentMananger = getSupportFragmentManager();
                FragmentTransaction mFragmentTransaction = mFragmentMananger.beginTransaction();
                mFragmentTransaction.add(R.id.login_fragment_container, new LoginFragment());
                mFragmentTransaction.commit();
            }
        }
    };
}

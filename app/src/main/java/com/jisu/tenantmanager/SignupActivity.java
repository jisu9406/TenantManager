package com.jisu.tenantmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.os.Bundle;
import android.view.Menu;
import android.widget.Button;

public class SignupActivity extends AppCompatActivity {

    Toolbar mToolbar;
    Button mSubmitButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mToolbar= findViewById(R.id.signup_toolbar);
        mToolbar.setTitle(R.string.signup_toolbar_title);
        mToolbar.setTitleTextColor(getResources().getColor(R.color.White));
        setSupportActionBar(mToolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.signupbar, menu) ;
        return true ;
    }


}

package com.jisu.tenantmanager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = "SignupActivity";
    private Toolbar mToolbar;
    private Button mSubmitButton;
    private EditText mNameEditText;
    private EditText mPhoneEditText;
    private EditText mAddressEditText;
    private EditText mAgeEditText;

    private AlertDialog.Builder mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mToolbar= findViewById(R.id.signup_toolbar);
        mNameEditText = findViewById(R.id.name_edittext);
        mPhoneEditText = findViewById(R.id.phonenumber_edittext);
        mAddressEditText = findViewById(R.id.address_edittext);
        mAgeEditText = findViewById(R.id.age_edittext);
        mSubmitButton = findViewById(R.id.submit_button);

        mSubmitButton.setOnClickListener(this);
        toolbarStyle();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.signupbar, menu) ;
        return true ;
    }

    @Override
    public void onClick(View view) {
        String name = mNameEditText.getText().toString();
        String phonenumber = mPhoneEditText.getText().toString();
        String address = mAddressEditText.getText().toString();
        int age = Integer.parseInt(mAgeEditText.getText().toString());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_cancel:
                cancelDialog();
                return true;
            default :
                return super.onOptionsItemSelected(item);
        }
    }

    private void toolbarStyle() {
        mToolbar.setTitle(R.string.signup_toolbar_title);
        mToolbar.setTitleTextColor(getResources().getColor(R.color.White));
        setSupportActionBar(mToolbar);
    }

    private void cancelDialog() {
        mDialog = new AlertDialog.Builder(this);
        mDialog.setTitle(R.string.cancel_dialog_title);
        mDialog.setMessage(R.string.cancel_dialog_message);
        mDialog.setPositiveButton(R.string.dialog_possitive_btn,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.i(TAG, "cancelDialog(...) Positive Button Clicked");

                        finish();
                    }
                });
        mDialog.setNegativeButton(R.string.dialog_negative_btn,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.i(TAG, "cancelDialog(...) Negative Button Clicked");
                    }
                });
        mDialog.show();
    }
}

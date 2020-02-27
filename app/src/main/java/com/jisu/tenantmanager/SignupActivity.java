package com.jisu.tenantmanager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = "SignupActivity";
    private Toolbar mToolbar;
    private Button mSubmitButton;
    private EditText mNameEditText;
    private EditText mPhoneEditText;
    private EditText mAddressEditText;
    private EditText mAgeEditText;

    private AlertDialog.Builder mDialog;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private ChildEventListener mChildEventListener;

    private int mUniqueNumber = 1;
    private ArrayList<Integer> mUnumberList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mToolbar= findViewById(R.id.signup_toolbar);
        mNameEditText = findViewById(R.id.name_edittext);
        mAgeEditText = findViewById(R.id.age_edittext);
        mPhoneEditText = findViewById(R.id.phonenumber_edittext);
        mAddressEditText = findViewById(R.id.address_edittext);
        mSubmitButton = findViewById(R.id.submit_button);

        mSubmitButton.setOnClickListener(this);
        toolbarStyle();

        //Firebase
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference().child("Users");
        getUniqueNumber();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.signupbar, menu) ;
        return true ;
    }

    @Override
    public void onClick(View view) {
        String name = mNameEditText.getText().toString();
        int age = Integer.parseInt(mAgeEditText.getText().toString());
        String phonenumber = mPhoneEditText.getText().toString();
        String address = mAddressEditText.getText().toString();

        mDatabaseReference.child(String.valueOf(mUniqueNumber)).child("Name").setValue(name);
        mDatabaseReference.child(String.valueOf(mUniqueNumber)).child("Age").setValue(age);
        mDatabaseReference.child(String.valueOf(mUniqueNumber)).child("PhoneNumber").setValue(phonenumber);
        mDatabaseReference.child(String.valueOf(mUniqueNumber)).child("Address").setValue(address);
        
        mChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.i(TAG, "onChildAdded(...)");
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.i(TAG, "onChildChanged(...)");
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Log.i(TAG, "onChildRemoved(...)");
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.i(TAG, "onChildMoved(...)");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.i(TAG, "onCancelled(...)");
            }
        };
        mDatabaseReference.addChildEventListener(mChildEventListener);

        Intent intent = new Intent(this.getApplicationContext(), MainActivity.class);
        startActivity(intent);
        Log.i(TAG, "submit button clicked!");
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

    private void getUniqueNumber() {
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(int i=1; i<=dataSnapshot.getChildrenCount(); i++) {
                    mUnumberList.add(i);
                }
                mUniqueNumber += mUnumberList.size();
                Log.i(TAG, "uniqueNumber : " + mUniqueNumber);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

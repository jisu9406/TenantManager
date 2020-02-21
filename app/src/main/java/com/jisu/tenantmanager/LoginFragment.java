package com.jisu.tenantmanager;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment implements View.OnClickListener {

    private final String TAG = "LoginFragment";
    EditText mIdEditText;
    EditText mPwEditText;
    Button mLoginButton;

    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mDatabaseReference;

    String[] account = new String[2];

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mView = inflater.inflate(R.layout.fragment_login, container, false);

        mIdEditText = mView.findViewById(R.id.id_edittext);
        mPwEditText = mView.findViewById(R.id.pwd_edittext);
        mLoginButton = mView.findViewById(R.id.login_button);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference();
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    Log.i(TAG, "Data : " + snapshot.getValue());

                    AccountData mAccountData = snapshot.child("Admin_account").getValue(AccountData.class);
                    account[0] = mAccountData.id;
                    account[1] = mAccountData.password;
                }

                Log.i(TAG, "id : " + account[0] + " password : " + account[1]);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mLoginButton.setOnClickListener(this);
        return mView;
    }

    @Override
    public void onClick(View view) {
        login();
    }

    private void login() {
        String id = mIdEditText.getText().toString();
        String password = mPwEditText.getText().toString();
        String dbId = account[0];
        String dbPassword = account[1];

        if(TextUtils.isEmpty(id)){
            Toast.makeText(this.getContext(), "email을 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this.getContext(), "password를 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        if(id.equals(dbId) && password.equals(dbPassword)) {
            Toast.makeText(this.getContext(), "로그인 되었습니다.", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(getContext(), MainActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(this.getContext(), "틀렸습니다.", Toast.LENGTH_SHORT).show();
        }

    }
}

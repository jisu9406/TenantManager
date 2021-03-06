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
    private EditText mIdEditText;
    private EditText mPwEditText;
    private Button mLoginButton;

    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mDatabaseReference;

    private String[] mAccount = new String[2];

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        mIdEditText = view.findViewById(R.id.id_edittext);
        mPwEditText = view.findViewById(R.id.pwd_edittext);
        mLoginButton = view.findViewById(R.id.login_button);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference().child("Admin");
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()) {
                    Log.i(TAG, "Data : " + snapshot.getValue());

                    mAccount[0] = (String) snapshot.child("Id").getValue();
                    mAccount[1] = (String) snapshot.child("Password").getValue();
                }
                Log.i(TAG, "id : " + mAccount[0] + " password : " + mAccount[1]);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mLoginButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
//        login();
        Intent intent = new Intent(getContext(), MainActivity.class);
        startActivity(intent);
    }

    private void login() {
        String id = mIdEditText.getText().toString();
        String password = mPwEditText.getText().toString();
        String dbId = mAccount[0];
        String dbPassword = mAccount[1];

        if(TextUtils.isEmpty(id)){
            Toast.makeText(this.getContext(), "ID을 입력해 주세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this.getContext(), "PASSWORD를 입력해 주세요.", Toast.LENGTH_SHORT).show();
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

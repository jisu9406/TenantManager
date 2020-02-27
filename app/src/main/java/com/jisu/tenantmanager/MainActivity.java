package com.jisu.tenantmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";

    private Toolbar mToolbar;
    private Intent mIntent;
    private AlertDialog.Builder mDialog;
    private TextView mNoUserTextView;

    //RecyclerView
    private ArrayList<Dictionary> mArrayList;
    private RecyclerView mRecyclerView;
    private RecyclerAdapter mRecyclerAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private DividerItemDecoration mDividerItemDecoration;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;

    private int mUniqueNumber =1;
    private ArrayList<Integer> mUnumberList = new ArrayList<>();
    private ArrayList<String> mNameList = new ArrayList<>();
    private ArrayList<String> mAgeList = new ArrayList<>();
    private ArrayList<String> mPhoneList = new ArrayList<>();
    private ArrayList<String> mAddressList = new ArrayList<>();

    //뒤로가기 버튼 눌렸을 때
   private long pressedTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = findViewById(R.id.main_toolbar);
        mNoUserTextView = findViewById(R.id.nouser_textview);
        mRecyclerView = findViewById(R.id.user_recyclerview);
        toolbarStyle();
        mArrayList = new ArrayList<>();

        //Firebase
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference().child("Users");
        initRecyclerView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.appbar, menu) ;

        return true ;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search :
                sortUserDialog();
                return true ;
            case R.id.action_adduser :
                addUserDialog();
                return true ;
            default :
                return super.onOptionsItemSelected(item);
        }
    }

    private void toolbarStyle() {
        mToolbar.setTitle(R.string.app_name);
        mToolbar.setTitleTextColor(getResources().getColor(R.color.White));
        setSupportActionBar(mToolbar);
    }

    private void addUserDialog() {
        mDialog = new AlertDialog.Builder(this);
        mDialog.setTitle(R.string.adduser_dialog_title);
        mDialog.setMessage(R.string.adduser_dialog_message);
        mDialog.setPositiveButton(R.string.dialog_possitive_btn,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.i(TAG, "addUserDialog(...) Positive Button Clicked");

                        mIntent = new Intent(getApplicationContext(), SignupActivity.class);
                        startActivity(mIntent);
                    }
                });
        mDialog.setNegativeButton(R.string.dialog_negative_btn,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.i(TAG, "addUserDialog(...) Negative Button Clicked");
                    }
                });
        mDialog.show();
    }

    private void sortUserDialog() {
        //아이템 목록
        final List<String> mListItems = new ArrayList<>();
        mListItems.add("최신순");
        mListItems.add("이름");
        mListItems.add("관리 회사");
        mListItems.add("유학원 명");
        mListItems.add("멘션 명");
        final CharSequence[] items =  mListItems.toArray(new String[mListItems.size()]);

        //선택된 아이템
        final List mSelectedItems  = new ArrayList();
        int defaultItem = 0;
        mSelectedItems.add(defaultItem);

        mDialog = new AlertDialog.Builder(this);
        mDialog.setTitle(R.string.sortuser_dialog_title);
        mDialog.setSingleChoiceItems(items, defaultItem,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mSelectedItems.clear();
                        mSelectedItems.add(i);
                    }
                });
        mDialog.setPositiveButton(R.string.dialog_possitive_btn,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String msg="";

                        if (!mSelectedItems.isEmpty()) {
                            int index = (int) mSelectedItems.get(0);
                            msg = mListItems.get(index);
                        }
                        Toast.makeText(getApplicationContext(),
                                "Items Selected\n"+ msg , Toast.LENGTH_SHORT)
                                .show();
                    }
                });
        mDialog.show();
    }

    private void initRecyclerView() {
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(int i=1; i<=dataSnapshot.getChildrenCount(); i++) {
                    mUnumberList.add(i);
                    mNameList.add(String.valueOf(dataSnapshot.child(String.valueOf(i)).child("Name").getValue()));
                    mAgeList.add(String.valueOf(dataSnapshot.child(String.valueOf(i)).child("Age").getValue()));
                    mPhoneList.add(String.valueOf(dataSnapshot.child(String.valueOf(i)).child("PhoneNumber").getValue()));
                    mAddressList.add(String.valueOf(dataSnapshot.child(String.valueOf(i)).child("Address").getValue()));
                }
                mUniqueNumber = mUnumberList.size();
                Log.i(TAG, "uniqueNumber : " + mUniqueNumber);

                for(int i=1; i<=mUniqueNumber; i++) {
                    Dictionary dictionary = new Dictionary(String.valueOf(i), mNameList.get(i-1), mAgeList.get(i-1), mPhoneList.get(i-1), mAddressList.get(i-1));
                    mArrayList.add(dictionary);
                    Log.i(TAG, "mArrayList : " + mArrayList.get(i-1).getUsernumber() + " , " + mArrayList.get(i-1).getName());
                }
                if(mArrayList.isEmpty()) {
                    mNoUserTextView.setVisibility(View.INVISIBLE);
                }
                mLinearLayoutManager = new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false);
                mRecyclerAdapter = new RecyclerAdapter(mArrayList);
                mDividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), mLinearLayoutManager.getOrientation());

                mRecyclerView.setAdapter(mRecyclerAdapter);
                mRecyclerView.addItemDecoration(mDividerItemDecoration);
                mRecyclerView.setLayoutManager(mLinearLayoutManager);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //뒤로가기 키 눌렀을 때
    @Override
    public void onBackPressed() {
        if (pressedTime == 0) {
            Toast.makeText(MainActivity.this, " 한 번 더 누르면 종료됩니다." , Toast.LENGTH_SHORT).show();
            pressedTime = System.currentTimeMillis();
        }
        else {
            int seconds = (int) (System.currentTimeMillis() - pressedTime);

            if (seconds > 2000) {
                Toast.makeText(MainActivity.this, " 한 번 더 누르면 종료됩니다." , Toast.LENGTH_SHORT).show();
                pressedTime = 0;
            }
            else {
                finishAffinity();
            }
        }
    }

}

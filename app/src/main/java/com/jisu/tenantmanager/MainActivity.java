package com.jisu.tenantmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
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

    private ArrayList<Dictionary> mArrayList;
    private RecyclerView mRecyclerView;
    private RecyclerAdapter mRecyclerAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private DividerItemDecoration mDividerItemDecoration;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;

    private ArrayList<Integer> mUnumberList = new ArrayList<>();
    private ArrayList<String> mNameList = new ArrayList<>();
    private ArrayList<String> mAgeList = new ArrayList<>();
    private ArrayList<String> mPhoneList = new ArrayList<>();
    private ArrayList<String> mAddressList = new ArrayList<>();

    private int mUniqueNumber =1;
    private long mFirstPressedTime = 0;
    private boolean mIsFragment = false;
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

        setItemTouchListener();
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

    //뒤로가기 키 눌렀을 때
    @Override
    public void onBackPressed() {
        if(mIsFragment) {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.userinfo_fragment_container);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().remove(fragment).commit();
            fragmentManager.popBackStack();
            mIsFragment = false;
        } else {

            if (mFirstPressedTime == 0) {
                Toast.makeText(MainActivity.this, R.string.finish_toast_message , Toast.LENGTH_SHORT).show();
                mFirstPressedTime = System.currentTimeMillis();
            }
            else {
                int secondPressedTime = (int) (System.currentTimeMillis() - mFirstPressedTime);

                if (secondPressedTime > 2000) {
                    Toast.makeText(MainActivity.this, R.string.finish_toast_message , Toast.LENGTH_SHORT).show();
                    mFirstPressedTime = 0;
                }
                else {
                    finishAffinity();
                }
            }
        }
    }

    private void toolbarStyle() {
        mToolbar.setTitle(R.string.app_name);
        mToolbar.setTitleTextColor(getResources().getColor(R.color.White));
        setSupportActionBar(mToolbar);
    }

    //추가 다이얼로그
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

    //분류 다이얼로그
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

    //RecyclerView
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
                if(!mArrayList.isEmpty()) {
                    mNoUserTextView.setVisibility(View.GONE);
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


    //아이템 터치했을 때
    private void setItemTouchListener() {
        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), mRecyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Dictionary dictionary = mArrayList.get(position);

                Log.i(TAG,"Dictionary : " + dictionary.getName() + ", " + dictionary.getAge() + ", " + dictionary.getPhonenumber() + ", " + dictionary.getAddress());

                FragmentManager mFragmentMananger = getSupportFragmentManager();
                FragmentTransaction mFragmentTransaction = mFragmentMananger.beginTransaction();
                mFragmentTransaction.add(R.id.userinfo_fragment_container, new UserInfoFragment(dictionary.getName(), dictionary.getAge(), dictionary.getPhonenumber(), dictionary.getAddress()));
                mFragmentTransaction.commit();
                mIsFragment = true;
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector mGestureDetector;
        private MainActivity.ClickListener mClickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final MainActivity.ClickListener clickListener) {
            this.mClickListener = clickListener;
            mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {

                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
            View child = recyclerView.findChildViewUnder(motionEvent.getX(), motionEvent.getY());
            if (child != null && mClickListener != null && mGestureDetector.onTouchEvent(motionEvent)) {
                mClickListener.onClick(child, recyclerView.getChildAdapterPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        }
    }
}

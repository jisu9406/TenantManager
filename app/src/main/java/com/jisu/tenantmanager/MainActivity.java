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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";

    Toolbar mToolbar;
    Intent mIntent;
    private AlertDialog.Builder mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = findViewById(R.id.main_toolbar);
        toolbarStyle();
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
                return super.onOptionsItemSelected(item) ;
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
}

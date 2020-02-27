package com.jisu.tenantmanager;


import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserInfoFragment extends Fragment {
    View view;

    TextView mInfoNameTextView;
    TextView mInfoAgeTextView;
    TextView mInfoPhoneTextView;
    TextView mInfoAddressTextView;

    private String mName;
    private String mAge;
    private String mPhone;
    private String mAddress;

    public UserInfoFragment(String name, String age, String phone, String address) {
        this.mName = name;
        this.mAge = age;
        this.mPhone = phone;
        this.mAddress = address;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_user_info, container, false);
        mInfoNameTextView = view.findViewById(R.id.infoname_textview);
        mInfoAgeTextView = view.findViewById(R.id.infoage_textview);
        mInfoPhoneTextView = view.findViewById(R.id.infophone_textview);
        mInfoAddressTextView = view.findViewById(R.id.infoaddress_textview);

        setInfo(mName, mAge, mPhone, mAddress);
        return view;
    }

    //Activity랑 통신해서 info에 들어갈 텍스트 변
    private void setInfo(String name, String age, String phone, String address) {
        mInfoNameTextView.setText(name);
        mInfoAgeTextView.setText(age);
        mInfoPhoneTextView.setText(phone);
        mInfoAddressTextView.setText(address);
    }
}

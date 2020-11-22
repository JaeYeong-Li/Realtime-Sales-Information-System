package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


public class CurLocationActivity extends AppCompatActivity {

    public FragmentManager fragmentManager;
    public EntertainOpenFragment fragmentEntertain;
    public RestaurantOpenFragment fragmentRestaurant;
    public CafeOpenFragment fragmentCafe;

    public FragmentTransaction transaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cur_location);

        //use toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //fragment handling
        fragmentManager = getSupportFragmentManager();

        fragmentCafe = new CafeOpenFragment();
        fragmentEntertain = new EntertainOpenFragment();
        fragmentRestaurant = new RestaurantOpenFragment();

        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.FrameLayout_Map,fragmentRestaurant).commitAllowingStateLoss();
        //레스토랑 버튼 잠시 바꿔준 상태...

    }


    public void clickHandler(View view) {

        transaction = fragmentManager.beginTransaction();

        switch (view.getId())
        {
            case R.id.Button_Cafe:
                transaction.replace(R.id.FrameLayout_Map,fragmentCafe).commitAllowingStateLoss();
                break;
            case R.id.Button_Entertain:
                transaction.replace(R.id.FrameLayout_Map,fragmentEntertain).commitAllowingStateLoss();
                break;
            case R.id.Button_Restaurant:
                transaction.replace(R.id.FrameLayout_Map,fragmentRestaurant).commitAllowingStateLoss();
                break;
        }
    }
}
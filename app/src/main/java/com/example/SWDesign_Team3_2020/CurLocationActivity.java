package com.example.SWDesign_Team3_2020;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

//for toolbar
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.view.View;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

//for search
import android.content.Intent;


public class CurLocationActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private Context context = this;

    public FragmentManager fragmentManager;
    public EntertainOpenFragment fragmentEntertain;
    public RestaurantOpenFragment fragmentRestaurant;
    public CafeOpenFragment fragmentCafe;

    public FragmentTransaction transaction;

    private GlobalVar m_gvar = null;
    Boolean isLogin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cur_location);

        //globalVal
        m_gvar = (GlobalVar) getApplicationContext();
        if(m_gvar.isIDnull() == true) {
            isLogin = false;
        }else{
            isLogin=true;
        }

        //use toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //툴바에 홈버튼 활성화
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //툴바의 홈버튼의 이미지를 변경
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_navi_menu);

        //drawyerLayout
        mDrawerLayout = (androidx.drawerlayout.widget.DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();

                int id = menuItem.getItemId();
                String title = menuItem.getTitle().toString();

                if (id == R.id.curLocationSearch) {
                    Toast.makeText(context, title + "현재 페이지", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.inputSearch) {
                    Intent intent = new Intent(getApplicationContext(), Search_Calender.class);
                    startActivity(intent);
                    finish();
                } else if (id == R.id.mypage_toolbar) {
                    Intent intent;
                    if(isLogin == true){
                        intent = new Intent(getApplicationContext(), MyPage_member.class);
                    }else {
                        intent = new Intent(getApplicationContext(), MyPage_nonmember.class);
                    }
                    startActivity(intent);
                    finish();
                } else if (id == R.id.logout) {
                android.widget.Toast.makeText(context, title + ": 로그아웃 시도중", android.widget.Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });


        //fragment handling
        fragmentManager = getSupportFragmentManager();

        fragmentCafe = new CafeOpenFragment();
        fragmentEntertain = new EntertainOpenFragment();
        fragmentRestaurant = new RestaurantOpenFragment();

        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.FrameLayout_Map,fragmentRestaurant).commitAllowingStateLoss();
        //레스토랑 버튼 색 초기화
        findViewById(R.id.Button_Restaurant).setBackgroundColor(Color.GRAY);

        //Sliding pannel
  //      ListView listView = findViewById(R.id.listView);
  //      listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,new String[] {"Copy","Paste","Cut","Delete"}));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ // 왼쪽 상단 버튼 눌렀을 때
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }


    public void clickHandler(View view) {

        transaction = fragmentManager.beginTransaction();

        switch (view.getId())
        {
            case R.id.Button_Cafe:
                transaction.replace(R.id.FrameLayout_Map,fragmentCafe).commitAllowingStateLoss();
                findViewById(R.id.Button_Cafe).setBackgroundColor(Color.GRAY);
                findViewById(R.id.Button_Entertain).setBackgroundColor(getResources().getColor(R.color.blue));
                findViewById(R.id.Button_Restaurant).setBackgroundColor(getResources().getColor(R.color.blue));
                break;

            case R.id.Button_Entertain:
                transaction.replace(R.id.FrameLayout_Map,fragmentEntertain).commitAllowingStateLoss();
                findViewById(R.id.Button_Entertain).setBackgroundColor(Color.GRAY);
                findViewById(R.id.Button_Cafe).setBackgroundColor(getResources().getColor(R.color.blue));
                findViewById(R.id.Button_Restaurant).setBackgroundColor(getResources().getColor(R.color.blue));
                break;

            case R.id.Button_Restaurant:
                transaction.replace(R.id.FrameLayout_Map,fragmentRestaurant).commitAllowingStateLoss();
                findViewById(R.id.Button_Restaurant).setBackgroundColor(Color.GRAY);
                findViewById(R.id.Button_Cafe).setBackgroundColor(getResources().getColor(R.color.blue));
                findViewById(R.id.Button_Entertain).setBackgroundColor(getResources().getColor(R.color.blue));
                break;
        }
    }
}
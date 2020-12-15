package com.example.SWDesign_Team3_2020;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class SpecificInfoActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private Context context = this;

    public FragmentManager fragmentManager;
    public SpecificInfoFragment_OpenTime fragmentOpentime;
    public SpecificInfoFragment_Menu fragmentMenu;
    public SpecificInfoFragment_OpenSchedule fragmentOpenschedule;

    public FragmentTransaction transaction;

    private Button Button_Opentime;
    private Button Button_Menu;
    private Button Button_Openschedule;

    private LinearLayout Button_Prefer;
    private boolean preferedStore = false;

    private ImageView ImageView_Heart;

    private GlobalVar m_gvar = null;
    Boolean isLogin = false;


    private android.widget.TextView TextView_StoreName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_info);

        //globalVal
        m_gvar = (GlobalVar) getApplicationContext();
        if(m_gvar.isIDnull() == true) {
            isLogin = false;
        }else{
            isLogin=true;
        }

        //initialize view
        Button_Opentime = findViewById(R.id.Button_OpenTime);
        Button_Menu = findViewById(R.id.Button_Menu);
        Button_Openschedule = findViewById(R.id.Button_OpenSchedule);
        Button_Opentime.setBackgroundColor(Color.GRAY);

        Button_Prefer = findViewById(R.id.Button_Prefer);

        ImageView_Heart = findViewById(R.id.ImageView_Heart);

        TextView_StoreName = findViewById(R.id.TextView_Storename);
        TextView_StoreName.setText("아에이오우");

        //set on click listener for linear layout button
        //버튼 클릭 이벤트(리니어 레이아웃 - 선호 버튼)

        //DB찾아보고 있는 거면 preferedStore = true
        // 사진 진한 하트로 바꾸기
        if (preferedStore == false)
        {
            ImageView_Heart.setImageResource(com.example.SWDesign_Team3_2020.R.drawable.ic_baseline_add_24);

        }
        else
        {
            //DB찾아보고 없는 거면 preferedStore = false
            ImageView_Heart.setImageResource(com.example.SWDesign_Team3_2020.R.drawable.ic_baseline_check_24);


        }






        Button_Prefer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //preferedStore이 false면
                // -> "선호가게로 등록되었습니다" 하고 선호 DB에 저장, 진한 하트로 바꿔, preferedStore 바꿔
                if (preferedStore==false) {
                    Toast.makeText(context, "Bookmarked as a preferred store", Toast.LENGTH_SHORT).show();
                    preferedStore = true;
                    ImageView_Heart.setImageResource(com.example.SWDesign_Team3_2020.R.drawable.ic_baseline_check_24);
                }
                //preferedStore이 true면
                //-> 선호가게에서 삭제했습니다" 하고 선호 DB에서 삭제, 연한 하트로 바꿔, preferedStore 바뀨ㅓ
                else
                {
                    Toast.makeText(context, "Deleted from the preferred store list", Toast.LENGTH_SHORT).show();
                    preferedStore = false;
                    ImageView_Heart.setImageResource(com.example.SWDesign_Team3_2020.R.drawable.ic_baseline_add_24);

                }
            }
        });
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
                    Intent intent;
                    if(isLogin = true){
                        intent = new Intent(getApplicationContext(), MyPage_member.class);
                    }else {
                        intent = new Intent(getApplicationContext(), MyPage_nonmember.class);
                    }
                    startActivity(intent);
                    finish();
                } else if (id == R.id.mypage_toolbar) {
                    Intent intent = new Intent(getApplicationContext(), MyPage_nonmember.class);
                    startActivity(intent);
                }
                return true;
            }
        });


        //fragment handling
        fragmentManager = getSupportFragmentManager();

        fragmentOpentime = new SpecificInfoFragment_OpenTime();
        fragmentMenu = new SpecificInfoFragment_Menu();
        fragmentOpenschedule = new SpecificInfoFragment_OpenSchedule();

        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.FrameLayout_SpecificInfo, fragmentOpentime).commitAllowingStateLoss();


    }
     //버튼 클릭 이벤트(프래그먼트)
    public void clickHandler(View view) {

        //Toast.makeText(context, "BtnClikced", Toast.LENGTH_SHORT).show();

        transaction = fragmentManager.beginTransaction();

        switch (view.getId()) {
            case R.id.Button_OpenTime:
                transaction.replace(R.id.FrameLayout_SpecificInfo, fragmentOpentime).commitAllowingStateLoss();
                findViewById(R.id.Button_OpenTime).setBackgroundColor(Color.GRAY);
                findViewById(R.id.Button_Menu).setBackgroundColor(getResources().getColor(R.color.blue));
                findViewById(R.id.Button_OpenSchedule).setBackgroundColor(getResources().getColor(R.color.blue));
                break;

            case R.id.Button_Menu:
                transaction.replace(R.id.FrameLayout_SpecificInfo, fragmentMenu).commitAllowingStateLoss();
                findViewById(R.id.Button_Menu).setBackgroundColor(Color.GRAY);
                findViewById(R.id.Button_OpenSchedule).setBackgroundColor(getResources().getColor(R.color.blue));
                findViewById(R.id.Button_OpenTime).setBackgroundColor(getResources().getColor(R.color.blue));
                break;

            case R.id.Button_OpenSchedule:
                transaction.replace(R.id.FrameLayout_SpecificInfo, fragmentOpenschedule).commitAllowingStateLoss();
                findViewById(R.id.Button_OpenSchedule).setBackgroundColor(Color.GRAY);
                findViewById(R.id.Button_Menu).setBackgroundColor(getResources().getColor(R.color.blue));
                findViewById(R.id.Button_OpenTime).setBackgroundColor(getResources().getColor(R.color.blue));
                break;
        }

    }
}
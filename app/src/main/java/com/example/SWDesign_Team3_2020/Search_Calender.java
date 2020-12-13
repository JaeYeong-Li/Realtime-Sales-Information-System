package com.example.SWDesign_Team3_2020;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull ;
import android.widget.TextView;
import android.widget.CalendarView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;;

//for toolbar
import android.view.MenuItem;
import android.widget.Toast;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;
import android.content.Context;
import androidx.appcompat.widget.Toolbar;

//for searchStep, fragment
import android.content.Intent;
import android.view.View;

public class Search_Calender extends AppCompatActivity {
    public CalendarView calendarView;
    public TextView diaryTextView;
    private DrawerLayout mDrawerLayout;
    private Context context = this;
    private GlobalVar m_gvar = null;
    Boolean isLogin = false;
    MaterialCalendarView materialCalendarView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_calender);
        //use toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //globalVal
        m_gvar = (GlobalVar) getApplicationContext();
        if(m_gvar.isIDnull() == true) {
            isLogin = false;
        }else{
            isLogin=true;
        }

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
                    Intent intent = new Intent(getApplicationContext(), CurLocationActivity.class);
                    startActivity(intent);
                    finish();
                } else if (id == R.id.inputSearch) {
                    android.widget.Toast.makeText(context, "현재페이지", android.widget.Toast.LENGTH_SHORT).show();
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
                   Toast.makeText(context, title + ": 로그아웃 시도중", android.widget.Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });

        // calender;
       materialCalendarView = findViewById(R.id.search_calendar);

        int afterYear = com.prolificinteractive.materialcalendarview.CalendarDay.today().getYear();
        int afterMonth = com.prolificinteractive.materialcalendarview.CalendarDay.today().getMonth()+1;
        if(afterMonth==13) {
            afterMonth = 1;
            afterYear += 1;
        }
        int afterDay = com.prolificinteractive.materialcalendarview.CalendarDay.today().getDay();
        if(afterMonth==2 && afterDay>28) {
            if(afterYear%4==0)
                afterDay =29;
            else {
                afterDay = 28;
            }
        }else if((afterMonth==4||afterMonth==6||afterMonth==9||afterMonth==11)&&afterDay==31)
            afterDay=30;

        //materialCalendarView.setSelectedDate(CalendarDay.today());
        materialCalendarView.state().edit()
                .setMinimumDate(com.prolificinteractive.materialcalendarview.CalendarDay.from(com.prolificinteractive.materialcalendarview.CalendarDay.today().getYear(), com.prolificinteractive.materialcalendarview.CalendarDay.today().getMonth(), com.prolificinteractive.materialcalendarview.CalendarDay.today().getDay()))
                .setMaximumDate(com.prolificinteractive.materialcalendarview.CalendarDay.from(afterYear, afterMonth, afterDay))
                .setCalendarDisplayMode(com.prolificinteractive.materialcalendarview.CalendarMode.MONTHS)
                .commit();
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
        switch (view.getId())
        {
            case R.id.searchStep1:
                CalendarDay cd = materialCalendarView.getSelectedDate();
                int idx = cd.toString().indexOf("{");
                String temp = cd.toString().substring(idx);
                idx = temp.indexOf("}");
                temp = temp.substring(1,idx);

                Intent intent = new Intent(getApplicationContext(), Search_Location.class);
                intent.putExtra("selDate", temp);
                startActivity(intent);
                break;
        }
    }


}

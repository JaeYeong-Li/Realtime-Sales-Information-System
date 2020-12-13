package com.example.SWDesign_Team3_2020;

import androidx.appcompat.app.AppCompatActivity;
import android.widget.ListView;
import com.google.android.material.navigation.NavigationView;

//for toolbar
import android.view.MenuItem;
import android.widget.Toast;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.content.Context;
import androidx.appcompat.widget.Toolbar;

//for searchStep, fragment
import android.content.Intent;
import android.view.View;


public class Search_Result extends AppCompatActivity {


    private ListView listview;
    private SearchResultViewAdapter adapter;

    private DrawerLayout mDrawerLayout;
    private Context context = this;
    private GlobalVar m_gvar = null;
    Boolean isLogin = false;

    @Override
    protected void onCreate(@androidx.annotation.Nullable android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_result);

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
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(android.view.MenuItem menuItem) {
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();

                int id = menuItem.getItemId();
                String title = menuItem.getTitle().toString();

                if (id == R.id.curLocationSearch) {
                    Intent intent = new Intent(getApplicationContext(), CurLocationActivity.class);
                    startActivity(intent);
                    finish();
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
                    Toast.makeText(context, title + ": 로그아웃 시도중", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });

        //searchresult Adapter 생성
        adapter = new SearchResultViewAdapter();

        //리스트뷰 참조 및 Adapter 달기
        listview = (ListView) findViewById(R.id.searchresult_listview);
        listview.setAdapter(adapter);

        adapter.


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
               Toast.makeText(context, ": 클릭", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), Search_Location.class);
                startActivity(intent);
                finish();
                break;
        }
    }


}


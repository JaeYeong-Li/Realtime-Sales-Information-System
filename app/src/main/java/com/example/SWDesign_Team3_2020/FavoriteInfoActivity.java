package com.example.SWDesign_Team3_2020;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import java.util.ArrayList;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import androidx.drawerlayout.widget.DrawerLayout;
import android.database.sqlite.SQLiteDatabase;

public class FavoriteInfoActivity extends AppCompatActivity {

    public static ArrayList<SearchResultViewItem> mArrayList;
    public static SearchResultViewAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private DrawerLayout mDrawerLayout;
    private android.content.Context context = this;
    private GlobalVar m_gvar = null;
    DBHelper mydb1= CurLocationActivity.mydb1;
    Boolean isLogin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoriteinfo);

        //use toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //툴바에 홈버튼 활성화
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //툴바의 홈버튼의 이미지를 변경
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_navi_menu);

        //globalVal
        m_gvar = (GlobalVar) getApplicationContext();
        if(m_gvar.isIDnull() == true) {
            isLogin = false;
        }else{
            isLogin=true;
        }

        //drawyerLayout
        mDrawerLayout = (androidx.drawerlayout.widget.DrawerLayout) findViewById(R.id.drawer_layout);
        com.google.android.material.navigation.NavigationView navigationView = (com.google.android.material.navigation.NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(android.view.MenuItem menuItem) {
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();

                int id = menuItem.getItemId();
                String title = menuItem.getTitle().toString();

                if (id == R.id.curLocationSearch) {
                    android.content.Intent intent = new android.content.Intent(getApplicationContext(), CurLocationActivity.class);
                    startActivity(intent);
                    finish();
                } else if (id == R.id.inputSearch) {
                    android.content.Intent intent = new android.content.Intent(getApplicationContext(), Search_Calender.class);
                    startActivity(intent);
                    finish();
                } else if (id == R.id.mypage_toolbar) {
                    android.content.Intent intent;
                    if(isLogin == true){
                        intent = new android.content.Intent(getApplicationContext(), MyPage_member.class);
                    }else {
                        intent = new android.content.Intent(getApplicationContext(), MyPage_nonmember.class);
                    }
                    startActivity(intent);
                    finish();
                } else if (id == R.id.logout) {
                    android.widget.Toast.makeText(context, title + ": 로그아웃 시도중", android.widget.Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });

        //뿌리기
        mRecyclerView = (RecyclerView) findViewById(R.id.myfavorite_recyclerview);
        mRecyclerView.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(this));

        mArrayList = new ArrayList<>();
        mydb1.updateItems();
        mAdapter = new SearchResultViewAdapter(this, mArrayList);
        mRecyclerView.setAdapter(mAdapter);
    }

    public void clickHandler(android.view.View view) {
        android.content.Intent intent;
        switch (view.getId())
        {
            case R.id.myfavorite_delete:
                mydb1.deleteData();
                mydb1.updateItems();
                mRecyclerView.setAdapter(mAdapter);
                break;
        }
    }
}
package com.example.SWDesign_Team3_2020;


import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import android.content.Context;
import android.app.Activity;
import android.content.Intent;

public class MyPage_nonmember extends AppCompatActivity {

    public static Activity _MyPage_nonmember;
    private Context context = this;
    private DrawerLayout mDrawerLayout;

    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _MyPage_nonmember = MyPage_nonmember.this;
        setContentView(R.layout.mypage_nonmeber);

        //use toolbar
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //툴바에 홈버튼 활성화
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //툴바의 홈버튼의 이미지를 변경
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_navi_menu);

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
                    Intent intent = new android.content.Intent(getApplicationContext(), CurLocationActivity.class);
                    startActivity(intent);
                    finish();
                } else if (id == R.id.inputSearch) {
                    Intent intent = new Intent(getApplicationContext(), Search_Calender.class);
                    startActivity(intent);
                    finish();
                } else if (id == R.id.mypage_toolbar) {
                    android.widget.Toast.makeText(context, "현재페이지", android.widget.Toast.LENGTH_SHORT).show();
                } else if (id == R.id.logout) {
                    android.widget.Toast.makeText(context, title + ": 로그아웃 시도중", android.widget.Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ // 왼쪽 상단 버튼 눌렀을 때
                mDrawerLayout.openDrawer(androidx.core.view.GravityCompat.START);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void clickHandler(android.view.View view) {
        android.content.Intent intent;
        switch (view.getId())
        {
            case R.id.mytosignin:
      //          android.widget.Toast.makeText(context, ": 클릭", android.widget.Toast.LENGTH_SHORT).show();
                intent = new android.content.Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
                break;
            case R.id.mytologin:
        //        android.widget.Toast.makeText(context, ": 클릭", android.widget.Toast.LENGTH_SHORT).show();
                intent = new android.content.Intent(getApplicationContext(), LogInActivity.class);
                startActivity(intent);
                break;
            case R.id.mybutton2_nonmember:
     //           android.widget.Toast.makeText(context, ": 클릭", android.widget.Toast.LENGTH_SHORT).show();
                intent = new android.content.Intent(getApplicationContext(), FavoriteInfoActivity.class);
                startActivity(intent);
                break;
        }
    }
}

package com.example.SWDesign_Team3_2020;


import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import android.content.Context;

public class MyPage_nonmember extends AppCompatActivity {

    private Context context = this;
    private DrawerLayout mDrawerLayout;

    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
                    android.content.Intent intent = new android.content.Intent(getApplicationContext(), CurLocationActivity.class);
                    startActivity(intent);
                    finish();
                } else if (id == R.id.setting) {
                    android.content.Intent intent = new android.content.Intent(getApplicationContext(), Search_Calender.class);
                    startActivity(intent);
                } else if (id == R.id.mypage_toolbar) {
                    android.widget.Toast.makeText(context, title + "현재 페이지", android.widget.Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
    }

    public void clickHandler(android.view.View view) {
        switch (view.getId())
        {
            case R.id.mytosignin:
                android.widget.Toast.makeText(context, ": 클릭", android.widget.Toast.LENGTH_SHORT).show();
                android.content.Intent intent = new android.content.Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}

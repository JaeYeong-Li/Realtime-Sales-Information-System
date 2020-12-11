package com.example.SWDesign_Team3_2020;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.Intent;
import android.widget.TextView;
import android.content.Context;
import androidx.drawerlayout.widget.DrawerLayout;
import android.view.View;
import android.widget.Button;

public class EditStoreInfo extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private Context context = this;
    private TextView addressView;
    private TextView menuView;
    private Double lan;
    private Double lon;
    private Button regButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addressView = findViewById(R.id.editSI_address);
        menuView = findViewById(R.id.editSI_menu);
        regButton = findViewById(R.id.button_editstoreInfo);
        setContentView(R.layout.activity_editstoreinfo);

        //use toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
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
                    android.widget.Toast.makeText(context, title + ": 현재 페이지", android.widget.Toast.LENGTH_SHORT).show();
                } else if (id == R.id.logout) {
                    android.widget.Toast.makeText(context, title + ": 로그아웃 시도중", android.widget.Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
/*
        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = addressView.getText().toString();
                String menu = menuView.getText().toString();


                //com.example.SWDesign_Team3_2020.SignUpActivity.SignUp task = new com.example.SWDesign_Team3_2020.SignUpActivity.SignUp();
                //task.execute("http://" + IP_ADDRESS + "/SignUp.php", id,pw,name);


            }
        });*/
    }

    public void clickHandler(View view) {
        switch (view.getId())
        {
            case R.id.editSI_addressButton:
                android.widget.Toast.makeText(context, ": 클릭", android.widget.Toast.LENGTH_SHORT).show();
                Intent intent = new android.content.Intent(getApplicationContext(), EditStoreInfo_SearchLocation.class);
                startActivityForResult(intent,1);
                break;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        // 액티비티가 정상적으로 종료되었을 경우
        if (resultCode == RESULT_OK) {
            // requestCode==1 로 호출한 경우에만 처리.
            if (requestCode == 1) {
                addressView.setText(data.getStringExtra("address"));
                lon = data.getDoubleExtra("lon",0);
                lan = data.getDoubleExtra("lan",0);
            }
        }

    }
}

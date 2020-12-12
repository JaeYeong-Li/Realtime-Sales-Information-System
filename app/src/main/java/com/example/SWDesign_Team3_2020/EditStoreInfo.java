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
import android.widget.CheckBox;
import java.util.Calendar;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

public class EditStoreInfo extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private Context context = this;
    private TextView addressView;
    private TextView menuView;
    private Double lan;
    private Double lon;
    private CheckBox cbmon,cbtue,cbwed,cbthu,cbfri,cbsat,cbsun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addressView = findViewById(R.id.editSI_address);
        menuView = findViewById(R.id.editSI_menu);
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
        //monthly 플랜 설정
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH,+1);

        MaterialCalendarView materialCalendarView = findViewById(R.id.editSI_monthly);
        materialCalendarView.setOnDateChangedListener(this);
        materialCalendarView.setOnMonthChangedListener(this);
        materialCalendarView.setTopbarVisible(false);

*/


        //체크박스 설정
        cbmon = (CheckBox) findViewById(R.id.monday);
        cbtue = (CheckBox) findViewById(R.id.tuesday);
        cbwed = (CheckBox) findViewById(R.id.wednesday);
        cbthu = (CheckBox) findViewById(R.id.thursday);
        cbfri = (CheckBox) findViewById(R.id.friday);
        cbsat = (CheckBox) findViewById(R.id.saturday);
        cbsun = (CheckBox) findViewById(R.id.sunday);


        Button regButton = findViewById(R.id.button_editstoreInfo);

        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = addressView.getText().toString();
                String menu = menuView.getText().toString();

                //openDate
                String openDate = "";  // 결과를 출력할 문자열  ,  항상 스트링은 빈문자열로 초기화 하는 습관을 가지자
                if(cbmon.isChecked() == true){
                    openDate += "mon";
                }
                if(cbtue.isChecked() == true) {
                    openDate += "tue";
                }
                if(cbwed.isChecked() == true) {
                    openDate += "wed";
                }
                if(cbthu.isChecked() == true) {
                    openDate += "thu";
                }
                if(cbfri.isChecked() == true) {
                    openDate += "fri";
                }
                if(cbsat.isChecked() == true) {
                    openDate += "sat";
                }
                if(cbsun.isChecked() == true) {
                    openDate += "sun";
                }

                //com.example.SWDesign_Team3_2020.SignUpActivity.SignUp task = new com.example.SWDesign_Team3_2020.SignUpActivity.SignUp();
                //task.execute("http://" + IP_ADDRESS + "/SignUp.php", id,pw,name);


            }
        });
    }

    public void clickHandler(View view) {
        int clickid = view.getId();
        if (clickid ==R.id.editSI_addressButton) {
            android.widget.Toast.makeText(context, ": 클릭", android.widget.Toast.LENGTH_SHORT).show();
            Intent intent = new android.content.Intent(getApplicationContext(), EditStoreInfo_SearchLocation.class);
            startActivityForResult(intent, 1);
        }else if(clickid == R.id.monto|| clickid == R.id.monto|| clickid == R.id.monfrom|| clickid == R.id.tueto|| clickid ==R.id.tuefrom
                ||clickid == R.id.wedto|| clickid == R.id.wedfrom|| clickid == R.id.thuto|| clickid == R.id.thufrom||
                clickid == R.id.frito||clickid == R.id.frifrom||clickid == R.id.satto||clickid == R.id.satfrom||clickid == R.id.sunto|| clickid ==R.id.sunfrom){
            EditStoreInfo_monthly mEditStoreInfo_monthly = new EditStoreInfo_monthly();
            Bundle args_id = new Bundle();
            args_id.putString("id",Integer.toString(clickid));
            mEditStoreInfo_monthly.setArguments(args_id);
            mEditStoreInfo_monthly.show(getSupportFragmentManager(),"TimePicker");
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

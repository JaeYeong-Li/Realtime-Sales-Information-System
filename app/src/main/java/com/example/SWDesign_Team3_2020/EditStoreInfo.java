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
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import androidx.annotation.NonNull;
import android.provider.MediaStore;
import android.net.Uri;
import androidx.loader.content.CursorLoader;
import android.database.Cursor;
import android.widget.Switch;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.CompoundButton;
import android.os.AsyncTask;
import android.widget.EditText;

public class EditStoreInfo extends AppCompatActivity {

    //private static String IP_ADDRESS = "10.0.2.2";
    private static String IP_ADDRESS = "27.113.19.27";
    private static String TAG = "phptest";

    private DrawerLayout mDrawerLayout;
    private Context context = this;
    private EditText storenameView,menuView;
    private TextView addressView;
    private String lan,lon;
    private CheckBox cbmon,cbtue,cbwed,cbthu,cbfri,cbsat,cbsun;
    String selDates="";
    private static final int PICK_FROM_ALBUM = 2;
    String imgurl;
    Boolean specialcheck = false;

    private GlobalVar m_gvar = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editstoreinfo);
        storenameView = (EditText) findViewById(R.id.eidtSI_storeName);
        addressView = (TextView) findViewById(R.id.editSI_address);
        menuView = (EditText) findViewById(R.id.editSI_menu);

        //globalVal
        m_gvar = (GlobalVar) getApplicationContext();

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

        //monthly 플랜 설정
        MaterialCalendarView materialCalendarView = findViewById(R.id.editSI_monthly);

        int afterYear = CalendarDay.today().getYear();
        int afterMonth = CalendarDay.today().getMonth()+1;
        if(afterMonth==13) {
            afterMonth = 1;
            afterYear += 1;
        }
        int afterDay = CalendarDay.today().getDay();
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
                .setMinimumDate(CalendarDay.from(CalendarDay.today().getYear(), CalendarDay.today().getMonth(), CalendarDay.today().getDay()))
                .setMaximumDate(CalendarDay.from(afterYear, afterMonth, afterDay))
                .setCalendarDisplayMode(CalendarMode.MONTHS)
                .commit();

        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                //List<CalendarDay> selectedDates = materialCalendarView.getSelectedDates();
                int idx = date.toString().indexOf("{");
                String temp = date.toString().substring(idx);
                idx = temp.indexOf("}");
                temp = temp.substring(1,idx);
                selDates = selDates + temp + ";";

                android.widget.Toast.makeText(context, selDates, android.widget.Toast.LENGTH_SHORT).show();
            }
        });


        //체크박스 설정
        cbmon = (CheckBox) findViewById(R.id.monday);
        cbtue = (CheckBox) findViewById(R.id.tuesday);
        cbwed = (CheckBox) findViewById(R.id.wednesday);
        cbthu = (CheckBox) findViewById(R.id.thursday);
        cbfri = (CheckBox) findViewById(R.id.friday);
        cbsat = (CheckBox) findViewById(R.id.saturday);
        cbsun = (CheckBox) findViewById(R.id.sunday);

        //메뉴사진
        Button btmenupic = (Button) findViewById(R.id.editSI_menuPicture);
        btmenupic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_FROM_ALBUM);
            }
        });

        //금일 일정 변동 여부
        Switch sw = (Switch)findViewById(R.id.editSI_specialSwitch);

        //스위치의 체크 이벤트를 위한 리스너 등록

        sw.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                // TODO Auto-generated method stub
                android.widget.Toast.makeText(context, "체크상태 = " + isChecked, android.widget.Toast.LENGTH_SHORT).show();
                specialcheck = isChecked;
            }

        });


        Button regButton = findViewById(R.id.button_editstoreInfo);

        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = storenameView.getText().toString();
                String menu = menuView.getText().toString();
                TextView opentotv, openfromtv, specialtv;
                //openDate: selDates
                //openTime: openTime
                String openTime = "";  // 결과를 출력할 문자열  ,  항상 스트링은 빈문자열로 초기화 하는 습관을 가지자
                if(cbmon.isChecked() == true){
                    openTime += "mon";
                    opentotv = findViewById(R.id.monto);
                    openfromtv = findViewById(R.id.monfrom);
                    openTime += opentotv.getText();
                    openTime += openfromtv.getText();
                    openTime += ";";
                }
                if(cbtue.isChecked() == true) {
                    openTime += "tue";
                    opentotv = findViewById(R.id.tueto);
                    openfromtv = findViewById(R.id.tuefrom);
                    openTime += opentotv.getText();
                    openTime += openfromtv.getText();
                    openTime += ";";
                }
                if(cbwed.isChecked() == true) {
                    openTime += "wed";
                    opentotv = findViewById(R.id.wedto);
                    openfromtv = findViewById(R.id.wedfrom);
                    openTime += opentotv.getText();
                    openTime += openfromtv.getText();
                    openTime += ";";
                }
                if(cbthu.isChecked() == true) {
                    openTime += "thu";
                    opentotv = findViewById(R.id.thuto);
                    openfromtv = findViewById(R.id.thufrom);
                    openTime += opentotv.getText();
                    openTime += openfromtv.getText();
                    openTime += ";";
                }
                if(cbfri.isChecked() == true) {
                    openTime += "fri";
                    opentotv = findViewById(R.id.frito);
                    openfromtv = findViewById(R.id.frifrom);
                    openTime += opentotv.getText();
                    openTime += openfromtv.getText();
                    openTime += ";";
                }
                if(cbsat.isChecked() == true) {
                    openTime += "sat";
                    opentotv = findViewById(R.id.satto);
                    openfromtv = findViewById(R.id.satfrom);
                    openTime += opentotv.getText();
                    openTime += openfromtv.getText();
                    openTime += ";";
                }
                if(cbsun.isChecked() == true) {
                    openTime += "sun";
                    opentotv = findViewById(R.id.sunto);
                    openfromtv = findViewById(R.id.sunfrom);
                    openTime += opentotv.getText();
                    openTime += openfromtv.getText();
                    openTime += ";";
                }

                //ownerID: userid
                //specialBool: specialcheck
                //specialTime: specialtv
                specialtv = findViewById(R.id.editSI_specialTime);
                //photoUrl: imgurl

                UploadStoreInfo task = new UploadStoreInfo();
                task.execute("http://" + IP_ADDRESS + "/EditStoreInfo.php", name, lan, lon, menu, selDates,
                        openTime, m_gvar.getuserID(),specialcheck.toString(),specialtv.getText().toString(),imgurl);
            }
        });
    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };

        CursorLoader cursorLoader = new CursorLoader(this, contentUri, proj, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();

        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
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
                addressView = (TextView) findViewById(R.id.editSI_address);
                lon = data.getStringExtra("lon");
                lan = data.getStringExtra("lan");
                addressView.setText(data.getStringExtra("address"));
            }
        }else if(resultCode == PICK_FROM_ALBUM) {
            Uri uri = data.getData();
            //경로 구하기
            imgurl=getRealPathFromURI(uri);
            android.widget.Toast.makeText(context, imgurl, android.widget.Toast.LENGTH_SHORT).show();
        }

    }

    class UploadStoreInfo extends AsyncTask<String, Void, String> {
        android.app.ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = android.app.ProgressDialog.show(EditStoreInfo.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            //mTextViewResult.setText(result);
            android.util.Log.d(TAG, "POST response  - " + result);
        }


        @Override
        protected String doInBackground(String... params) {
/*
task.execute("http://" + IP_ADDRESS + "/EditStoreInfo.php", name, lan.toString(),
                        lon.toString(), menu, selDates, openTime, m_gvar.getuserID(),specialcheck.toString(),specialtv.getText().toString(),imgurl);
 */
            String d_name = (String)params[1];
            String d_lan = (String)params[2];
            String d_lon = (String)params[3];
            String d_menu = (String)params[4];
            String d_openDate = (String)params[5];
            String d_openTime = (String)params[6];
            String d_ownerID = (String)params[7];
            String d_specialBool = (String)params[8];
            String d_speciLTime = (String)params[9];
            String d_photoUrl1 = (String)params[10];

            String serverURL = (String)params[0];

            String postParameters = "storeName=" + d_name + "&lat=" + d_lan + "&lang=" + d_lon
                    + "&menu=" + d_menu + "&openDate=" + d_openDate + "&openTime" + d_openTime + "&ownerID=" + d_ownerID
                    + "&specialBool=" + d_specialBool + "&specialTime=" + d_speciLTime + "&photoUrl1=" + d_photoUrl1;


            try {

                java.net.URL url = new java.net.URL(serverURL);
                java.net.HttpURLConnection httpURLConnection = (java.net.HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.connect();


                java.io.OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();


                int responseStatusCode = httpURLConnection.getResponseCode();
                android.util.Log.d(TAG, "POST response code - " + responseStatusCode);

                java.io.InputStream inputStream;
                if(responseStatusCode == java.net.HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }


                java.io.InputStreamReader inputStreamReader = new java.io.InputStreamReader(inputStream, "UTF-8");
                java.io.BufferedReader bufferedReader = new java.io.BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line = null;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }


                bufferedReader.close();


                return sb.toString();


            } catch (Exception e) {

                android.util.Log.d(TAG, "InsertData: Error ", e);

                return new String("Error: " + e.getMessage());
            }

        }
    }
}

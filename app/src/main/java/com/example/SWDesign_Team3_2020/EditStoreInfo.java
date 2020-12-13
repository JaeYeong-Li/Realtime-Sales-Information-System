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
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.CalendarMode;
import android.provider.MediaStore;
import android.net.Uri;
import androidx.loader.content.CursorLoader;
import android.database.Cursor;
import android.widget.Switch;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.CompoundButton;
import android.os.AsyncTask;
import android.widget.EditText;
import java.util.List;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class EditStoreInfo extends AppCompatActivity {

    //private static String IP_ADDRESS = "10.0.2.2";
    private static String IP_ADDRESS = "27.113.19.27";
    private static String TAG = "phptest";
    private GlobalVar m_gvar = null;

    private DrawerLayout mDrawerLayout;
    private Context context = this;
    private EditText storenameView,menuView;
    private TextView addressView;
    private String lat,lan;
    String selDates="",imgurl;
    private static final int PICK_FROM_MAP = 1;
    private static final int PICK_FROM_ALBUM = 2;
    private RadioButton rc1,rc2,rc3;
    private RadioGroup rg_category;
    Boolean specialcheck = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editstoreinfo);
        storenameView = (EditText) findViewById(R.id.eidtSI_storeName);
        addressView = (TextView) findViewById(R.id.editSI_address);
        menuView = (EditText) findViewById(R.id.editSI_menu);
        rc1 = (RadioButton) findViewById(R.id.editSI_rbRest);
        rc2 = (RadioButton) findViewById(R.id.editSI_rbCaffe);
        rc3 = (RadioButton) findViewById(R.id.editSI_rbEnt);
        rg_category = (RadioGroup) findViewById(R.id.editSI_rgcategory);

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
                    Intent intent = new Intent(getApplicationContext(), CurLocationActivity.class);
                    startActivity(intent);
                    finish();
                } else if (id == R.id.inputSearch) {
                    Intent intent = new Intent(getApplicationContext(), Search_Calender.class);
                    startActivity(intent);
                    finish();
                } else if (id == R.id.mypage_toolbar) {
                    Intent intent = new Intent(getApplicationContext(), MyPage_member.class);
                    startActivity(intent);
                    finish();
                } else if (id == R.id.logout) {
                    android.widget.Toast.makeText(context, title + ": 로그아웃 시도중", android.widget.Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });

        //category


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
                //category: category
                int category = 1;
                if(rc1.isChecked()){
                    category = 1;
                }else if(rc2.isChecked()){
                    category = 2;
                }else if(rc3.isChecked()){
                    category = 3;
                }
                //openDate: selDates
                List<CalendarDay> selDatesList = materialCalendarView.getSelectedDates();
                selDates = "";
                for(CalendarDay cd : selDatesList){
                    int idx = cd.toString().indexOf("{");
                    String temp = cd.toString().substring(idx);
                    idx = temp.indexOf("}");
                    temp = temp.substring(1,idx);
                    selDates = selDates + temp + ";";
                }
                //openTime: openTime
                    //체크박스 설정
                CheckBox cbmon = (CheckBox) findViewById(R.id.monday);
                CheckBox cbtue = (CheckBox) findViewById(R.id.tuesday);
                CheckBox cbwed = (CheckBox) findViewById(R.id.wednesday);
                CheckBox cbthu = (CheckBox) findViewById(R.id.thursday);
                CheckBox cbfri = (CheckBox) findViewById(R.id.friday);
                CheckBox cbsat = (CheckBox) findViewById(R.id.saturday);
                CheckBox cbsun = (CheckBox) findViewById(R.id.sunday);

                String openTime = "";  // 결과를 출력할 문자열  ,  항상 스트링은 빈문자열로 초기화 하는 습관을 가지자
                if(cbmon.isChecked()){
                    openTime = openTime + "mon";
                    opentotv = (TextView)findViewById(R.id.monto);
                    openfromtv = (TextView)findViewById(R.id.monfrom);
                    openTime = openTime + opentotv.getText();
                    openTime = openTime + openfromtv.getText();
                    openTime = openTime +";";
                }
                if(cbtue.isChecked()) {
                    openTime = openTime + "tue";
                    opentotv = (TextView)findViewById(R.id.tueto);
                    openfromtv = (TextView)findViewById(R.id.tuefrom);
                    openTime = openTime +opentotv.getText();
                    openTime = openTime +openfromtv.getText();
                    openTime = openTime +";";
                }
                if(cbwed.isChecked()) {
                    openTime = openTime + "wed";
                    opentotv = (TextView)findViewById(R.id.wedto);
                    openfromtv = (TextView)findViewById(R.id.wedfrom);
                    openTime = openTime +opentotv.getText();
                    openTime = openTime + openfromtv.getText();
                    openTime = openTime +";";
                }
                if(cbthu.isChecked()) {
                    openTime = openTime + "thu";
                    opentotv = (TextView) findViewById(R.id.thuto);
                    openfromtv = (TextView) findViewById(R.id.thufrom);
                    openTime = openTime +opentotv.getText();
                    openTime = openTime +openfromtv.getText();
                    openTime = openTime +";";
                }
                if(cbfri.isChecked()) {
                    openTime = openTime + "fri";
                    opentotv = (TextView) findViewById(R.id.frito);
                    openfromtv = (TextView) findViewById(R.id.frifrom);
                    openTime = openTime +opentotv.getText();
                    openTime = openTime +openfromtv.getText();
                    openTime = openTime +";";
                }
                if(cbsat.isChecked()) {
                    openTime = openTime + "sat";
                    opentotv = (TextView) findViewById(R.id.satto);
                    openfromtv = (TextView) findViewById(R.id.satfrom);
                    openTime = openTime +opentotv.getText();
                    openTime = openTime +openfromtv.getText();
                    openTime = openTime +";";
                }
                if(cbsun.isChecked()) {
                    openTime = openTime + "sun";
                    opentotv = (TextView) findViewById(R.id.sunto);
                    openfromtv = (TextView) findViewById(R.id.sunfrom);
                    openTime = openTime +opentotv.getText();
                    openTime = openTime +openfromtv.getText();
                    openTime = openTime +";";
                }

                //ownerID: g_gvar.getuserID()
                //specialBool: specialcheck
                //specialTime: specialtv
                specialtv = findViewById(R.id.editSI_specialTime);
                //photoUrl: imgurl

                UploadStoreInfo task = new UploadStoreInfo();
                task.execute("http://" + IP_ADDRESS + "/EditStoreInfo.php", name, Integer.toString(category), lat, lan, addressView.getText().toString(), menu, selDates, openTime,
                        m_gvar.getuserID(), specialcheck.toString(),specialtv.getText().toString(),imgurl);

                finish();
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
            startActivityForResult(intent,PICK_FROM_MAP);
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

        switch(requestCode){
            case PICK_FROM_MAP:
                addressView = (TextView) findViewById(R.id.editSI_address);
                lat = data.getStringExtra("lat");
                lan = data.getStringExtra("lon");
                addressView.setText(data.getStringExtra("address"));
                break;
            case PICK_FROM_ALBUM:
                Uri uri = data.getData();
                //경로 구하기
                imgurl=getRealPathFromURI(uri);
                break;
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
            String d_name = (String)params[1];
            String d_category = (String)params[2];
            String d_lat = (String)params[3];
            String d_lan = (String)params[4];
            String d_address = (String)params[5];
            String d_menu = (String)params[6];
            String d_openDate = (String)params[7];
            String d_openTime = (String)params[8];
            String d_ownerID = (String)params[9];
            String d_specialBool = (String)params[10];
            String d_specialTime = (String)params[11];
            String d_photoUrl1 = (String)params[12];

            String serverURL = (String)params[0];

            String postParameters = "storeName=" + d_name + "&category=" + d_category + "&lat=" + d_lat + "&lang=" + d_lan + "&address=" + d_address
                    + "&menu=" + d_menu + "&openDate=" + d_openDate + "&openTime=" + d_openTime + "&ownerId=" + d_ownerID
                    + "&specialBool=" + d_specialBool + "&specialTime=" + d_specialTime + "&photoUrl1=" + d_photoUrl1;


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

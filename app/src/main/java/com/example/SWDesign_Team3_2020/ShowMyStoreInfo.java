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
import android.widget.Switch;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.CompoundButton;
import android.widget.EditText;
import java.util.List;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.util.Log;

public class ShowMyStoreInfo extends AppCompatActivity {

    //private static String IP_ADDRESS = "10.0.2.2";
    private static String IP_ADDRESS = "27.113.19.27";
    private static String TAG = "phptest";
    private GlobalVar m_gvar = null;

    private DrawerLayout mDrawerLayout;
    private Context context = this;
    private TextView addressView,storenameView,menuView,openTime;
    private RadioButton rc1,rc2,rc3;
    private RadioGroup rg_category;
    String mJsonString="";
    int category=1;
    Boolean alreadystore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mystoreinfo);
        storenameView = (TextView) findViewById(R.id.mySI_storeName);
        addressView = (TextView) findViewById(R.id.mySI_address);
        menuView = (TextView) findViewById(R.id.mySI_menu);
        rc1 = (RadioButton) findViewById(R.id.mySI_rbRest);
        rc2 = (RadioButton) findViewById(R.id.mySI_rbCaffe);
        rc3 = (RadioButton) findViewById(R.id.mySI_rbEnt);
        rg_category = (RadioGroup) findViewById(R.id.mySI_rgcategory);
        openTime = (TextView) findViewById(R.id.mySI_openTime);

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


        //check if this user register his store already
        checkstore task1 = new checkstore();
        task1.execute( "http://" + IP_ADDRESS + "/checkstore.php", m_gvar.getuserID());
    }

    private class checkstore extends android.os.AsyncTask<String, Void, String> {

        android.app.ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = android.app.ProgressDialog.show(ShowMyStoreInfo.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            Log.d(TAG, "response - " + result);

            if (result == null){
                android.widget.Toast.makeText(context, errorString, android.widget.Toast.LENGTH_SHORT).show();
            }
            else {
                mJsonString = result;
                int idx = mJsonString.indexOf("storeInfo");
                if(idx==-1){
                    Log.i("AlreadyStoreinfo?","no");
                    alreadystore=false;
                    android.widget.Toast.makeText(context, "기존가게없음. 등록페이지로", android.widget.Toast.LENGTH_SHORT).show();
                    Intent intent = new android.content.Intent(getApplicationContext(), EditStoreInfo.class);
                    startActivity(intent);
                    finish();
                }else{
                    Log.i("AlreadyStoreinfo?","yes");
                    alreadystore=true;
                    arrangeResult();
                }
            }
        }


        @Override
        protected String doInBackground(String... params) {

            String serverURL = params[0];
            String postParameters = "ownerId=" + params[1];

            try {

                java.net.URL url = new java.net.URL(serverURL);
                java.net.HttpURLConnection httpURLConnection = (java.net.HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();

                java.io.OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();

                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);

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
                String line;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }

                bufferedReader.close();

                return sb.toString().trim();


            } catch (Exception e) {

                Log.d(TAG, "InsertData: Error ", e);
                errorString = e.toString();

                return null;
            }

        }
    }

    private void arrangeResult() {
        String TAG_STOREID = "storeId";
        String TAG_STORENAME = "storeName";
        String TAG_CATEGORY = "category";
        String TAG_ADDRESS = "address";
        String TAG_MENU = "menu";
        String TAG_OPENTIME = "openTime";

        try {
            int idx = mJsonString.indexOf("[");
            mJsonString = mJsonString.substring(idx);
            mJsonString.trim();

            Log.d("MyApp", mJsonString);
            org.json.JSONArray jsonArray = new org.json.JSONArray(mJsonString);

            for (int i = 0; i < jsonArray.length(); i++) {
                Log.d(TAG, "start arrange result");
                org.json.JSONObject item = jsonArray.getJSONObject(i);

                storenameView.setText(item.getString(TAG_STORENAME));
                addressView.setText(item.getString(TAG_ADDRESS));
                category = Integer.parseInt(item.getString(TAG_CATEGORY));
                if(category==1){
                    rc1.setChecked(true);
                }else if(category==2){
                    rc2.setChecked(true);
                }else if(category==3){
                    rc3.setChecked(true);
                }
                menuView.setText(item.getString(TAG_MENU));
                openTime.setText(item.getString(TAG_OPENTIME));
            }
            Log.d(TAG, "finish arrange result");

        } catch (org.json.JSONException e) {

            Log.d(TAG, "showResult : ", e);
        }
    }

    public void clickHandler(android.view.View view) {
        switch (view.getId())
        {
            case R.id.mytoedit:
                android.widget.Toast.makeText(context, ": 클릭", android.widget.Toast.LENGTH_SHORT).show();
                Intent intent = new android.content.Intent(getApplicationContext(), EditStoreInfo.class);
                startActivity(intent);
                finish();
                break;
        }
    }

}

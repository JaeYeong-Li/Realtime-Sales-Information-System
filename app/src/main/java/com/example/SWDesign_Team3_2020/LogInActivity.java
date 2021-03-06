package com.example.SWDesign_Team3_2020;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.content.Context;
import androidx.drawerlayout.widget.DrawerLayout;
import android.widget.EditText;
import android.widget.Button;
import android.view.View;

import org.json.JSONObject;
import android.os.AsyncTask;
import android.app.ProgressDialog;
import android.util.Log;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import org.json.JSONArray;
import org.json.JSONException;
import java.util.HashMap;
import android.content.Intent;

public class LogInActivity extends AppCompatActivity {

    private static String TAG = "phpquerytest";
    //private static String IP_ADDRESS = "10.0.2.2"; 에뮬레이터용
    private static String IP_ADDRESS = "27.113.19.27";

    private static final String TAG_JSON="storeInfo";
    private static final String TAG_ID = "id";
    private static final String TAG_PW = "pw";
    private static final String TAG_NAME = "name";

    private DrawerLayout mDrawerLayout;
    private Context context = this;

    private EditText EditTextID;
    private EditText EditTextPW;
    String mJsonString;

    private GlobalVar m_gvar = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
                    Intent intent = new android.content.Intent(getApplicationContext(), CurLocationActivity.class);
                    startActivity(intent);
                    finish();
                } else if (id == R.id.inputSearch) {
                    Intent intent = new Intent(getApplicationContext(), Search_Calender.class);
                    startActivity(intent);
                    finish();
                } else if (id == R.id.mypage_toolbar) {
                    Intent intent = new Intent(getApplicationContext(), MyPage_nonmember.class);
                    startActivity(intent);
                    finish();
                } else if (id == R.id.logout) {
                    android.widget.Toast.makeText(context, title + "로그인한 유저가 아닙니다.", android.widget.Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });

        EditTextID = (EditText)findViewById(R.id.loginText_id);
        EditTextPW = (EditText)findViewById(R.id.loginText_pw);

        Button buttonInsert = (Button)findViewById(R.id.button_login);
        buttonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                GetData task = new GetData();
                String id = EditTextID.getText().toString();
                String pw = EditTextPW.getText().toString();
                task.execute( "http://" + IP_ADDRESS + "/login.php", id,pw);
                MyPage_nonmember MN = (MyPage_nonmember) MyPage_nonmember._MyPage_nonmember;
                MN.finish();
                finish();
            }
        });
    }

    private class GetData extends AsyncTask<String, Void, String>{

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(LogInActivity.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            //mTextViewResult.setText(result);
            Log.d(TAG, "response - " + result);

            if (result == null){

                //mTextViewResult.setText(errorString);
            }
            else {

                mJsonString = result;
                showResult();
            }
        }


        @Override
        protected String doInBackground(String... params) {

            String searchKeyword1 = params[1];
            String searchKeyword2 = params[2];
            String serverURL = (String)params[0];

            String postParameters = "id=" + searchKeyword1 + "&pw=" + searchKeyword2;


            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();


                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }


                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

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

    private void showResult(){
        try {
            int idx = mJsonString.indexOf("[");
            if(idx==-1){
                android.widget.Toast.makeText(context, "존재하지 않은 회원이나 잘못된 id, pw 입력하셨습니다", android.widget.Toast.LENGTH_SHORT).show();
                finish();
                android.content.Intent intent = new android.content.Intent(getApplicationContext(), LogInActivity.class);
                startActivity(intent);
            }else {
                mJsonString = mJsonString.substring(idx);
                mJsonString.trim();

                Log.d("MyApp", mJsonString);
                JSONArray jsonArray = new JSONArray(mJsonString);

                JSONObject item = jsonArray.getJSONObject(0);
                String id = item.getString(TAG_ID);
                String pw = item.getString(TAG_PW);
                String name = item.getString(TAG_NAME);

                m_gvar.setUserID(id);
                m_gvar.setUserPW(pw);
                m_gvar.setUserNAME(name);

                android.widget.Toast.makeText(context, "환영합니다" + name + "님", android.widget.Toast.LENGTH_SHORT).show();

                android.content.Intent intent = new android.content.Intent(getApplicationContext(), MyPage_member.class);
                startActivity(intent);
            }

        } catch (JSONException e) {

            Log.d(TAG, "showResult : ", e);
        }

    }


}
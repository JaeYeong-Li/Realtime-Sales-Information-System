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

public class LogInActivity extends AppCompatActivity {

    private static String TAG = "phpquerytest";
    private static String IP_ADDRESS = "10.0.2.2";

    private static final String TAG_JSON="storeInfo";
    private static final String TAG_ID = "id";
    private static final String TAG_PW = "pw";
    private static final String TAG_NAME = "name";

    private DrawerLayout mDrawerLayout;
    private Context context = this;

    private EditText EditTextID;
    private EditText EditTextPW;
    String mJsonString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
                    android.content.Intent intent = new android.content.Intent(getApplicationContext(), Search_Calender.class);
                    startActivity(intent);
                } else if (id == R.id.mypage_toolbar) {
                    android.widget.Toast.makeText(context, title + "현재 페이지", android.widget.Toast.LENGTH_SHORT).show();
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

    private void showResult(){
        try {
            int idx = mJsonString.indexOf("[");
            mJsonString = mJsonString.substring(idx);
            mJsonString.trim();

            Log.d("MyApp",mJsonString);
            JSONArray jsonArray = new JSONArray(mJsonString);

            JSONObject item = jsonArray.getJSONObject(0);
            String id = item.getString(TAG_ID);
            String pw = item.getString(TAG_PW);
            String name = item.getString(TAG_NAME);

            android.widget.Toast.makeText(context, "환영합니다"+name+"님", android.widget.Toast.LENGTH_SHORT).show();
            System.out.println(name);

        } catch (JSONException e) {

            Log.d(TAG, "showResult : ", e);
        }

    }


}
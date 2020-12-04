package com.example.SWDesign_Team3_2020;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.content.Context;
import androidx.drawerlayout.widget.DrawerLayout;
import android.widget.EditText;
import android.widget.Button;
import android.view.View;

//import android.widget.TextView;
import android.os.AsyncTask;
import android.app.ProgressDialog;
import android.util.Log;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;

public class SignUpActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private Context context = this;

    private static String IP_ADDRESS = "10.0.2.2";
    private static String TAG = "phptest";

    private EditText EditTextID;
    private EditText EditTextPW;
    private EditText EditTextNAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

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

        EditTextID = (EditText)findViewById(R.id.signupText_id);
        EditTextPW = (EditText)findViewById(R.id.signupText_pw);
        EditTextNAME = (EditText)findViewById(R.id.signupText_name);

        Button buttonInsert = (Button)findViewById(R.id.button_signup);
        buttonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String id = EditTextID.getText().toString();
                String pw = EditTextPW.getText().toString();
                String name = EditTextNAME.getText().toString();

                SignUp task = new SignUp();
                task.execute("http://" + IP_ADDRESS + "/SignUp.php", id,pw,name);

                EditTextID.setText("");
                EditTextPW.setText("");
                EditTextNAME.setText("");

            }
        });
    }

    class SignUp extends AsyncTask<String, Void, String>{
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(SignUpActivity.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            //mTextViewResult.setText(result);
            Log.d(TAG, "POST response  - " + result);
        }


        @Override
        protected String doInBackground(String... params) {

            String id = (String)params[1];
            String pw = (String)params[2];
            String name = (String)params[3];

            String serverURL = (String)params[0];
            String postParameters = "id=" + id + "&pw=" + pw + "&name=" + name;


            try {

                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();


                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.connect();


                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(postParameters.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();


                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "POST response code - " + responseStatusCode);

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
                String line = null;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }


                bufferedReader.close();


                return sb.toString();


            } catch (Exception e) {

                Log.d(TAG, "InsertData: Error ", e);

                return new String("Error: " + e.getMessage());
            }

        }
    }


}

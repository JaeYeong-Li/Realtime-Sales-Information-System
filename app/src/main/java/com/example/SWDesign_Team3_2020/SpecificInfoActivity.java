package com.example.SWDesign_Team3_2020;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.util.Log;

import com.google.android.material.navigation.NavigationView;

public class SpecificInfoActivity extends AppCompatActivity {
    //private static String IP_ADDRESS = "10.0.2.2";
    private static String IP_ADDRESS = "27.113.19.27";

    private DrawerLayout mDrawerLayout;
    private Context context = this;

    public FragmentManager fragmentManager;
    public SpecificInfoFragment_OpenTime fragmentOpentime;
    public SpecificInfoFragment_Menu fragmentMenu;
    public SpecificInfoFragment_OpenSchedule fragmentOpenschedule;

    public FragmentTransaction transaction;

    private Button Button_Opentime;
    private Button Button_Menu;
    private Button Button_Openschedule;

    private LinearLayout Button_Prefer;
    private boolean preferedStore = false;

    private ImageView ImageView_Heart;
    private TextView addFvorite;

    private GlobalVar m_gvar = null;
    Boolean isLogin = false;

    DBHelper myfavorite;
    String StoreName,StoreId,StoreAddress;
    String mJsonString;


    TextView TextView_StoreName,TextView_Address;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_info);
        Intent intent_name = getIntent();

        StoreName = intent_name.getStringExtra("storeName");
        myfavorite = CurLocationActivity.mydb1;

        //globalVal
        m_gvar = (GlobalVar) getApplicationContext();
        if(m_gvar.isIDnull() == true) {
            isLogin = false;
        }else{
            isLogin=true;
        }

        //initialize view
        Button_Opentime = findViewById(R.id.Button_OpenTime);
        Button_Menu = findViewById(R.id.Button_Menu);
        Button_Openschedule = findViewById(R.id.Button_OpenSchedule);
        Button_Opentime.setBackgroundColor(Color.GRAY);

        Button_Prefer = findViewById(R.id.Button_Prefer);

        ImageView_Heart = findViewById(R.id.ImageView_Heart);

        TextView_StoreName = findViewById(R.id.TextView_Storename);
        TextView_StoreName.setText(StoreName);
        TextView_Address = findViewById(R.id.TextView_Address);

        addFvorite = findViewById(R.id.addFavorite);



        //set on click listener for linear layout button
        //버튼 클릭 이벤트(리니어 레이아웃 - 선호 버튼)

        //DB찾아보고 있는 거면 preferedStore = true
        // 사진 진한 하트로 바꾸기
        if (preferedStore == false)
        {
            ImageView_Heart.setImageResource(com.example.SWDesign_Team3_2020.R.drawable.ic_baseline_add_24);

        }
        else
        {
            //DB찾아보고 없는 거면 preferedStore = false
            ImageView_Heart.setImageResource(com.example.SWDesign_Team3_2020.R.drawable.ic_baseline_check_24);


        }




        Button_Prefer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //preferedStore이 false면
                // -> "선호가게로 등록되었습니다" 하고 선호 DB에 저장, 진한 하트로 바꿔, preferedStore 바꿔
                if (preferedStore==false) {
                    Toast.makeText(context, "Bookmarked as a preferred store", Toast.LENGTH_SHORT).show();
                    preferedStore = true;
                    ImageView_Heart.setImageResource(com.example.SWDesign_Team3_2020.R.drawable.ic_baseline_check_24);
                }
                //preferedStore이 true면
                //-> 선호가게에서 삭제했습니다" 하고 선호 DB에서 삭제, 연한 하트로 바꿔, preferedStore 바뀨ㅓ
                else
                {
                    Toast.makeText(context, "Deleted from the preferred store list", Toast.LENGTH_SHORT).show();
                    preferedStore = false;
                    ImageView_Heart.setImageResource(com.example.SWDesign_Team3_2020.R.drawable.ic_baseline_add_24);

                }
            }
        });
        //use toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //툴바에 홈버튼 활성화
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //툴바의 홈버튼의 이미지를 변경
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_navi_menu);

        //drawyerLayout
        mDrawerLayout = (androidx.drawerlayout.widget.DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();

                int id = menuItem.getItemId();
                String title = menuItem.getTitle().toString();

                if (id == R.id.curLocationSearch) {
                    Toast.makeText(context, title + "현재 페이지", Toast.LENGTH_SHORT).show();
                } else if (id == R.id.inputSearch) {
                    Intent intent;
                    if(isLogin = true){
                        intent = new Intent(getApplicationContext(), MyPage_member.class);
                    }else {
                        intent = new Intent(getApplicationContext(), MyPage_nonmember.class);
                    }
                    startActivity(intent);
                    finish();
                } else if (id == R.id.mypage_toolbar) {
                    Intent intent = new Intent(getApplicationContext(), MyPage_nonmember.class);
                    startActivity(intent);
                }
                return true;
            }
        });


        //fragment handling
        fragmentManager = getSupportFragmentManager();

        fragmentOpentime = new SpecificInfoFragment_OpenTime();
        fragmentMenu = new SpecificInfoFragment_Menu();
        fragmentOpenschedule = new SpecificInfoFragment_OpenSchedule();

        transaction = fragmentManager.beginTransaction();

        GetData task = new GetData();
        Log.i("storeName",StoreName);
        task.execute( "http://" + IP_ADDRESS + "/checkstore2.php", StoreName);


    }
     //버튼 클릭 이벤트(프래그먼트)
    public void clickHandler(View view) {

        //Toast.makeText(context, "BtnClikced", Toast.LENGTH_SHORT).show();

        transaction = fragmentManager.beginTransaction();

        switch (view.getId()) {
            case R.id.Button_OpenTime:
                transaction.replace(R.id.FrameLayout_SpecificInfo, fragmentOpentime).commitAllowingStateLoss();
                findViewById(R.id.Button_OpenTime).setBackgroundColor(Color.GRAY);
                findViewById(R.id.Button_Menu).setBackgroundColor(getResources().getColor(R.color.blue));
                findViewById(R.id.Button_OpenSchedule).setBackgroundColor(getResources().getColor(R.color.blue));
                break;

            case R.id.Button_Menu:
                transaction.replace(R.id.FrameLayout_SpecificInfo, fragmentMenu).commitAllowingStateLoss();
                findViewById(R.id.Button_Menu).setBackgroundColor(Color.GRAY);
                findViewById(R.id.Button_OpenSchedule).setBackgroundColor(getResources().getColor(R.color.blue));
                findViewById(R.id.Button_OpenTime).setBackgroundColor(getResources().getColor(R.color.blue));
                break;

            case R.id.Button_OpenSchedule:
                transaction.replace(R.id.FrameLayout_SpecificInfo, fragmentOpenschedule).commitAllowingStateLoss();
                findViewById(R.id.Button_OpenSchedule).setBackgroundColor(Color.GRAY);
                findViewById(R.id.Button_Menu).setBackgroundColor(getResources().getColor(R.color.blue));
                findViewById(R.id.Button_OpenTime).setBackgroundColor(getResources().getColor(R.color.blue));
                break;
            case R.id.addFavorite:
                boolean isInserted = myfavorite.insertData(StoreId,StoreName);
                if (isInserted == true) {
                    //myfavorite.updateItems();
                    //FavoriteInfoActivity.mAdapter.notifyDataSetChanged();
                    Toast.makeText(this, "등록성공",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "등록실패",
                            Toast.LENGTH_LONG).show();
                }
        }

    }


    private class GetData extends android.os.AsyncTask<String, Void, String> {

        android.app.ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = android.app.ProgressDialog.show(SpecificInfoActivity.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            Log.d("si:getData", "response - " + result);

            if (result == null) {
                //Toast.makeText(context, errorString, Toast.LENGTH_SHORT).show();
            } else {
                mJsonString = result;
                arrangeResult();
            }
        }


        @Override
        protected String doInBackground(String... params) {

            String serverURL = params[0];
            String postParameters = "storeName=" + params[1];

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
                Log.d("si:getData", "response code - " + responseStatusCode);

                java.io.InputStream inputStream;
                if (responseStatusCode == java.net.HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    inputStream = httpURLConnection.getErrorStream();
                }

                java.io.InputStreamReader inputStreamReader = new java.io.InputStreamReader(inputStream, "UTF-8");
                java.io.BufferedReader bufferedReader = new java.io.BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }

                bufferedReader.close();

                return sb.toString().trim();


            } catch (Exception e) {

                Log.d("si:getData", "InsertData: Error ", e);
                errorString = e.toString();

                return null;
            }

        }
    }

    private void arrangeResult() {
        String TAG_STOREID = "storeId";
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
                org.json.JSONObject item = jsonArray.getJSONObject(i);
                StoreId = item.getString(TAG_STOREID);
                StoreAddress = item.getString(TAG_ADDRESS);
                TextView_Address.setText(StoreAddress);
                m_gvar.setselectedMenu(item.getString(TAG_MENU));
                m_gvar.setselectedOpenTime(item.getString(TAG_OPENTIME));
            }
            Log.d("si:getData", "finish arrange result");

        } catch (org.json.JSONException e) {

            Log.d("si:getData", "showResult : ", e);
        }

        transaction.replace(R.id.FrameLayout_SpecificInfo, fragmentOpentime).commitAllowingStateLoss();
    }
}
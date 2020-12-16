package com.example.SWDesign_Team3_2020;

import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

import com.example.SWDesign_Team3_2020.R.id;
import com.google.android.material.navigation.NavigationView;

//for toolbar
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.content.Context;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import com.google.android.gms.maps.model.LatLng;
import android.os.AsyncTask;
import android.app.ProgressDialog;
import android.util.Log;
import java.net.URL;
import java.net.HttpURLConnection;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import org.json.JSONObject;
import org.json.JSONArray;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.fragment.app.FragmentManager;
import android.app.ProgressDialog;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class Search_Result extends AppCompatActivity {

    //private static String IP_ADDRESS = "10.0.2.2";
    private static String IP_ADDRESS = "27.113.19.27";
    private static String TAG = "phpexample";

    private ArrayList<SearchResultViewItem> mArrayList;
    private SearchResultViewAdapter mAdapter;
    private RecyclerView mRecyclerView;

    private FragmentManager fragmentManager;
    private Fragment searchResultMapFragment;
    private FragmentTransaction fragmentTransaction;

    private DrawerLayout mDrawerLayout;
    private Context context = this;
    private GlobalVar m_gvar = null;
    Boolean isLogin = false;
    String selDate, sellat, sellon;
    String mJsonString;
    LatLng myLocation;


    @Override
    protected void onCreate(@androidx.annotation.Nullable android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_result);

        //fragment
        searchResultMapFragment = new SearchResultMapFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.FrameLayout_SearchResult,searchResultMapFragment).commit();



        Intent intent = getIntent();
        selDate = intent.getStringExtra("selDate");
        sellat = intent.getStringExtra("lat");
        sellon = intent.getStringExtra("lon");
        myLocation = new LatLng(Double.parseDouble(sellat), Double.parseDouble(sellon));

        //globalVal
        m_gvar = (GlobalVar) getApplicationContext();
        if(m_gvar.isIDnull() == true) {
            isLogin = false;
        }else{
            isLogin=true;
        }

        //use toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //툴바에 홈버튼 활성화
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //툴바의 홈버튼의 이미지를 변경
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_navi_menu);

        //drawyerLayout
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
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
                    Intent intent;
                    if(isLogin == true){
                        intent = new Intent(getApplicationContext(), MyPage_member.class);
                    }else {
                        intent = new Intent(getApplicationContext(), MyPage_nonmember.class);
                    }
                    startActivity(intent);
                    finish();
                } else if (id == R.id.logout) {
                    Toast.makeText(context, title + ": 로그아웃 시도중", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });

        //리사이클러뷰 정리
        mRecyclerView = (RecyclerView) findViewById(R.id.searchresult_recyclerview);
        mRecyclerView.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(this));

        mArrayList = new ArrayList<>();
        mAdapter = new SearchResultViewAdapter(this, mArrayList);
        mRecyclerView.setAdapter(mAdapter);

        mArrayList.clear();
        mAdapter.notifyDataSetChanged();

        String Keyword = selDate;

        GetData task = new GetData();
        task.execute( "http://" + IP_ADDRESS + "/showSearchResult.php", Keyword);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ // 왼쪽 상단 버튼 눌렀을 때
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isitClose(LatLng storeLocation, LatLng MyLocation) {
        //현위치에서 1km 이내 가게인지 보여줌.

        double EARTH_R, Rad, radLat1, radLat2, radDist;
        double distance, ret;

        EARTH_R = 6371000.0;
        Rad = Math.PI / 180;
        radLat1 = Rad * storeLocation.latitude;
        radLat2 = Rad * MyLocation.latitude;
        radDist = Rad * (storeLocation.longitude - MyLocation.longitude);

        distance = Math.sin(radLat1) * Math.sin(radLat2);
        distance = distance + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.cos(radDist);
        ret = EARTH_R * Math.acos(distance);

        double rslt = ret / 1000;
        // km로 변환 및 반올림

        Log.i("거리", rslt + "km");
        if (rslt <= 1.0) {
            return true;
        } else
            return false;
    }

    private boolean isitOpen(String openDate, String openTime, String selectedDate){
        Date sd;
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
        try {
            sd = sdf.parse(selectedDate);
        }catch (Exception e) {
            sd = new Date();
        }
        Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(sd);

        int sd_nday = cal.get(Calendar.DAY_OF_WEEK);
        String sd_day = "";
        switch(sd_nday) {
            case 1: sd_day="sun"; break;
            case 2: sd_day="mon"; break;
            case 3: sd_day="tue"; break;
            case 4: sd_day="wed"; break;
            case 5: sd_day="thu"; break;
            case 6: sd_day="fri"; break;
            case 7: sd_day="sat"; break;
        }

        String ot = openTime;
        String od = openDate;
        do{
            int idx1 = ot.indexOf(";");
            if (idx1 == -1)
                break;
            String ot_temp = ot.substring(idx1+1);
            ot = ot.substring(0, 3);
            Log.i("openTime요일", ot);

            if (ot.equals(sd_day)) {
                while(true) {
                    int idx2 = od.indexOf(";");
                    if (idx2 != -1) {
                        //"123;"일때idx는3,length는4
                        String od_temp="";
                        if(idx2-1!=od.length())
                            od_temp = od.substring(idx2+1);
                        od = od.substring(0, idx2);
                        Log.i("od_temp:", od_temp);
                        if(od.equals(selectedDate))
                            return false;
                        if(od_temp=="") { break; }
                        else { od = od_temp; }
                    }else { break; }
                }
                return true;
            }
            ot = ot_temp;
        }while(true);
        return false;
    }

    private class GetData extends AsyncTask<String, Void, String>{

        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(Search_Result.this,
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            Log.d(TAG, "response - " + result);

            if (result == null){
                Toast.makeText(context, errorString, Toast.LENGTH_SHORT).show();
            }
            else {
                mJsonString = result;
                Toast.makeText(context, "start arrangeResult", Toast.LENGTH_SHORT).show();
                arrangeResult();
            }
        }


        @Override
        protected String doInBackground(String... params) {

            String serverURL = params[0];
            String postParameters = "country=" + params[1];

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

    private void arrangeResult() {
        String TAG_STOREID = "storeId";
        String TAG_STORENAME = "storeName";
        String TAG_CATEGORY = "category";
        String TAG_LAT = "lat";
        String TAG_LANG = "lang";
        String TAG_ADDRESS = "address";
        String TAG_OPENDATE = "openDate";
        String TAG_OPENTIME = "openTime";

        try {
            int idx = mJsonString.indexOf("[");
            mJsonString = mJsonString.substring(idx);
            mJsonString.trim();

            Log.d("MyApp", mJsonString);
            JSONArray jsonArray = new JSONArray(mJsonString);

            for (int i = 0; i < jsonArray.length(); i++) {
                Log.d(TAG, "start arrange result");
                JSONObject item = jsonArray.getJSONObject(i);

                String lat = item.getString(TAG_LAT);
                String lang = item.getString(TAG_LANG);
                LatLng storeLocation = new LatLng(Double.parseDouble(lat), Double.parseDouble(lang));

                String openDate = item.getString(TAG_OPENDATE);
                String openTime = item.getString(TAG_OPENTIME);

                //boolean isitClose(LatLng storeLocation, LatLng MyLocation)
                if (isitClose(storeLocation, myLocation) == true && isitOpen(openDate, openTime, selDate)==true) {


                    String storeId = item.getString(TAG_STOREID);
                    String storeName = item.getString(TAG_STORENAME);
                    String category = item.getString(TAG_CATEGORY);
                    String address = item.getString(TAG_ADDRESS);

                    SearchResultViewItem storeData = new SearchResultViewItem(storeId, storeName, category, lat, lang, address, openDate, openTime);

                    mArrayList.add(storeData);
                    Log.d("어레이 들어감??",mArrayList.get(0).getStoreName());
                    mAdapter.notifyDataSetChanged();
                }
            }
            Log.d(TAG, "finish arrange result");



            Bundle bundle = new Bundle();
            //미리 갯수 알려주긔
            int arrLen = mArrayList.size();
            System.out.println("size"+ String.valueOf(arrLen));
            bundle.putInt("mArrayListSize",arrLen);

            //검색 결과를 프래그먼트로 전달 -> 각 record마다 storeId를 SearchResultMapFragment로 전달
            for (int r = 0;r<arrLen;r++)
            {
                System.out.println("번들 싸는 중");
                String tag = String.valueOf(r);
                String found = mArrayList.get(r).getStoreId();
               bundle.putString(tag,found);
            }
            bundle.putString("test","test");
            searchResultMapFragment.setArguments(bundle);
            final int commit = getSupportFragmentManager().beginTransaction().replace(com.example.SWDesign_Team3_2020.R.id.FrameLayout_SearchResult,searchResultMapFragment).commit();


        } catch (org.json.JSONException e) {

         //   Log.d(TAG, "showResult : ", e);
        }
    }

}


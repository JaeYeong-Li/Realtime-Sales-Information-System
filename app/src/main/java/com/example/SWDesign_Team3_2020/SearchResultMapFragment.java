package com.example.SWDesign_Team3_2020;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.Objects;
import java.text.SimpleDateFormat;
import java.util.Date;

import static java.lang.Double.parseDouble;
import static java.lang.Float.parseFloat;
import static java.lang.Integer.parseInt;
import static java.lang.String.*;

public class SearchResultMapFragment extends Fragment implements GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMarkerClickListener {

    //private static String IP_ADDRESS = "10.0.2.2";
    private static String IP_ADDRESS = "27.113.19.27";
    private static String TAG = "phpexample";

    private LatLng selected;
    private Double selectedLat;
    private Double selectedLoc;

    LocationManager manager;
    GoogleMap map;

    //  MarkerOptions storeLocationMarker;
    MarkerOptions myLocationMarker;
    Marker myMarker;

    int storeId;
    int markerIndex = 1;
    String mJsonString;
    private LatLng curPoint;

    int flag = 0;
    String id;
    String StoreName;
    private java.util.ArrayList<SearchResultViewItem> mArrayList;
    private SearchResultViewAdapter mAdapter;

    //목표: searchResultMapFragment에서 받은 id를 마커에 띄우기.

    HashMap<Marker, Integer> markerHashMap = new HashMap<Marker, Integer>();
    HashMap<Integer,String> markerHashMap2 = new java.util.HashMap<Integer, String>();

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap googleMap) {
            manager = (LocationManager) Objects.requireNonNull(getContext()).getSystemService(getContext().LOCATION_SERVICE);
            map = googleMap;


        }


    };

    @Override

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if (getArguments() != null) {

            String test1 = getArguments().getString("test");

//            Log.e("테스트", test1);


        } else {

            Log.e("없덩", "null");
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_search_result_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }



        //리스크 사이즈
        //     int mArrayListSize = getArguments().getInt("mArrayListSize");
        String storeId_;
//        Toast.makeText(getContext(),getArguments().getString("test"), android.widget.Toast.LENGTH_LONG).show();
        for (int j = 0; j < 13; j++) {
//            storeId_ = getArguments().getString(String.valueOf(j));
            storeId_ = Integer.toString(j);

            GetData task = new GetData();
            task.execute("http://" + IP_ADDRESS + "/checkstore2.php", storeId_);


        }
    }


    @Override
    public void onInfoWindowClick(Marker marker) {
        String msg;

        storeId = markerHashMap.get(marker);
        StoreName = markerHashMap2.get(storeId);
        Intent i = new Intent(this.getContext(), SpecificInfoActivity.class);
        i.putExtra("storeName", StoreName);
     //   Toast.makeText(getContext(),StoreName+"전달", android.widget.Toast.LENGTH_SHORT).show();
        startActivity(i);


    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        marker.showInfoWindow();
        return true;

    }


    private class GetData extends android.os.AsyncTask<String, Void, String> {

        android.app.ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = android.app.ProgressDialog.show(getContext(),
                    "Please Wait", null, true, true);
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();
            Log.d(TAG, "response - " + result);

            if (result == null) {
                Toast.makeText(getContext(), errorString, Toast.LENGTH_SHORT).show();
            } else {
                mJsonString = result;
      //          Toast.makeText(getContext(), "start arrangeResult", Toast.LENGTH_SHORT).show();
                arrangeResult();
            }
        }


        @Override
        protected String doInBackground(String... params) {

            String serverURL = params[0];
            String postParameters = "storeId=" + params[1];

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

                Log.d(TAG, "InsertData: Error ", e);
                errorString = e.toString();

                return null;
            }

        }
    }

    private void arrangeResult() {
        Toast.makeText(getContext(), mJsonString, android.widget.Toast.LENGTH_LONG).show();

        String TAG_STOREID = "storeId";

        String TAG_LAT = "lat";
        String TAG_LANG = "lang";
        String TAG_STORENAME = "storeName";

        try {
            int idx = mJsonString.indexOf("[");
            if (idx > 0) {
                mJsonString = mJsonString.substring(idx);
                mJsonString.trim();

                Log.d("MyApp", mJsonString);
                org.json.JSONArray jsonArray = new org.json.JSONArray(mJsonString);

                for (int i = 0; i < jsonArray.length(); i++) {
                    Log.d(TAG, "start arrange result");
                    org.json.JSONObject item = jsonArray.getJSONObject(i);

                    String lat = item.getString(TAG_LAT);
                    String lang = item.getString(TAG_LANG);
                    String id = item.getString(TAG_STOREID);
                    String name = item.getString(TAG_STORENAME);

                    LatLng storeLocation = new LatLng(Double.parseDouble(lat), Double.parseDouble(lang));

                    ////////////마커로 표시////////////////////
                    showStoreLocationMarker(storeLocation, Integer.parseInt(id),name);
                }
            }


        } catch (org.json.JSONException e) {

            Log.d(TAG, "showResult : ", e);
        }
    }


    private Marker addMarkerforStore(LatLng storeLatLng, int storeId) {
        Log.i("storeLca", String.valueOf(storeId));

        return map.addMarker(new MarkerOptions()
                .position(storeLatLng)
                .title("Store Position")
                .snippet("Click here to see.")
                .icon(BitmapDescriptorFactory.defaultMarker(180)));


    }

    private void showStoreLocationMarker(LatLng storeLatLng, int storeId,String storeName) {

        //      Toast.makeText(getContext(),"마커 입략 증", android.widget.Toast.LENGTH_LONG).show();

        markerHashMap.put(addMarkerforStore(storeLatLng, storeId), storeId);
        markerHashMap2.put(storeId,storeName);
        if (flag == 0)
        {
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(storeLatLng,17));
            flag = 1;
        }
        //마커 클릭 이벤트
        map.setOnMarkerClickListener(this);
        //툴팁 클릭 이벤트
        map.setOnInfoWindowClickListener(this);


    }


}

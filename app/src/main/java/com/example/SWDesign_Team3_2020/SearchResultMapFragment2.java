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

public class SearchResultMapFragment2 extends Fragment implements GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMarkerClickListener {

    //private static String IP_ADDRESS = "10.0.2.2";
    private static String IP_ADDRESS = "27.113.19.27";
    private static String TAG = "phpexample";

    LocationManager manager;
    GPSListener gpsListener;
    GoogleMap map;

    //  MarkerOptions storeLocationMarker;
    MarkerOptions myLocationMarker;
    MarkerOptions storeLocationMarker;

    Marker myMarker;
    java.util.ArrayList<com.google.android.gms.maps.model.Marker> storeMarker;

    int storeId;
    int markerIndex = 1;
    String mJsonString;
    private LatLng curPoint;
    private LatLng selectedPoint;
    private Double selectedLat;
    private Double selectedLon;

    private java.util.ArrayList<SearchResultViewItem> mArrayList;
    private SearchResultViewAdapter mAdapter;
    private String StoreName;
    private java.util.ArrayList<SearchResultViewItem> receivedList;

    HashMap<Marker, Integer> markerHashMap = new HashMap<Marker, Integer>();
    HashMap<Integer, String> markerHashMap2 = new java.util.HashMap<Integer, String>();
    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap googleMap) {
            manager = (LocationManager) Objects.requireNonNull(getContext()).getSystemService(getContext().LOCATION_SERVICE);
            gpsListener = new GPSListener();
            map = googleMap;
            //startLocationService
            Location location = null;


            long minTime = 0;
            float minDistance = 0;

            mArrayList = new java.util.ArrayList<>();
            mAdapter = new SearchResultViewAdapter(getActivity(), mArrayList);

            mArrayList.clear();
            mAdapter.notifyDataSetChanged();


            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "접근 권한이 없습니다. 설정에서 접근권한을 허용해주세요.", Toast.LENGTH_SHORT).show();
                return;
            } else {
                if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    double latitude, longitude;
                    location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                    if (location != null) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                    } else {
                        latitude = 35.8154885;
                        longitude = 128.5144324;
                    }
                    String message = "최근 위치1 -> Latitude : " + latitude + "\n Longitude : " + longitude;

                    showCurrentLocation(latitude, longitude);
                    Log.i("MyLocTest", "최근 위치1 호출");
                    //        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,gpsListener);
                } else if (manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    location = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (location != null) {
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        String message = "최근 위치2 -> Latitude : " + latitude + "\n Longitude : " + longitude;

                        showCurrentLocation(latitude, longitude);

                        Log.i("MyLocTest", "최근 위치2 호출");
                    }
                    //           manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, gpsListener);
                    //manager.removeUpdates(gpsListener);
                }
                if (googleMap != null) {
                    googleMap.setMyLocationEnabled(true);

                }
            }
        }


    };


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(com.example.SWDesign_Team3_2020.R.layout.fragment_search_result_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }


        //선택위치 받아오기
        String myValue_lat = this.getArguments().getString("latitude");
        String myValue_lon = this.getArguments().getString("longitude");

        selectedLat = Double.parseDouble(myValue_lat);
        selectedLon = Double.parseDouble(myValue_lon);
        selectedPoint = new com.google.android.gms.maps.model.LatLng(selectedLat, selectedLon);

        //     Toast.makeText(getContext(), "선택값!!!!!!!!!!!!!!" + String.valueOf(selectedPoint.latitude) + " " + String.valueOf(selectedPoint.longitude), android.widget.Toast.LENGTH_LONG).show();

        //검색 결과 받아오기
        int arrlen = this.getArguments().getInt("arrlen");
        //     Toast.makeText(getContext(),"검색결과"+String.valueOf(arrlen), android.widget.Toast.LENGTH_LONG).show();

        String myValue_name;
        String myValue_id;

        receivedList = new java.util.ArrayList<>();
        storeMarker = new java.util.ArrayList<>();
        //검색 결과 받아오기
        for(int i = 0;i<arrlen;i++)
        {
            SearchResultViewItem receivedItem = new com.example.SWDesign_Team3_2020.SearchResultViewItem();
            myValue_lat = this.getArguments().getString(String.valueOf(i)+"Lat");
            myValue_lon = this.getArguments().getString(String.valueOf(i)+"Long");
            myValue_name = this.getArguments().getString(String.valueOf(i)+"Name");
            myValue_id = this.getArguments().getString(String.valueOf(i)+"Id");

            receivedItem.setLat(myValue_lat);
            receivedItem.setLang(myValue_lon);
            receivedItem.setStoreName(myValue_name);
            receivedItem.setStoreId(myValue_id);

            receivedList.add(receivedItem);
            //        Toast.makeText(getContext(),"검색리스트"+receivedItem.getStoreName(), android.widget.Toast.LENGTH_LONG).show();
        }
        // map.animateCamera(CameraUpdateFactory.newLatLngZoom(selectedPoint, 17));


        Date selDate = new java.util.Date();
        String Keyword = selDate.toString();
        GetData task = new GetData();
        task.execute("http://" + IP_ADDRESS + "/showSearchResult.php", Keyword);
    }


    @Override
    public void onInfoWindowClick(Marker marker) {
        String msg;

        storeId = markerHashMap.get(marker);
        StoreName = markerHashMap2.get(storeId);

        if (storeId != -1) { // 내 위치가 아니라면 가게 id 보내기
            Intent i = new Intent(this.getContext(), SpecificInfoActivity.class);
            i.putExtra("storeName", StoreName);
            startActivity(i);
        }


    }


    @Override
    public boolean onMarkerClick(Marker marker) {

        marker.showInfoWindow();
        return true;

    }

    class GPSListener implements LocationListener {
        //위치 확인되었을 때 자동으로 호출됨

        @Override
        public void onLocationChanged(@NonNull Location location) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            String message = "내 위치는 Latitude : " + latitude + "\nLongtitude : " + longitude;
            Log.i("MyLocTest", message);

            showCurrentLocation(latitude, longitude);
            Log.i("MyLocTest", "onLocationChanged() 호출되었습니다.");

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }

    private void showCurrentLocation(double latitude, double longitude) {
        curPoint = new com.google.android.gms.maps.model.LatLng(latitude, longitude);

        map.animateCamera(CameraUpdateFactory.newLatLngZoom(selectedPoint, 15));

        //내 위치 보여주기 (해쉬맵에 추가)
        showMyLocationMarker(selectedPoint);

        //가게 위치 보여주기 -> DB에서 끌어올 것


        //마커 클릭 이벤트
        map.setOnMarkerClickListener(this);
        //툴팁 클릭 이벤트
        map.setOnInfoWindowClickListener(this);




    }

    ///////////////////////여기부터 손봐야함 - 가게의 시간 정보 vs 현재의 시간 정보 비교
    private boolean isitOpen(String openDate, String openTime, Date sd) {
        /*
        Toast.makeText(getContext(),"하...이게문제임", android.widget.Toast.LENGTH_LONG).show();
        //Date sd는 today!
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(sd);

        //오늘 요일(int): sd_nday

        int sd_nday = cal.get(java.util.Calendar.DAY_OF_WEEK);
        String sd_day = "";
        switch (sd_nday) {
            case 1:
                sd_day = "sun";
                break;
            case 2:
                sd_day = "mon";
                break;
            case 3:
                sd_day = "tue";
                break;
            case 4:
                sd_day = "wed";
                break;
            case 5:
                sd_day = "thu";
                break;
            case 6:
                sd_day = "fri";
                break;
            case 7:
                sd_day = "sat";
                break;
        }
        Log.i("open요일:", sd_day);


        String ot = openTime;
        String od = openDate;
        do {
            int idx1 = ot.indexOf(";");
            if (idx1 == -1)
                break;
            String ot_temp = ot.substring(idx1);
            ot = ot.substring(1, 4);
            Log.i("openTime요일:", ot);

            if (ot.equals(sd_day)) {
                while (true) {
                    int idx2 = od.indexOf(";");
                    if (idx2 != -1) {
                        //"123;"일때idx는3,length는4
                        String od_temp = "";
                        if (idx2 - 1 != od.length())
                            od_temp = od.substring(idx2 + 1);
                        od = od.substring(0, idx2);
                        Log.i("od_temp:", od_temp);
                        if (od.equals(sd))
                            return false;
                        if (od_temp == "") {
                            break;
                        } else {
                            od = od_temp;
                        }
                    } else {
                        break;
                    }
                }
                return true;
            }
            ot = ot_temp;
        } while (true);
        */

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
                //     Toast.makeText(getContext(), "start arrangeResult", Toast.LENGTH_SHORT).show();
                arrangeResult();
            }
        }


        @Override
        protected String doInBackground(String... params) {

            String serverURL = params[0];
            String postParameters = "country=" + params[1];

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
        //    Toast.makeText(getContext(), "arrangeREut", android.widget.Toast.LENGTH_LONG).show();

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
            org.json.JSONArray jsonArray = new org.json.JSONArray(mJsonString);

            for (int i = 0; i < jsonArray.length(); i++) {
                Log.d(TAG, "start arrange result");
                org.json.JSONObject item = jsonArray.getJSONObject(i);

                String lat = item.getString(TAG_LAT);
                String lang = item.getString(TAG_LANG);
                LatLng storeLocation = new LatLng(Double.parseDouble(lat), Double.parseDouble(lang));

                String openDate = item.getString(TAG_OPENDATE);
                String openTime = item.getString(TAG_OPENTIME);
                String category = item.getString(TAG_CATEGORY);
                String storeId = item.getString(TAG_STOREID);
                String storename = item.getString(TAG_STORENAME);

                Date today = new Date();

                //boolean isitClose(LatLng storeLocation, LatLng MyLocation)

                if (isitClose(storeLocation, curPoint) == true && isitOpen(openDate, openTime, today) == true && (category.equals("2"))) {

                    String storeName = item.getString(TAG_STORENAME);
                    String address = item.getString(TAG_ADDRESS);

                    SearchResultViewItem storeData = new SearchResultViewItem(storeId, storeName, category, lat, lang, address, openDate, openTime);
                    LatLng storeLatlng = new LatLng(parseDouble(lat), parseDouble(lang));
                    //              Toast.makeText(getContext(), "문제야문제", android.widget.Toast.LENGTH_LONG).show();

                    ////////////마커로 표시////////////////////

                    mArrayList.add(storeData);
                    //        Log.i("어레이 들어감??", mArrayList.get(0).getStoreName());
                    //    mAdapter.notifyDataSetChanged();
                } else {

                    //     Toast.makeText(getContext(), "멂어어어어어", android.widget.Toast.LENGTH_LONG).show();
                }
            }
            Log.d(TAG, "finish arrange result");

        } catch (org.json.JSONException e) {

            Log.d(TAG, "showResult : ", e);
        }
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
        }
        return false;
    }



    private void showMyLocationMarker(LatLng curPoint) {

        myLocationMarker = new MarkerOptions(); // 마커 객체 생성
        myLocationMarker.position(curPoint);
        myLocationMarker.title("Selected Place \n");
        myLocationMarker.icon(BitmapDescriptorFactory.defaultMarker(120));

        myMarker = map.addMarker(myLocationMarker);


        markerHashMap.put(myMarker, -1);
        //해쉬맵에 현위치 추가


        //    Toast.makeText(getContext(),"@@@@@@@@@@@@@@@@@"+String.valueOf(receivedList.size()), android.widget.Toast.LENGTH_LONG).show();
        //가게 위치 추가
        for(int i = 0;i<receivedList.size();i++)
        {
            storeLocationMarker = new MarkerOptions();
            LatLng receivedPosition = new LatLng(Double.parseDouble(receivedList.get(i).getLat()),Double.parseDouble(receivedList.get(i).getLang()));

            storeLocationMarker.position(receivedPosition);
            storeLocationMarker.title(receivedList.get(i).getStoreName());
            storeLocationMarker.snippet("Click here to see");
            storeLocationMarker.icon(BitmapDescriptorFactory.defaultMarker(300));

            storeMarker.add( map.addMarker(storeLocationMarker));

            markerHashMap.put(storeMarker.get(i),Integer.parseInt(receivedList.get(i).getStoreId()));
            markerHashMap2.put(Integer.parseInt(receivedList.get(i).getStoreId()),receivedList.get(i).getStoreName());

        }
    }
}

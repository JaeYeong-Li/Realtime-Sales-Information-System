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

import static java.lang.Double.parseDouble;
import static java.lang.Float.parseFloat;
import static java.lang.Integer.parseInt;
import static java.lang.String.*;

public class RestaurantOpenFragment extends Fragment implements GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMarkerClickListener {

    LocationManager manager;
    GPSListener gpsListener;
    GoogleMap map;

    MarkerOptions myLocationMarker;
    Marker myMarker;
    int markerIndex = 1;

    HashMap<Marker, Integer> markerHashMap = new HashMap<Marker, Integer>();

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


            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "접근 권한이 없습니다. 설정에서 접근권한을 허용해주세요.", Toast.LENGTH_SHORT).show();
                return;
            } else {
                if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                    location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                    if (location != null) {
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        String message = "최근 위치1 -> Latitude : " + latitude + "\n Longitude : " + longitude;

                        showCurrentLocation(latitude, longitude);
                        Log.i("MyLocTest", "최근 위치1 호출");
                    }
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
    private Marker marker;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_restaurant_open, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }

        Context context = getContext();
        CharSequence text = "Restaurant Open Fragement!";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }


    @Override
    public void onInfoWindowClick(Marker marker) {
        String msg = "tooltip!!";

        Toast toast = Toast.makeText(this.getContext(), msg, Toast.LENGTH_LONG);
        toast.show();
        if (markerHashMap.get(marker) != 0 ) {
            Intent i = new Intent(this.getContext(),SpecificInfoActivity.class);
            i.putExtra("Latitude", marker.getPosition().latitude);
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
    }

    private void showCurrentLocation(double latitude, double longitude) {
        LatLng curPoint = new LatLng(latitude, longitude);

        map.animateCamera(CameraUpdateFactory.newLatLngZoom(curPoint, 14));

        //내 위치 보여주기 (해쉬맵에 추가)
        showMyLocationMarker(curPoint);

        //가게 위치 보여주기 -> DB에서 끌어올 것
        LatLng storeLatlng_close = new LatLng(curPoint.latitude, curPoint.longitude + 0.01);
        //마커 옵션 객체 생성 ->가까운 것들만 (해쉬맵에 추가)
        if (isitClose(storeLatlng_close, curPoint) == true) {
            showStoreLocationMarker(storeLatlng_close);
            markerIndex++;
        }

        //마커 클릭 이벤트
        map.setOnMarkerClickListener(this);
        //툴팁 클릭 이벤트
        map.setOnInfoWindowClickListener(this);

    }

    private double decimalToradian(double decimal) {
        return (decimal * Math.PI / 180.0);
    }

    private double radianTodecimal(double radian) {
        return (radian * 180 / Math.PI);
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


    private void showStoreLocationMarker(LatLng storeLatLng) {

        MarkerOptions storeLocationMarker = new MarkerOptions();
        storeLocationMarker.position(storeLatLng);
        storeLocationMarker.title("Store Position \n");
        storeLocationMarker.snippet("Click here to see.");
        storeLocationMarker.icon(BitmapDescriptorFactory.defaultMarker(150));
        myMarker = map.addMarker(storeLocationMarker);

        markerHashMap.put(myMarker,markerIndex);
        Log.i("showStoreLocation", "가게 마커 불러옴");


    }



    private void showMyLocationMarker(LatLng curPoint) {
        if (myLocationMarker == null) {
            myLocationMarker = new MarkerOptions(); // 마커 객체 생성
            myLocationMarker.position(curPoint);
            myLocationMarker.title("Current Position \n");
            myLocationMarker.snippet("You are here.");
            myLocationMarker.icon(BitmapDescriptorFactory.defaultMarker(200));

            myMarker = map.addMarker(myLocationMarker);


        } else {
            myMarker.remove(); // 마커삭제
            myLocationMarker.position(curPoint);
            myMarker = map.addMarker(myLocationMarker);
        }

        markerHashMap.put(myMarker, 0);
        //해쉬맵에 현위치 추가

    }
}

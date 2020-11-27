package com.example.SWDesign_Team3_2020;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Context;
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

import java.util.Objects;

public class CafeOpenFragment extends Fragment implements onMapReadyCallback {

    LocationManager manager;
    GPSListener gpsListener;
    GoogleMap map;

    MarkerOptions myLocationMarker;
    Marker myMarker;
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
                    && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            {
                Toast.makeText(getContext(),"접근 권한이 없습니다. 설정에서 접근권한을 허용해주세요.",Toast.LENGTH_SHORT).show();
                return;
            }
            else
            {
                if(manager.isProviderEnabled(LocationManager.GPS_PROVIDER)){

                    location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

                    if (location != null) {
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        String message = "최근 위치1 -> Latitude : " + latitude + "\n Longitude : " + longitude;

                        showCurrentLocation(latitude, longitude);
                        Log.i("MyLocTest", "최근 위치1 호출");
                    }
                    //        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,gpsListener);
                }
                else if (manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    location = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    if (location != null) {
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        String message = "최근 위치2 -> Latitude : " + latitude + "\n Longitude : " + longitude;

                        showCurrentLocation(latitude,longitude);

                        Log.i("MyLocTest","최근 위치2 호출");
                    }
                    //           manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, gpsListener);
                    //manager.removeUpdates(gpsListener);
                }
                if(googleMap!=null)
                {
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

    class GPSListener implements LocationListener {
        //위치 확인되었을 때 자동으로 호출됨

        @Override
        public void onLocationChanged(@NonNull Location location) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            String message = "내 위치는 Latitude : " + latitude + "\nLongtitude : " + longitude;
            Log.i("MyLocTest",message);

            showCurrentLocation(latitude,longitude);
            Log.i("MyLocTest","onLocationChanged() 호출되었습니다.");

        }
    }

    private void showCurrentLocation(double latitude, double longitude) {
        LatLng curPoint = new LatLng(latitude, longitude);

        map.animateCamera(CameraUpdateFactory.newLatLngZoom(curPoint, 14));
        showMyLocationMarker(curPoint);

        //DB에서 가게 정보 확인
        //현위치 내 인근의 가게 ROW만 뽑을 것. -> 아이디.
        //밑에 슬라이딩 리스트 띄우고 아이디에 맞는 가게 정보 리스트로 나타내기
        //가게 정보 마커 등장 -> ROW의 위, 경도만 필요 , 클릭 이벤트 처리할땐 가게 ID필요
        LatLng storeLatlng_far = new LatLng(58,-122);
        LatLng storeLatlng_close = new LatLng(curPoint.latitude,curPoint.longitude+0.01);

        //이건 뜨고
        showStoreLocationMarker(storeLatlng_close,curPoint);
        //이건안떠야함
        showStoreLocationMarker(storeLatlng_far,curPoint);

    }
    private double decimalToradian(double decimal) {
        return (decimal * Math.PI / 180.0);
    }
    private double radianTodecimal(double radian){
        return (radian*180/Math.PI);
    }
    private boolean isitClose(LatLng storeLocation, LatLng MyLocation) {
        //현위치에서 1km 이내 가게인지 보여줌.

        double EARTH_R, Rad, radLat1, radLat2, radDist;
        double distance, ret;

        EARTH_R = 6371000.0;
        Rad = Math.PI / 180;
        radLat1 = Rad * storeLocation.latitude;
        radLat2 = Rad * MyLocation.latitude;
        radDist = Rad * (storeLocation.longitude- MyLocation.longitude);

        distance = Math.sin(radLat1) * Math.sin(radLat2);
        distance = distance + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.cos(radDist);
        ret = EARTH_R * Math.acos(distance);

        double rslt = ret / 1000;
        // km로 변환 및 반올림

        Log.i("거리",rslt+"km");
        if (rslt<=1.0)
        {
            return true;
        }
        else
            return false;
    }


    private void showStoreLocationMarker(LatLng storeLocation,LatLng curPoint){
        //거리 계산 -> 1km 내의 가게들만 뜨도록,,
        if (isitClose(storeLocation,curPoint)==true)
        {
            MarkerOptions storeLocationMarker = new MarkerOptions();
            storeLocationMarker.position(storeLocation);
            storeLocationMarker.icon(BitmapDescriptorFactory.defaultMarker(150));
            Marker myStoreMarker;
            myStoreMarker= map.addMarker(storeLocationMarker);
            Log.i("showStoreLocation", "가게 마커 불러옴");

        }

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

    }
}

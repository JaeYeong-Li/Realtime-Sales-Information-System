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
    public static java.util.ArrayList<SearchResultViewItem> mArrayList;
    public static SearchResultViewAdapter mAdapter;
    java.util.ArrayList<SearchResultViewItem> stores = new java.util.ArrayList<>();

    private java.util.ArrayList<SearchResultViewItem> mMyData;



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
        String myValue = this.getArguments().getString("latitude");
        Toast.makeText(getContext(),"전달값!!!!!!!!!!!!!!"+myValue, android.widget.Toast.LENGTH_LONG).show();

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

        //맵에 마커 찍기
/*
        Toast.makeText(getContext(),"어레이 리스트 ㅎ사용 가능?", android.widget.Toast.LENGTH_LONG).show();
        Toast.makeText(getContext(),valueOf(stores.size())+"개 원소 있음", android.widget.Toast.LENGTH_LONG).show();
*/
        for(int i=0; i< stores.size(); i++){
            Toast.makeText(getContext(),"ㅇㅇ가능", android.widget.Toast.LENGTH_LONG).show();

            LatLng storeLocation = new LatLng(Double.parseDouble(Search_Result.mArrayList.get(i).getLat()), Double.parseDouble(Search_Result.mArrayList.get(i).getLang()));

Toast.makeText(getContext(),valueOf(storeLocation.latitude), android.widget.Toast.LENGTH_SHORT).show();
            showStoreLocationMarker(storeLocation, Integer.parseInt(Search_Result.mArrayList.get(i).getStoreId()),Search_Result.mArrayList.get(i).getStoreName());
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

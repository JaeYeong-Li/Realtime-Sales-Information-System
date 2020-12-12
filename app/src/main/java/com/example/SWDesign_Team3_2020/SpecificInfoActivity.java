package com.example.SWDesign_Team3_2020;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class SpecificInfoActivity extends AppCompatActivity {

    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_info);
        textView = findViewById(R.id.TextView_markerInfo);

        //intent 받기
        double latitude = getIntent().getDoubleExtra("Latitude",0);
        //받은 정보 게시

        textView.setText(String.valueOf(latitude));

    }
}
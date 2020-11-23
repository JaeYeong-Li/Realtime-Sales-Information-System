package com.example.SWDesign_Team3_2020;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.NonNull ;
import android.widget.TextView;
import android.widget.CalendarView;

public class Search_Calender extends AppCompatActivity {
    public CalendarView calendarView;
    public TextView diaryTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.SWDesign_Team3_2020.R.layout.search_calender);
        // calender;
        diaryTextView = findViewById(R.id.calender_textView);
        calendarView = findViewById(R.id.calendarView);
        calendarView.setOnDateChangeListener(new android.widget.CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                diaryTextView.setVisibility(android.view.View.VISIBLE);
                diaryTextView.setText(String.format("%d / %d / %d",year,month+1,dayOfMonth));
            }
        });
//        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
//            @SuppressLint("DefaultLocale")
//            @Override
//            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
//                month += 1;
//                textView.setText(String.format("%d년 %d월 %d일", year, month, dayOfMonth));
//            }
//        });
    }


}

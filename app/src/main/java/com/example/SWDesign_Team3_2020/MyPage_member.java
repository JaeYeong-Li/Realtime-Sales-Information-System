package com.example.SWDesign_Team3_2020;

import androidx.appcompat.app.AppCompatActivity;

public class MyPage_member extends AppCompatActivity {

    private android.content.Context context = this;

    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_member);

        //use toolbar
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //툴바에 홈버튼 활성화
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //툴바의 홈버튼의 이미지를 변경
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_navi_menu);
    }

    public void clickHandler(android.view.View view) {
        switch (view.getId())
        {
            case R.id.mybutton1:
                android.widget.Toast.makeText(context, ": 클릭", android.widget.Toast.LENGTH_SHORT).show();
                android.content.Intent intent = new android.content.Intent(getApplicationContext(), Search_Location.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}

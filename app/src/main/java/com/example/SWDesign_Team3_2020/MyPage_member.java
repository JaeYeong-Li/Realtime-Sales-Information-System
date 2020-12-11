package com.example.SWDesign_Team3_2020;

import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;
import android.content.Intent;

public class MyPage_member extends AppCompatActivity {

    private android.content.Context context = this;
    private GlobalVar m_gvar = null;
    private String myName;
    private TextView myNameView;

    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mypage_member);

        //globalVal
        m_gvar = (GlobalVar) getApplicationContext();
        if(m_gvar.getuserNAME()!=null)
            myName=m_gvar.getuserNAME();

        //use toolbar
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //툴바에 홈버튼 활성화
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //툴바의 홈버튼의 이미지를 변경
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_navi_menu);

        myNameView = (TextView) findViewById(R.id.mypage_name);
        myNameView.setText(myName);
    }

    public void clickHandler(android.view.View view) {
        switch (view.getId())
        {
            case R.id.mytoedit:
                android.widget.Toast.makeText(context, ": 클릭", android.widget.Toast.LENGTH_SHORT).show();
                Intent intent = new android.content.Intent(getApplicationContext(), EditStoreInfo.class);
                startActivity(intent);
                finish();
                break;
        }
    }
}

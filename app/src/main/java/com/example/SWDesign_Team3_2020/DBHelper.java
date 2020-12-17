package com.example.SWDesign_Team3_2020;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class DBHelper extends SQLiteOpenHelper {

    final String TAG = "DBHelper";

    public Context context;

    public static final String TABLE_NAME = "MYFAVORITE";

    //Database information
    static final String DB_NAME = "favoriteStore.db";
    static final int DB_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.context = context;
    }

    //    최초 DB가 존재하지 않으면 새로 생성한다.
    @Override
    public void onCreate(SQLiteDatabase db) {

        //변수랑 섞어 놔서 복잡하니 공백을 잘 체크하자. 이것 때문에 에러나서 많은 시간을 허비했다.
        StringBuffer stringBuffer = new StringBuffer();

        stringBuffer.append("CREATE TABLE MYFAVORITE (");
        stringBuffer.append("id INTEGER PRIMARY KEY AUTOINCREMENT, ");
        stringBuffer.append("storeId TEXT, ");
        stringBuffer.append("storeName TEXT);");
        Log.d(TAG, stringBuffer.toString());
        db.execSQL(stringBuffer.toString());

        Toast.makeText(context, "선호가게 테이블 생성완료", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        try {
            Toast.makeText(context, "onUpgrade", Toast.LENGTH_SHORT).show();
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    DB에 직접 데어터를 추가하고 수정하고 삭제하는 함수들을 모아 둠
     */
    //데이터 추가하고 true/false를 반환한다.
    public boolean insertData(String id, String name){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("storeId", id);
        values.put("storeName", name);
        long result = db.insert(TABLE_NAME, null, values);
        if(result == -1){
            return false;
        } else {
            return true;
        }
    }


    //데이터 모두 가져오기
    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor corsor = db.rawQuery("select * from " + TABLE_NAME, null);
        return corsor;
    }

    //테이블 삭제
    public void deleteData(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "+TABLE_NAME);
        db.execSQL("UPDATE SQLITE_SEQUENCE SET seq = 0 WHERE name = '" + TABLE_NAME+"'");
    }

    /*
        DB에서 데이터를 모두 가져와서 ResyclerView 어답터에서 사용할 Items 배열에
        순더대로 추가하는 함수. 여기저기에서 불러서 사용해야 할경우를 대비 여기에 넣어 둠
     */
    public void updateItems() {
        FavoriteInfoActivity.mArrayList.clear();
        Cursor cursor = getAllData();
        if(cursor.getCount() == 0) {
            Toast.makeText(context, "데이터없음", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()){
                Log.i("데이터있음", cursor.getString(cursor.getColumnIndex("storeName")));
                String _id = cursor.getString(cursor.getColumnIndex("storeId"));
                String _name = cursor.getString(cursor.getColumnIndex("storeName"));

                FavoriteInfoActivity.mArrayList.add(new SearchResultViewItem(_id,_name));
            }
        }
        cursor.close();
    }

}
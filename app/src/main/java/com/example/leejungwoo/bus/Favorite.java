package com.example.leejungwoo.bus;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.app.ActionBar.LayoutParams;
import android.util.Log;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class Favorite extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);

        final DBHelper dbHelper = new DBHelper(getApplicationContext(), "Favorite.db", null, 1);

        final LinearLayout lm = (LinearLayout) findViewById(R.id.ll);

        // linearLayout params 정의
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        final String[] items = dbHelper.getResult().split("\n");

        for (int j = 0; j < items.length; j++) {
            // LinearLayout 생성
            LinearLayout ll = new LinearLayout(this);
            ll.setOrientation(LinearLayout.HORIZONTAL);

            // TextView 생성
            TextView tvProdc = new TextView(this);
            tvProdc.setText(items[j]);
            ll.addView(tvProdc);

            // 버튼 생성
            final Button btn = new Button(this);
            // setId 버튼에 대한 키값
            btn.setId(j + 1);
            btn.setText("검색");
            btn.setLayoutParams(params);

            final int position = j;

            btn.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    try
                    {
                        int s = Integer.parseInt(items[position]);
                        // do something when integer values comes
                        Intent intent = new Intent(getApplicationContext(), SearchResult.class);
                        intent.putExtra("bus", items[position]);
                        startActivity(intent);
                    }
                    catch(NumberFormatException nfe)
                    {
                        // do something when string values comes
                        Intent intent2 = new Intent(getApplicationContext(), SearchResult2.class);
                        intent2.putExtra("stop", items[position]);
                        startActivity(intent2);
                    }
                }
            });

            //버튼 add
            ll.addView(btn);
            //LinearLayout 정의된거 add
            lm.addView(ll);

        }
    }
}

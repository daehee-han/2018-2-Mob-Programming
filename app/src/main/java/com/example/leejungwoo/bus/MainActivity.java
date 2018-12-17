package com.example.leejungwoo.bus;
import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends Activity {
    public static final int sub = 1001; // 다른 액티비티를 띄우기 위한 요청코드(상수)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button search = (Button) findViewById(R.id.button); // 검색

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SearchResult.class);
                EditText content = (EditText) findViewById(R.id.editText);
                String busNum = content.getText().toString();
                intent.putExtra("bus", busNum);
                startActivity(intent);
                //startActivityForResult(intent, sub); // 액티비티 띄우기
            }
        });
    }
}
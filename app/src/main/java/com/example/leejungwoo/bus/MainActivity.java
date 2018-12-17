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
                EditText content = (EditText) findViewById(R.id.editText);
                String input = content.getText().toString();
                try
                {
                    int s = Integer.parseInt(input);
                    // do something when integer values comes
                    Intent intent = new Intent(getApplicationContext(), SearchResult.class);
                    intent.putExtra("bus", input);
                    startActivity(intent);
                }
                catch(NumberFormatException nfe)
                {
                    // do something when string values comes
                    Intent intent2 = new Intent(getApplicationContext(), SearchResult2.class);
                    intent2.putExtra("stop", input);
                    startActivity(intent2);
                }

                //startActivityForResult(intent, sub); // 액티비티 띄우기
            }
        });
    }
}
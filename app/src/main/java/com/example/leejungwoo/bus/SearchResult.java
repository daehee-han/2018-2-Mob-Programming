package com.example.leejungwoo.bus;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.content.Intent;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import java.net.URL;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class SearchResult extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        String apikey = "saSiyy3augedJEZZYd0Ij2HLMhLQIfXQulIc6PzkUBhqaLYsf9j07%2FvyIaiaymdS9hbTJR%2BFZeVrbMs0CGDNYw%3D%3D";

        Intent intent = getIntent();
        final String busNum = intent.getStringExtra("bus");

        StrictMode.enableDefaults();

        final TextView num = (TextView) findViewById(R.id.busNumber);
        TextView status1 = (TextView) findViewById(R.id.result); // 파싱된 결과확인

        boolean find_busId = false, find_stId = false, find_stNm = false,
                find_arrmsg1 = false, find_arrmsg2 = false;
        String busId = null, stId = null, stNm = null, arrmsg1 = null, arrmsg2 = null;

        try{
            URL numurl = new URL("http://ws.bus.go.kr/api/rest/busRouteInfo/getBusRouteList?"
                    + "ServiceKey="
                    + apikey
                    + "&strSrch=" + busNum
            );
            XmlPullParserFactory parserCreator2 = XmlPullParserFactory.newInstance();
            XmlPullParser parser2 = parserCreator2.newPullParser();

            parser2.setInput(numurl.openStream(), null);

            int parserEvent2 = parser2.getEventType();

            while (parserEvent2 != XmlPullParser.END_DOCUMENT){
                switch(parserEvent2){
                    case XmlPullParser.START_TAG: // parser가 시작 태그를 만나면 실행
                        if(parser2.getName().equals("busRouteId")){
                            find_busId = true;
                        }
                        break;

                    case XmlPullParser.TEXT: // parser가 내용에 접근했을때
                        if(find_busId){
                            busId = parser2.getText();
                            find_busId = false;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        // status1.setText(status1.getText() + "busnum: " + busNum + "버스 ID: " + busId + "\n");
                        break;
                }
                parserEvent2 = parser2.next();
            }
        } catch (Exception e){
            status1.setText("Error");
        }


        try{
            URL url = new URL("http://ws.bus.go.kr/api/rest/arrive/getArrInfoByRouteAll?"
                    + "ServiceKey="
                    + apikey
                    + "&busRouteId=" + busId
            ); // 검색 URL 부분

            XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserCreator.newPullParser();

            parser.setInput(url.openStream(), null);

            int parserEvent = parser.getEventType();

            while (parserEvent != XmlPullParser.END_DOCUMENT){
                switch(parserEvent){
                    case XmlPullParser.START_TAG: // parser가 시작 태그를 만나면 실행
                        if(parser.getName().equals("stId")){
                            find_stId = true;
                        }
                        if(parser.getName().equals("stNm")){
                            find_stNm = true;
                        }
                        if(parser.getName().equals("arrmsg1")){
                            find_arrmsg1 = true;
                        }
                        if(parser.getName().equals("arrmsg2")){
                            find_arrmsg2 = true;
                        }
                        break;

                    case XmlPullParser.TEXT: // parser가 내용에 접근했을때
                        if(find_stId){
                            stId = parser.getText();
                            find_stId = false;
                        }
                        if(find_stNm){
                            stNm = parser.getText();
                            find_stNm = false;
                        }
                        if(find_arrmsg1){
                            arrmsg1 = parser.getText();
                            find_arrmsg1 = false;
                        }
                        if(find_arrmsg2){
                            arrmsg2 = parser.getText();
                            find_arrmsg2 = false;
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if(parser.getName().equals("itemList")){
                            num.setText(busNum);
                            status1.setText(status1.getText() + "정류장 이름: " + stNm + "\n첫번째 도착 예정 버스: " + arrmsg1 + "\n두번째 도착 예정 버스: " + arrmsg2 + "\n\n");
                        }
                        break;
                }
                parserEvent = parser.next();
            }
        } catch(Exception e){
            status1.setText("Error");
        }

        final DBHelper dbHelper = new DBHelper(getApplicationContext(), "Favorite.db", null, 1);
        Button insert = (Button) findViewById(R.id.favorite);
        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.insert(busNum);
                Log.d("log", "save");
                Toast.makeText(getApplicationContext(), "저장되었습니다", Toast.LENGTH_LONG).show();
            }
        });

        Button delete = (Button) findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.delete(busNum);
                Log.d("log", "delete");
                Toast.makeText(getApplicationContext(), "삭제되었습니다", Toast.LENGTH_LONG).show();
            }
        });



    }

}

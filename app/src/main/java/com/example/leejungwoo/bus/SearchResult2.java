package com.example.leejungwoo.bus;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import java.net.URL;
import java.util.ArrayList;

public class SearchResult2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ArrayList<String> id = new ArrayList<String>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result2);

        String apikey = "saSiyy3augedJEZZYd0Ij2HLMhLQIfXQulIc6PzkUBhqaLYsf9j07%2FvyIaiaymdS9hbTJR%2BFZeVrbMs0CGDNYw%3D%3D";

        Intent intent = getIntent();
        final String stopName = intent.getStringExtra("stop");

        StrictMode.enableDefaults();

        TextView stop = (TextView) findViewById(R.id.busStop);
        TextView status1 = (TextView) findViewById(R.id.result); // 파싱된 결과확인

        boolean find_stopId = false, find_no1 = false, find_no2 = false,
                find_arrmsg1 = false, find_arrmsg2 = false;
        String stopId = null, no1 = null, no2 = null, arrmsg1 = null, arrmsg2 = null;

        try{
            URL numurl = new URL("http://ws.bus.go.kr/api/rest/stationinfo/getStationByName?"
                    + "serviceKey="
                    + apikey
                    + "&stSrch=" + stopName
            );
            XmlPullParserFactory parserCreator2 = XmlPullParserFactory.newInstance();
            XmlPullParser parser2 = parserCreator2.newPullParser();

            parser2.setInput(numurl.openStream(), null);

            int parserEvent2 = parser2.getEventType();

            while (parserEvent2 != XmlPullParser.END_DOCUMENT){
                switch(parserEvent2){
                    case XmlPullParser.START_TAG: // parser가 시작 태그를 만나면 실행
                        if(parser2.getName().equals("arsId")){
                            find_stopId = true;
                        }
                        break;

                    case XmlPullParser.TEXT: // parser가 내용에 접근했을때
                        if(find_stopId){
                            stopId = parser2.getText();
                            id.add(stopId);
                            find_stopId = false;
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
        stopId = id.get(0);

        try{
            URL url = new URL("http://ws.bus.go.kr/api/rest/stationinfo/getStationByUid?"
                    + "ServiceKey="
                    + apikey
                    + "&arsId=" + stopId
            ); // 검색 URL 부분

            XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserCreator.newPullParser();

            parser.setInput(url.openStream(), null);

            int parserEvent = parser.getEventType();

            while (parserEvent != XmlPullParser.END_DOCUMENT){
                switch(parserEvent){
                    case XmlPullParser.START_TAG: // parser가 시작 태그를 만나면 실행
                        if(parser.getName().equals("rtNm")){
                            find_no1 = true;
                        }
                        if(parser.getName().equals("arrmsg1")){
                            find_arrmsg1 = true;
                        }
                        if(parser.getName().equals("arrmsg2")){
                            find_arrmsg2 = true;
                        }
                        break;

                    case XmlPullParser.TEXT: // parser가 내용에 접근했을때
                        if(find_no1){
                            no1 = parser.getText();
                            find_no1 = false;
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
                            stop.setText(stopName);
                            status1.setText(status1.getText() + "버스 번호: " + no1 + "\n" + arrmsg1 + "\n" + arrmsg2 + "\n\n");
                        }
                        break;
                }
                parserEvent = parser.next();
            }
        } catch(Exception e){
            status1.setText("Error");
        }

        final DBHelper dbHelper = new DBHelper(getApplicationContext(), "Favorite.db", null, 1);
        Button insert = (Button) findViewById(R.id.favorite2);
        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.insert(stopName);
            }
        });

        Button insert = (Button) findViewById(R.id.delete2);
        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.delete(stopName);
            }
        });


    }
}

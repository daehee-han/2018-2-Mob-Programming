package com.example.leejungwoo.bus;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.content.Intent;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import java.net.URL;

public class SearchResult extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        String apikey = "saSiyy3augedJEZZYd0Ij2HLMhLQIfXQulIc6PzkUBhqaLYsf9j07%2FvyIaiaymdS9hbTJR%2BFZeVrbMs0CGDNYw%3D%3D";

        Intent intent = getIntent();
        String busNum = intent.getStringExtra("bus");

        StrictMode.enableDefaults();

        TextView status1 = (TextView) findViewById(R.id.result); // 파싱된 결과확인

        boolean find_busId = false, find_stId = false, find_stNm = false;
        String busId = null, stId = null, stNm = null;

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
                        break;
                    case XmlPullParser.END_TAG:
                        if(parser.getName().equals("itemList")){
                            status1.setText(status1.getText() + "정류소 ID: " + stId + "\n정류소 이름: " + stNm + "\n");
                        }
                        break;
                }
                parserEvent = parser.next();
            }
        } catch(Exception e){
            status1.setText("Error");
        }

    }
}

package com.example.leejungwoo.bus;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.net.URL;

public class SearchResult extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        StrictMode.enableDefaults();

        TextView status1 = (TextView)findViewById(R.id.result); // 파싱된 결과확인

        boolean instId = false, instNm = false;

        String stId = null, stNm = null;


        try{
            URL url = new URL("http://ws.bus.go.kr/api/rest/arrive/getArrInfoByRouteAll?"
                    + "ServiceKey="
                    + "saSiyy3augedJEZZYd0Ij2HLMhLQIfXQulIc6PzkUBhqaLYsf9j07%2FvyIaiaymdS9hbTJR%2BFZeVrbMs0CGDNYw%3D%3D"
                    + "&busRouteId=100100118"
            ); //검색 URL부분

            XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserCreator.newPullParser();

            parser.setInput(url.openStream(), null);

            int parserEvent = parser.getEventType();
            System.out.println("파싱시작합니다.");

            while (parserEvent != XmlPullParser.END_DOCUMENT){
                switch(parserEvent){
                    case XmlPullParser.START_TAG://parser가 시작 태그를 만나면 실행
                        if(parser.getName().equals("stId")){ //title 만나면 내용을 받을수 있게 하자
                            instId = true;
                        }
                        if(parser.getName().equals("stNm")){ //address 만나면 내용을 받을수 있게 하자
                            instNm = true;
                        }
                        if(parser.getName().equals("message")){ //message 태그를 만나면 에러 출력
                            status1.setText(status1.getText()+"에러");
                            //여기에 에러코드에 따라 다른 메세지를 출력하도록 할 수 있다.
                        }
                        break;

                    case XmlPullParser.TEXT://parser가 내용에 접근했을때
                        if(instId){ //isTitle이 true일 때 태그의 내용을 저장.
                            stId = parser.getText();
                            instId = false;
                        }
                        if(instNm){ //isAddress이 true일 때 태그의 내용을 저장.
                            stNm = parser.getText();
                            instNm = false;
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
            status1.setText("에러가..났습니다...");
        }
    }
}

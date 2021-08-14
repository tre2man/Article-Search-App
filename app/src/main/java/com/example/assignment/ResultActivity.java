package com.example.assignment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;


public class ResultActivity<DataVector> extends AppCompatActivity {

    //각 항목에 대한 결과값을 저장할 수 있는 어레이 자료형 선언
    ArrayList<String> JournalArray = new ArrayList<String>();
    ArrayList<String> JournalNameArray = new ArrayList<String>();
    ArrayList<String> YearArray = new ArrayList<String>();
    ArrayList<String> CategoryVector = new ArrayList<String>();
    ArrayList<String> TitleArray = new ArrayList<String>();
    ArrayList<String> AuthorArray = new ArrayList<String>();
    ArrayList<String> UrlArray = new ArrayList<String>();
    ArrayList<String> referenceArray = new ArrayList<String>();

    ResultClass CollectResultClass = new ResultClass();

    //넘어온 인텐트 값을 받는 클래스
    SearchClass ResultClass = new SearchClass();

    //주소 조합에 필요한 문자열 정의하기
    URL Apiurl;
    String category = "";
    String title = "";
    String author = "";
    String keyword = "";
    String abs = ""; //초록
    String dateFrom = "";
    String dateTo = "";
    String journal = "";
    String institution = "";
    String affiliation = "";
    String Adress = "&remoteAddr=112.158.203.34&remoteHost=112.158.203.34&referer=&displayCount=50";
    String key = "67462000";
    String url = "";

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        setTitle("검색 결과");

        //인텐트 데이터 수신부 및 url 조합
        Intent intent = getIntent();
        ResultClass = (SearchClass) intent.getSerializableExtra("object");
        JouranlChoice1(ResultClass.journalChoice1);
        JouranlChoice2(ResultClass.journalChoice2);
        JouranlChoice3(ResultClass.journalChoice3);

        dateFrom = ResultClass.journalStartyear;
        dateTo = ResultClass.journalLastyear;

        //논문, 참고문헌 검색의 url 합성식을 구분해야 한다.
        if(ResultClass.category.equals("논문")) {
            category = "articleSearch";
            //url 합성하기
            url = "https://www.kci.go.kr/kciportal/po/openapi/openApiSearch2.kci?apiCode=" + category + "&key=" + key + "&title=" + title + "&author=" + author + "&keyword=" + keyword +
                    "&abs=" + abs + "&dateFrom=" + dateFrom + "&dateTo=" + dateTo + "&journal=" + journal + "&institution=" + institution + "&affiliation=" + affiliation + Adress;
        } else {
            category = "referenceSearch";
            url = "https://www.kci.go.kr/kciportal/po/openapi/openApiSearch2.kci?apiCode=" + category + "&key=" + key + "&title=" + ResultClass.referenceName + "&author=" + ResultClass.referenceAuthor +
                    "&pubiYr=" + ResultClass.referenceYear + "&institution=" + ResultClass.referenceInstitution + "&remoteAddr=112.158.203.34&remoteHost=112.158.203.34&referer=";
        }

        //크롤링 시작
        if(category.equals("articleSearch")){
            //저널 검색부분
            GetArticleDataAsync getArticleDataAsync = new GetArticleDataAsync();
            try {
                //예외처리를 해야 어플리케이션이 강제종료 되지 않는다. 안드로이드 정책상 기본 구조
                CollectResultClass = getArticleDataAsync.execute().get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            //참고문헌 검색부분
            GetReferenceDataAsync getReferenceDataAsync = new GetReferenceDataAsync();
            try {
                CollectResultClass = getReferenceDataAsync.execute().get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //리스트 뷰 사용하기(정의하기)
        ListView listView = (ListView) findViewById(R.id.listviews);
        ItemListViewClass itemListViewClass = new ItemListViewClass();

        //논문 검색할 경우와 참고문헌 검색할 경우를 나누어서 리스트뷰에 대입한다.
        //어플리케이션의 성능 문제로 인하여 상위 10개의 논문만 출력되게 했다.
        if(ResultClass.category.equals("논문")) {
            for(int i=0;i<CollectResultClass.JournalArray.size();i++) {
                itemListViewClass.AddElemJournal(CollectResultClass.TitleArray.get(i),CollectResultClass.AuthorArray.get(i),CollectResultClass.JournalNameArray.get(i));
            }
        } else {
            for(int i=0;i<CollectResultClass.JournalArray.size();i++) {
                itemListViewClass.AddElemJournal(CollectResultClass.JournalArray.get(i),"","");
            }
        }


        //리스트뷰 보이게 하기
        listView.setAdapter(itemListViewClass);

        //리스트뷰에 있는 항목을 클릭했을 경우에?
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //DetailActivity로 보내기 위한 준비
                Intent intent = new Intent(getApplicationContext(), DetailActivity.class);

                //전송할 클래스에 결과값 클래스를 대입하기
                CollectResultClass.type = ResultClass.category;

                //결과값과 선택한 항목이 몇 번째인지를 DetailActivity로 전송한다.
                intent.putExtra("result", CollectResultClass );
                intent.putExtra("num", position);
                startActivity(intent);
            }
        });
    }

    //앱 내부에서 네트워크 작업을 수행할 경우에는 Async 클래스로 실행한다.
    //Main 함수랑 구분해서 실행해야 하는 안드로이드 정책상 필수다.
    //논문 파싱 부분
    public class GetArticleDataAsync extends AsyncTask<String, String, com.example.assignment.ResultClass> {

        ResultClass AsyncResultClass = new ResultClass();

        @Override
        protected com.example.assignment.ResultClass doInBackground(String... strings) {

            boolean journal_name = false, publisher_name = false, pub_year = false, title = false, title_tag = true, author = false, url_ = false, record = false;
            String Author = "";

            //xml 파싱을 시작한다.
            //xml 파싱은 시작태그, 종료태그, 내용태그를 구분하는 것이 기본이다.
            //다음 태그로 넘어갈 때 while문을 한번 돌게 된다.
            //원하는 시작태그가 나오면 그 태그에 맞는 boolean을 true로 해준다.
            //목표한 태그가 true값을 가지게 되면 내용태그(내용)를 백터에 추가한다.

            try{
                Apiurl = new URL(url);
                InputStream input = Apiurl.openStream();
                XmlPullParserFactory parsers = XmlPullParserFactory.newInstance();
                XmlPullParser parser = parsers.newPullParser();
                parser.setInput(input,"UTF-8");

                //데이터 분석 시작, 한번에 한 개의 태그를 분석한다.
                while(parser.getEventType() != XmlPullParser.END_DOCUMENT){

                    //파싱한 데이터의 타입 변수를 저장한다. 시작태그, 텍스트태그, 종료태그를 구분한다.
                    int type = parser.getEventType();

                    //조건에 맞는 데이터가 발견되면 각 데이터에 맞게 대입한다.
                    if(journal_name) {
                        JournalArray.add(parser.getText());
                        journal_name = false;
                    } else if(publisher_name) {
                        JournalNameArray.add(parser.getText());
                        publisher_name = false;
                    } else if(pub_year) {
                        YearArray.add(parser.getText());
                        pub_year = false;
                    } else if(title && title_tag) {
                        //제목은 한글 제목만 추출한다.
                        TitleArray.add(parser.getText());
                        title_tag = false;
                    } else if(url_) {
                        UrlArray.add(parser.getText());
                        url_ = false;
                    } else if(author && type == XmlPullParser.TEXT && record) {
                        //공동저자일 경우가 있으므로 ArrayList에 저장
                        //빈칸이 있는 경우가 있다. 빈칸을 없애기 위하여 trim 함수를 사용한다.
                        Author = Author.trim() + "\n" + parser.getText().trim();
                        System.out.println(parser.getText());
                    } else if(type == XmlPullParser.END_TAG && parser.getName().equals("author-group")) {
                        author = false;
                    } else if(type == XmlPullParser.END_TAG && parser.getName().equals("record")) {
                        //마지막 태그일때는 한 개의 논문에 대한 정보 result 1 칸에 저장, resultClass는 초기화
                        title_tag = true;
                        title = false;
                        record = false;
                        AuthorArray.add(Author);
                        Author = "";
                    } else if(type == XmlPullParser.END_TAG && parser.getName().equals("author")) {
                        author = false;
                    }

                    //원하는 태그의 값이 나올 경우에 그 항목에 대한 boolean을 true로 지정
                    if(type == XmlPullParser.START_TAG && parser.getName().equals("journal-name")) {
                        journal_name = true;
                    } else if(type == XmlPullParser.START_TAG && parser.getName().equals("publisher-name")) {
                        publisher_name = true;
                    } else if(type == XmlPullParser.START_TAG && parser.getName().equals("pub-year")) {
                        pub_year = true;
                    } else if(type == XmlPullParser.START_TAG && parser.getName().equals("article-title")) {
                        title = true;
                    } else if(type == XmlPullParser.START_TAG && parser.getName().equals("author")) {
                        author = true;
                    } else if(type == XmlPullParser.START_TAG && parser.getName().equals("url")) {
                        url_ = true;
                    } else if(type == XmlPullParser.START_TAG && parser.getName().equals("record")) {
                        record = true;
                    }

                    /*
                    //파싱 확인을 위한 코드
                    if(type == XmlPullParser.START_DOCUMENT){
                        System.out.println("Start Doc");
                    } else if(type == XmlPullParser.START_TAG){
                        System.out.println("Start Tag " + parser.getName());
                    } else if(type == XmlPullParser.END_TAG){
                        System.out.println("End Tag " + parser.getName());
                    } else if(type == XmlPullParser.TEXT){
                        System.out.println(parser.getText());
                    }
                     */

                    type = parser.next();

                    }
                } catch (XmlPullParserException ex) {
                ex.printStackTrace();
            } catch (MalformedURLException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            //리턴할 클래스에 모은 데이터 전송
            AsyncResultClass.JournalArray = JournalArray;
            AsyncResultClass.JournalNameArray = JournalNameArray;
            AsyncResultClass.YearArray = YearArray;
            AsyncResultClass.CategoryVector = CategoryVector;
            AsyncResultClass.TitleArray = TitleArray;
            AsyncResultClass.AuthorArray = AuthorArray;
            AsyncResultClass.UrlArray = UrlArray;

            return AsyncResultClass;
        }
    }

    //참고문헌 파싱 부분
    public class GetReferenceDataAsync extends AsyncTask<String, String, com.example.assignment.ResultClass> {

        ResultClass AsyncResultClass = new ResultClass();

        @Override
        protected com.example.assignment.ResultClass doInBackground(String... strings) {

            try{
                Apiurl = new URL(url);
                InputStream input = Apiurl.openStream();
                XmlPullParserFactory parsers = XmlPullParserFactory.newInstance();
                XmlPullParser parser = parsers.newPullParser();
                parser.setInput(input,"UTF-8");

                boolean record = false;

                while(parser.getEventType() != XmlPullParser.END_DOCUMENT){

                    int type = parser.getEventType();

                    if(record) {
                        referenceArray.add(parser.getText());
                        record = false;
                    }

                        //원하는 태그의 값이 나올 경우에 그 항목에 대한 boolean을 true로 지정
                    if(type == XmlPullParser.START_TAG && parser.getName().equals("record")) {
                        record = true;
                    }

                    /*
                    //파싱 확인을 위한 코드
                    if(type == XmlPullParser.START_DOCUMENT){
                        System.out.println("Start Doc");
                    } else if(type == XmlPullParser.START_TAG){
                        System.out.println("Start Tag " + parser.getName());
                    } else if(type == XmlPullParser.END_TAG){
                        System.out.println("End Tag " + parser.getName());
                    } else if(type == XmlPullParser.TEXT){
                        System.out.println(parser.getText());
                    }
                     */

                    type = parser.next();

                }
            } catch (XmlPullParserException ex) {
                ex.printStackTrace();
            } catch (MalformedURLException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            //리턴할 클래스에 모은 데이터 전송
            AsyncResultClass.JournalArray = referenceArray;
            return AsyncResultClass;
        }
    }


    //검색 항목의 태그와 내용을 연결시켜주는 함수 부분이다.
    public void JouranlChoice1 (String input){
        if(input.equals("논문제목")){
            title = ResultClass.journalEditText1;
        } else if(input.equals("저자명")) {
            author = ResultClass.journalEditText1;
        } else if(input.equals("저널")) {
            journal = ResultClass.journalEditText1;
        } else if(input.equals("발행기관명")) {
            institution = ResultClass.journalEditText1;
        } else if(input.equals("저자소속기관")) {
            affiliation = ResultClass.journalEditText1;
        } else if(input.equals("키워드")) {
            keyword = ResultClass.journalEditText1;
        } else if(input.equals("초록")) {
            abs = ResultClass.journalEditText1;
        }
    }

    public void JouranlChoice2 (String input){
        if(input.equals("논문제목") && title.equals("")){
            title = ResultClass.journalEditText2;
        } else if(input.equals("저자명") && author.equals("")) {
            author = ResultClass.journalEditText2;
        } else if(input.equals("저널") && journal.equals("")) {
            journal = ResultClass.journalEditText2;
        } else if(input.equals("발행기관명") && institution.equals("")) {
            institution = ResultClass.journalEditText2;
        } else if(input.equals("저자소속기관") && affiliation.equals("")) {
            affiliation = ResultClass.journalEditText2;
        } else if(input.equals("키워드") && keyword.equals("")) {
            keyword = ResultClass.journalEditText2;
        } else if(input.equals("초록") && abs.equals("")) {
            abs = ResultClass.journalEditText2;
        }
    }

    public void JouranlChoice3 (String input){
        if(input.equals("논문제목") && title.equals("")){
            title = ResultClass.journalEditText3;
        } else if(input.equals("저자명") && author.equals("")) {
            author = ResultClass.journalEditText3;
        } else if(input.equals("저널") && journal.equals("")) {
            journal = ResultClass.journalEditText3;
        } else if(input.equals("발행기관명") && institution.equals("")) {
            institution = ResultClass.journalEditText3;
        } else if(input.equals("저자소속기관") && affiliation.equals("")) {
            affiliation = ResultClass.journalEditText3;
        } else if(input.equals("키워드") && keyword.equals("")) {
            keyword = ResultClass.journalEditText3;
        } else if(input.equals("초록") && abs.equals("")) {
            abs = ResultClass.journalEditText3;
        }
    }
}

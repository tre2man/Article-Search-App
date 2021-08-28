package com.example.assignment;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {

    //공유할 텍스트를 저장하기 위한 변수
    String Share;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        setTitle("검색 결과");

        //ResultActicity에서 정보를 수신한다.
        Intent intent = getIntent();
        ResultClass resultClass = (ResultClass) intent.getSerializableExtra("result");

        //받아온 좌표값을 표시하는 곳에 사용한다.
        int position = (Integer) intent.getSerializableExtra("num");

        //리니어 레이아웃 변수를 선언한다.
        LinearLayout journals = findViewById(R.id.journals);
        LinearLayout references = findViewById(R.id.references);

        if(resultClass.type.equals("논문")) {
            //논문 정보를 표시할경우

            //논문 정보 표시, 저널 정보 숨기기
            journals.setVisibility(View.VISIBLE);
            references.setVisibility(View.INVISIBLE);

            //텍스트뷰 변수 선언
            TextView title = findViewById(R.id.title);
            TextView author = findViewById(R.id.author);
            TextView journal = findViewById(R.id.journal);
            TextView journalname = findViewById(R.id.journalname);
            TextView year = findViewById(R.id.year);
            TextView url = findViewById(R.id.url);

            //텍스트뷰에 들어갈 내용 채우기
            title.setText(resultClass.TitleArray.get(position));
            author.setText(resultClass.AuthorArray.get(position));
            journal.setText(resultClass.JournalArray.get(position));
            journalname.setText(resultClass.JournalNameArray.get(position));
            year.setText(resultClass.YearArray.get(position));

            //url에는 하이퍼링크를 적용시켜 원문을 보고 싶으면 링크로 바로 접속이 가능하게 했다.
            url.setText(resultClass.UrlArray.get(position));
            Share = resultClass.UrlArray.get(position);

        } else {
            //저널 정보를 표시할경우

            //논문 정보 숨기기, 저널 정보 표시하기
            journals.setVisibility(View.INVISIBLE);
            references.setVisibility(View.VISIBLE);

            //텍스트뷰 변수 선언 및 내용 채우기
            TextView reference = findViewById(R.id.reference);
            reference.setText(resultClass.JournalArray.get(position));

            //참고문헌 제목을 공유한다.
            Share = resultClass.JournalArray.get(position);
        }

    }

    //공유 메뉴를 표시하기 위한 코드
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_actions, menu) ;
        return true ;
    }

    //공유 버튼 눌렀을 경우에 실행될 동작
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //공유 인텐트를 선언한다.
        Intent intent = new Intent(Intent.ACTION_SEND);
        //공유 타입은 일반 텍스트이다.
        intent.setType("text/plain");

        //공유할 텍스트를 입력한다.
        intent.putExtra(intent.EXTRA_TEXT,Share);
        Intent chooser = Intent.createChooser(intent, "논문 링크 공유하기");
        startActivity(chooser);

        return super.onOptionsItemSelected(item);
    }
}

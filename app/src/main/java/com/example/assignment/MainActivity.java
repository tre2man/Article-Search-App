package com.example.assignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("논문 검색기");

        //단순검색 버튼과 상세검색 버튼 설정
        Button Normal = findViewById(R.id.Normalsearch);
        Button Detail = findViewById(R.id.Detailsearch);

        final EditText input = findViewById(R.id.editText);

        //검색값 전달 위한 클래스 선언
        final SearchClass SearchingClass = new SearchClass();

        //단순검색 버튼 늘렀을 경우
        Normal.setOnClickListener(v -> {
            if(input.getText().toString().getBytes().length <= 0){
                //입력값이 없을경우 토스트 문자 출력
                Toast.makeText(getApplicationContext(),"검색어를 입력해주세요.",Toast.LENGTH_SHORT).show();
            }
            else {
                //논문 제목으로 검색한다. searchingclass 에 올바른 항목 입력
                SearchingClass.category = "논문";
                SearchingClass.journalEditText1 = input.getText().toString();
                SearchingClass.journalChoice1 = "논문제목";

                //입력값이 있을경우, 입력값을 ResultActivity로 전달한다.
                Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
                intent.putExtra("object",SearchingClass);
                startActivity(intent);
            }
        });

        //상세검색 버튼 늘렀을 경우
        Detail.setOnClickListener(v -> {
            //SearchActivity로 이동하기
            Intent intent = new Intent(getApplicationContext(), SearchActivity.class);
            startActivity(intent);
        });
    }
}

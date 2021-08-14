package com.example.assignment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setTitle("상세 검색하기");

        //카테고리, 검색기준, 정렬기준 배열 선언
        final String[] ArrCondition = {"논문제목","저자명","저널","발행기관명","저자소속기관","키워드","초록"};

        //카테고리를 선택할 경우에 원하는 UI가 나오게 하기 위하여 선언
        final LinearLayout journalLayout = (LinearLayout) findViewById(R.id.journal);
        final LinearLayout referencelLayout = (LinearLayout) findViewById(R.id.reference);
        final String[] category = {"논문"};

        Button resultbutton = (Button)findViewById(R.id.searchbutton);
        final Button journal = (Button) findViewById(R.id.journalButton);
        final Button reference = (Button) findViewById(R.id.referenceButton);

        //스피너, 텍스트 입력 변수 선언
        final Spinner journal1 = (Spinner) findViewById(R.id.journalspinner1_1);
        final Spinner journal2 = (Spinner) findViewById(R.id.journalspinner2_1);
        final Spinner journal3 = (Spinner) findViewById(R.id.journalspinner3_1);

        final EditText JournalEditText1 = (EditText) findViewById(R.id.journaleditText1);
        final EditText JournalEditText2 = (EditText) findViewById(R.id.journaleditText2);
        final EditText JournalEditText3 = (EditText) findViewById(R.id.journaleditText3);
        final EditText JournalStartyear = (EditText) findViewById(R.id.journalStartyear);
        final EditText JournalLastyear = (EditText) findViewById(R.id.journalLastyear);

        final EditText ReferenceName = (EditText) findViewById(R.id.referenceName);
        final EditText ReferenceAuthor = (EditText) findViewById(R.id.referenceAuthor);
        final EditText ReferenceInstitution = (EditText) findViewById(R.id.referenceInstitution);
        final EditText ReferenceYear = (EditText) findViewById(R.id.referenceYear);

        //액티비티 전달할때 사용할 클래스
        final SearchClass SearchingClass = new SearchClass();

        //논문 조건1
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ArrCondition);
        journal1.setAdapter(adapter2);

        //논문 조건2
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ArrCondition);
        journal2.setAdapter(adapter3);

        //논문 조건3
        ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, ArrCondition);
        journal3.setAdapter(adapter4);


        //논문 조건 1,2,3 텍스트 색 흰색으로 변경하는 코드이다.
        journal1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView)parent.getChildAt(0)).setTextColor(Color.WHITE);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        journal2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView)parent.getChildAt(0)).setTextColor(Color.WHITE);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        journal3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView)parent.getChildAt(0)).setTextColor(Color.WHITE);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        //카테고리 선택하는 코드.
        journal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //논문검색을 선택했을 경우

                //논문입력 보이기, 참고문헌입력 가리기
                journalLayout.setVisibility(View.VISIBLE);
                referencelLayout.setVisibility(View.INVISIBLE);

                //버튼 테마 변경하기
                journal.setBackgroundResource(R.drawable.buttonshape_click);
                journal.setTextColor(Color.BLACK);
                reference.setBackgroundResource(R.drawable.buttonshape_noclick);
                reference.setTextColor(Color.WHITE);

                //선택한 검색방법
                category[0] = "논문";
            }
        });

        reference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //참고문헌 선택했을 경우

                //논문입력 가리기, 참고문헌입력 보이기
                journalLayout.setVisibility(View.INVISIBLE);
                referencelLayout.setVisibility(View.VISIBLE);

                //버튼 테마 변경하기
                journal.setBackgroundResource(R.drawable.buttonshape_noclick);
                journal.setTextColor(Color.WHITE);
                reference.setBackgroundResource(R.drawable.buttonshape_click);
                reference.setTextColor(Color.BLACK);

                //선택한 검색방법
                category[0] = "참고문헌";
            }
        });

        //검색 버튼을 눌렀을 경우에 동작
        resultbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //전송할 클래스에 변수 대입하기

                //카테고리 선택
                SearchingClass.category = category[0];

                //논문 카테고리에서 선택한 내용 대입하기
                SearchingClass.journalEditText1 = JournalEditText1.getText().toString();
                SearchingClass.journalEditText2 = JournalEditText2.getText().toString();
                SearchingClass.journalEditText3 = JournalEditText3.getText().toString();
                SearchingClass.journalStartyear = JournalStartyear.getText().toString();
                SearchingClass.journalLastyear = JournalLastyear.getText().toString();

                //참고문헌 카테고리에서 선택한 내용 대입하기
                SearchingClass.referenceName = ReferenceName.getText().toString();
                SearchingClass.referenceAuthor = ReferenceAuthor.getText().toString();
                SearchingClass.referenceInstitution = ReferenceInstitution.getText().toString();
                SearchingClass.referenceYear = ReferenceYear.getText().toString();

                //논문 카테고리에서 선택한 태그 대입하기
                SearchingClass.journalChoice1 = (String) journal1.getSelectedItem();
                SearchingClass.journalChoice2 = (String) journal2.getSelectedItem();
                SearchingClass.journalChoice3 = (String) journal3.getSelectedItem();

                //연도를 제외하고 한 칸도 입력하지 않았을 경우와 연도를 잘못 입력했을 경우의 동작.
                if (SearchingClass.category.equals("논문") && SearchingClass.journalEditText1.getBytes().length <= 0 && SearchingClass.journalEditText2.getBytes().length <= 0 && SearchingClass.journalEditText3.getBytes().length <= 0 ) {
                    Toast.makeText(getApplicationContext(), "논문 검색시에 연도를 제외하고 적어도 1칸 이상 입력해야 합니다.", Toast.LENGTH_SHORT).show();
                } else if (SearchingClass.category.equals("참고문헌") && SearchingClass.referenceName.getBytes().length <= 0 && SearchingClass.referenceAuthor.getBytes().length <= 0 && SearchingClass.referenceInstitution.getBytes().length <= 0 ) {
                    Toast.makeText(getApplicationContext(), "참고문헌 검색어시에 연도를 제외하고 적어도 1칸 이상 입력해야 합니다.", Toast.LENGTH_SHORT).show();
                } else if (SearchingClass.journalStartyear.length() <= 5 && SearchingClass.journalStartyear.length() >= 1 || SearchingClass.journalLastyear.length() <= 5 && SearchingClass.journalLastyear.length() >= 1) {
                    Toast.makeText(getApplicationContext(), "연도 + 월 입력해야 합니다. 예) 202006", Toast.LENGTH_SHORT).show();
                } else if(SearchingClass.referenceYear.length() <= 3 && SearchingClass.referenceYear.length() >= 1 ) {
                    Toast.makeText(getApplicationContext(), "4자리 연도를 입력해야 합니다. 예) 2020", Toast.LENGTH_SHORT).show();
                } else {
                    //ResultActivity 로 데이터 전송과 동시에 ResultActivity open
                    Intent intent = new Intent(getApplicationContext(), ResultActivity.class);
                    intent.putExtra("object", SearchingClass);
                    startActivity(intent);
                }
            }
        });
    }
}

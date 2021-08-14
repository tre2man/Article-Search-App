//검색 결과를 리스트뷰에 출력하기 위한 변수들을 담는 클래스.
//결과값이 얼마나 나올지 모르기 때문에 크기가 가변적인 ArrayList 변수형을 사용했다.

package com.example.assignment;

import java.io.Serializable;
import java.util.ArrayList;

public class ResultClass implements Serializable {
    public String type;
    public ArrayList<String> JournalArray = new ArrayList<String>();
    public ArrayList<String> JournalNameArray = new ArrayList<String>();
    public ArrayList<String> YearArray = new ArrayList<String>();
    public ArrayList<String> CategoryVector = new ArrayList<String>();
    public ArrayList<String> TitleArray = new ArrayList<String>();
    public ArrayList<String> AuthorArray = new ArrayList<String>();
    public ArrayList<String> UrlArray = new ArrayList<String>();
}

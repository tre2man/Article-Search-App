//검색 파라메터를 저장하는 클래스.
//문자열 형태의 변수들을 모아서 한번에 Intent 이동을 시킬려 한다.
package com.example.assignment;

import java.io.Serializable;

public class SearchClass implements Serializable {

    public String category = "";

    public String journalEditText1 = "";
    public String journalEditText2 = "";
    public String journalEditText3 = "";
    public String journalStartyear = "";
    public String journalLastyear = "";

    public String journalChoice1 = "";
    public String journalChoice2 = "";
    public String journalChoice3 = "";

    public String referenceName = "";
    public String referenceAuthor = "";
    public String referenceInstitution = "";
    public String referenceYear = "";
}

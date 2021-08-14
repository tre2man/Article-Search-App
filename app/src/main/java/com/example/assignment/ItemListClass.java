//1칸의 리스트뷰에 들어갈 변수들을 저장하는 클래스.
//private를 이용해서 보안성을 높였다.
package com.example.assignment;

public class ItemListClass {

    //제목, 저자, 발행기관 문자열 선언
    private String title;
    private String author;
    private String institution;

    public void setTitle(String input){
        title = input;
    }
    public void setAuthor(String input){
        author = input;
    }
    public void setInstitution(String input){
        institution = input;
    }

    public String getTitle() {
        return this.title;
    }
    public String getAuthor() {
        return this.author;
    }
    public String getInstitution() {
        return this.institution;
    }
}


//커스텀 리스트뷰를 위한 클래스.
//한 칸에 논문 제목, 저자, 실린 저널 3가지의 정보를 보여준다.
package com.example.assignment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ItemListViewClass extends BaseAdapter {

    //정보를 담는 배열을 선언한다.
    private ArrayList<ItemListClass> list = new ArrayList<>();

    //배열의 크기를 리턴하는 함수
    @Override
    public int getCount() {
        return list.size();
    }

    //아이템 리턴하는 함수
    @Override
    public ItemListClass getItem(int pos) {
        return list.get(pos);
    }

    //아이템의 위치를 리턴하는 함수
    @Override
    public long getItemId(int pos) {
        return pos;
    }

    @Override
    public View getView(int pos, View view, ViewGroup viewGroup) {

        //뷰에 저장된 것이 없을 시 지정된 칸에 출력하게 한다.
        if(view == null){
            LayoutInflater inflater = (LayoutInflater) viewGroup.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.itemview,viewGroup,false);
        }

        //텍스트뷰 변수 선언한다.
        TextView title = (TextView) view.findViewById(R.id.title);
        TextView author = (TextView) view.findViewById(R.id.author);
        TextView institution = (TextView) view.findViewById(R.id.institution);

        //ItemListClass에 리스트에 해당하는 변수를 저장한다.
        ItemListClass itemListClass = getItem(pos);

        //변수를 리스트뷰에 대입한다.
        title.setText(itemListClass.getTitle());
        author.setText(itemListClass.getAuthor());
        institution.setText(itemListClass.getInstitution());

        //뷰를 출력한다.
        return view;
    }

    //정보를 담는 배열에 출력할 구성요소들을 입력하는 함수이다.
    public void AddElemJournal(String title, String author, String pub_name){
        ItemListClass itemListClass = new ItemListClass();

        //출력할 구성요소 대입하기
        itemListClass.setTitle(title);
        itemListClass.setAuthor(author);
        itemListClass.setInstitution(pub_name);

        //list ArrayList에 아이템 추가하기
        list.add(itemListClass);
    }
}

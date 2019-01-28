package com.example.a1117p.bboom;

import android.content.Context;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;

public class ListViewAdapter extends BaseAdapter {
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<Custom_Item> Custom_ItemList = new ArrayList<>();

    // ListViewAdapter의 생성자
    public ListViewAdapter() {

    }

    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return Custom_ItemList.size();
    }

    ImageView profile_img, prev_img;
    TextView prev_text, info_text, nickname_text, time, title, count_img;

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.view_cutom_item, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
        profile_img = convertView.findViewById(R.id.profile_img);
        prev_img = convertView.findViewById(R.id.prev_img);
        prev_text = convertView.findViewById(R.id.prev_text);
        info_text = convertView.findViewById(R.id.info);
        nickname_text = convertView.findViewById(R.id.nickname);
        title = convertView.findViewById(R.id.title);
        time = convertView.findViewById(R.id.time);
        count_img = convertView.findViewById(R.id.count_img);


        // Data Set(Custom_ItemList)에서 position에 위치한 데이터 참조 획득
        Custom_Item Custom_Item = Custom_ItemList.get(position);
        HashMap<Integer, String> hashMap = MySharedPreferences.getHashmap();
        if (hashMap.containsKey(Custom_Item.getUserno())) {
            Custom_ItemList.remove(position);
            Custom_Item = Custom_ItemList.get(position);
            notifyDataSetChanged();
        }
        // 아이템 내 각 위젯에 데이터 반영
        profile_img.setImageBitmap(Custom_Item.getProfile_img());
        profile_img.setBackground(new ShapeDrawable(new OvalShape()));
        profile_img.setClipToOutline(true);
        FrameLayout frameLayout = convertView.findViewById(R.id.prev_img_frame);
        if (Custom_Item.getPrev_img() != null) {
            frameLayout.setVisibility(View.VISIBLE);
            prev_img.setImageBitmap(Custom_Item.getPrev_img());
            prev_text.setVisibility(View.GONE);
            if (Custom_Item.getisGIF()) {
                convertView.findViewById(R.id.gif).setVisibility(View.VISIBLE);
                convertView.findViewById(R.id.mov).setVisibility(View.GONE);
            } else {
                convertView.findViewById(R.id.gif).setVisibility(View.GONE);
                if (Custom_Item.getisMov()) {
                    if (MySharedPreferences.getBlock_Mov()) {
                        Custom_ItemList.remove(position);
                        Custom_Item = Custom_ItemList.get(position);
                        notifyDataSetChanged();
                    } else
                        convertView.findViewById(R.id.mov).setVisibility(View.VISIBLE);
                } else
                    convertView.findViewById(R.id.mov).setVisibility(View.GONE);
            }
        } else if (Custom_Item.getPrev_text() != null && Custom_Item.getPrev_text().length() > 0) {
            prev_text.setVisibility(View.VISIBLE);
            prev_text.setText(Custom_Item.getPrev_text());
            frameLayout.setVisibility(View.GONE);
        }
        info_text.setText(Custom_Item.getInform());
        nickname_text.setText(Custom_Item.getNickname());
        title.setText(Custom_Item.getTitle());
        time.setText(Custom_Item.getTime());
        count_img.setText(String.valueOf(Custom_Item.getImg_count()));
        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return Custom_ItemList.get(position);
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    void addItems(Elements elements, Context context) {


        HashMap<Integer, String> hashMap = MySharedPreferences.getHashmap();
        for (Element tmp : elements) {
            Elements elements1 = tmp.select(".sc_usr a");
            String[] strs = elements1.first().attr("href").split("userNo=");
            int userno = Integer.valueOf(strs[1]);
            elements1 = tmp.select(".play_mov");
            boolean is_Mov = false;
            if (elements1.size() > 0) is_Mov = true;
            if (!hashMap.containsKey(userno)&&!(MySharedPreferences.getBlock_Mov()&&is_Mov))
                Custom_ItemList.add(new Custom_Item(tmp, userno, is_Mov,context));
        }
    }
}


package com.example.a1117p.bboom;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class Post_List_Adapter extends BaseAdapter {
    private ArrayList<Post_List_Item> post_list_items = new ArrayList<>();

    @Override
    public int getCount() {
        return post_list_items.size();
    }

    @Override
    public Object getItem(int i) {
        return post_list_items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    private ImageView imageView;
    private TextView nickname_text, title_text;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Context context = parent.getContext();
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.post_list_item_view, parent, false);
        }
        imageView = convertView.findViewById(R.id.profile_img);
        nickname_text = convertView.findViewById(R.id.nickname_tv);
        title_text = convertView.findViewById(R.id.title);
        imageView.setImageBitmap(post_list_items.get(position).getPf_img());
        nickname_text.setText(post_list_items.get(position).getWriter());
        title_text.setText(post_list_items.get(position).getTitle());
        return convertView;
    }

    void addItem(int Post_no,String title, String writer, String pf_img, Context context) {
        post_list_items.add(new Post_List_Item(Post_no,title, writer, pf_img, context));
    }
}

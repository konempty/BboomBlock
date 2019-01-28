package com.example.a1117p.bboom;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;

public class NewBboom extends Fragment {
    Document document;
    Elements elements;
    int Posts_count = 30, last_post;
    ListView listView;
    ListViewAdapter listViewAdapter;
    boolean isLoading = false;
    private Handler handler = new Handler(Looper.getMainLooper());
    private final static int inc_v = 30;
    Dialog dialog;
    private Handler mHandler = new Handler(Looper.getMainLooper());

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.newbboom_view, container, false);
        listView = view.findViewById(R.id.new_list);
        listViewAdapter = new ListViewAdapter();
        dialog = ProgressDialog.show(getContext(), "기다려주세요", "내용을 불러오는중입니다.", true, false);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        loadHtml();
        listView.setAdapter(listViewAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getContext(), Web_Activity.class);

                intent.putExtra("url", ((Custom_Item) listViewAdapter.getItem(i)).getUrl());
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final Custom_Item item = (Custom_Item) listViewAdapter.getItem(i);
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                        .setMessage(item.getNickname() + "님을 차단하시겠습니까?")
                        .setPositiveButton("예", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                HashMap<Integer, String> hashMap = MySharedPreferences.getHashmap();
                                hashMap.put(item.getUserno(), item.getNickname());
                                MySharedPreferences.commit_Block_usr(hashMap);
                                listViewAdapter.notifyDataSetChanged();
                                Toast.makeText(getContext(), item.getNickname() + "님이 차단되었습니다.", Toast.LENGTH_LONG).show();
                            }
                        }).setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                builder.create().show();

                return true;
            }
        });
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int first, int visible, int total) {
                if (isLoading && total - first <= 10) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            dialog = ProgressDialog.show(getContext(), "기다려주세요", "다음 내용을 불러오는중입니다.", true, false);
                            dialog.setCancelable(false);
                        }
                    });
                    isLoading = false;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Elements elements1 = new Elements();
                            int count = 0;
                            while (count < 15) {
                                Posts_count += inc_v;
                                try {
                                    document = Jsoup.connect("http://m.bboom.naver.com/board/moreList.json?likeBandNo=&subCategory=&length=" + String.valueOf(Posts_count)).get();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                elements = document.select(".check_visible");
                                for (int i = Posts_count - inc_v; i < Posts_count; i++) {
                                    int post_no = Integer.parseInt(elements.get(i).attr("data-post-no"));
                                    if (post_no < last_post) {
                                        elements1.add(elements.get(i));
                                        count++;
                                        last_post = post_no;
                                    }
                                }
                            }
                            listViewAdapter.addItems(elements1, getContext());
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    listViewAdapter.notifyDataSetChanged();
                                    isLoading = true;
                                }
                            });
                            try {
                                Thread.sleep(1000);
                                if(dialog.isShowing())
                                    dialog.dismiss();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
            }
        });
        return view;
    }


    void loadHtml() { // 웹에서 html 읽어오기
        final Thread t = new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    document = Jsoup.connect("http://m.bboom.naver.com/board/moreList.json?likeBandNo=&subCategory=&length=" + String.valueOf(Posts_count)).get();
                    elements = document.select(".check_visible");

                } catch (Exception e) {
                    e.printStackTrace();
                }
                listViewAdapter.addItems(elements, getContext());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        listViewAdapter.notifyDataSetChanged();
                        isLoading = true;
                        last_post = Integer.parseInt(elements.first().attr("data-post-no"));
                    }
                });
                try {
                    Thread.sleep(1000);
                    if(dialog.isShowing())
                        dialog.dismiss();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start(); // 쓰레드 시작

    }
}

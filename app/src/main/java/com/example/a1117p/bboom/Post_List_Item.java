package com.example.a1117p.bboom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Post_List_Item {
    private String title;
    private String writer;
    private Bitmap pf_img;
    private int post_no;

    Post_List_Item(int no, String ti, String wri, String pf, final Context context) {
        title = ti;
        writer = wri;
        if (pf.indexOf('/') == 0)
            pf = "http://m.bboom.naver.com" + pf;
        final String finalPf = pf;
        new Thread(new Runnable() {
            @Override
            public void run() {
                BboomCache bboomCache = new BboomCache(context);
                if ((pf_img = bboomCache.Read(finalPf)) == null) {
                    try {
                        URL url1 = new URL(finalPf);
                        HttpURLConnection connection = (HttpURLConnection) url1.openConnection();
                        connection.setDoInput(true);
                        connection.connect();
                        InputStream is = connection.getInputStream();
                        pf_img = BitmapFactory.decodeStream(is);
                        bboomCache.Write(finalPf,pf_img);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        post_no = no;
    }

    public Bitmap getPf_img() {
        return pf_img;
    }

    public String getWriter() {
        return writer;
    }

    public String getTitle() {
        return title;
    }

    public int getPost_no() {
        return post_no;
    }
}

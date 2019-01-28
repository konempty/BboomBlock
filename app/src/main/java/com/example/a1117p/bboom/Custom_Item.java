package com.example.a1117p.bboom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Custom_Item {
    private String nickname, prev_text, bboom, reply, title, time, url;
    private int userno, img_count, postno;
    private boolean isGIF = false, isMov = false;
    private Bitmap profile_img, prev_img;

    Custom_Item(final Element document, int usr, final boolean mov, Context context) {
        final BboomCache bboomCache = new BboomCache(context);
        Elements elements = document.select(".thumbnail");
        String str;
        if (elements.size() != 0) {


            final Elements finalElements = elements;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String str = finalElements.first().attr("src");
                    if ((prev_img = bboomCache.Read(str)) == null) {
                        // 값을 출력하기
                        try {
                            URL url1 = new URL(str);
                            HttpURLConnection connection = (HttpURLConnection) url1.openConnection();
                            connection.setDoInput(true);
                            connection.connect();
                            InputStream is = connection.getInputStream();
                            prev_img = BitmapFactory.decodeStream(is);
                            bboomCache.Write(str, prev_img);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    Elements elements1 = document.select(".play_gif");
                    if (elements1.size() > 0)
                        isGIF = true;
                    else {
                        isMov = mov;
                    }
                }
            }).start();

        } else {
            prev_text = document.select(".txt_cont p").toString().replace("<p class=\"dsc\">", "").replace("<br>", "\n").replace("</p>", "");
        }
        postno = Integer.valueOf(document.attr("data-post-no"));
        elements = document.select(".pf_img img");
        str = elements.first().attr("src");
        // 값을 출력하기
        if (str.indexOf('/') == 0)
            str = "http://m.bboom.naver.com" + str;
        final String finalStr = str;
        new Thread(new Runnable() {
            @Override
            public void run() {
                if ((profile_img = bboomCache.Read(finalStr)) == null) {
                    try {
                        URL url1 = new URL(finalStr);
                        HttpURLConnection connection = (HttpURLConnection) url1.openConnection();
                        connection.setDoInput(true);
                        connection.connect();
                        InputStream is = connection.getInputStream();
                        profile_img = BitmapFactory.decodeStream(is);
                        bboomCache.Write(finalStr,profile_img);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        elements = document.select(".sc_usr a");
        nickname = elements.first().text();
        userno = usr;
        elements = document.select(".pht_cnt em");

        img_count = Integer.valueOf(elements.text());
        elements = document.select(".bundle_sc a"); //뿜업하기0댓글수0보내기 <-이런포멧
        bboom = elements.get(0).text().replace("뿜업하기", "");
        reply = elements.get(1).text().replace("댓글 수", "");

        elements = document.select(".tit a");

        title = elements.text();
        if (title.length() > 30)
            title = title.substring(0, 25) + "...";

        elements = document.select(".date");

        time = elements.text();

        elements = document.select(".tit a");

        url = "http://m.bboom.naver.com" + elements.attr("href");
    }

    String getNickname() {
        return nickname;
    }

    Bitmap getPrev_img() {
        return prev_img;
    }

    Bitmap getProfile_img() {
        return profile_img;
    }

    String getInform() {
        return String.format("뿜 :%-5s 댓글 :%-5s", bboom, reply);
    }

    String getPrev_text() {
        return prev_text;
    }

    public String getTime() {
        return time;
    }

    public String getTitle() {
        return title;
    }

    int getImg_count() {
        return img_count;
    }

    int getUserno() {
        return userno;
    }

    String getUrl() {
        return url;
    }

    boolean getisGIF() {
        return isGIF;
    }

    boolean getisMov() {
        return isMov;
    }

    public int getPostno() {
        return postno;
    }
}

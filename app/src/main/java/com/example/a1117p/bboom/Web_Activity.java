package com.example.a1117p.bboom;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpURLConnection;
import java.net.URL;

public class Web_Activity extends AppCompatActivity {
    WebView webView;
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(final Message msg) {
            // Get link-URL.
            final String tmp = (String) msg.getData().get("url");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String url = tmp.split("=")[2];

                    // Do something with it.
                    if (url == null)
                        url = "";

                    String wallpaper_url = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                            + File.separator + "Bboom_Download" + File.separator;
                    Bitmap profile_img;
                    String filename = "";
                    try {
                        URL url1 = new URL(url);
                        HttpURLConnection connection = (HttpURLConnection) url1.openConnection();
                        connection.setDoInput(true);
                        connection.connect();
                        InputStream is = connection.getInputStream();
                        profile_img = BitmapFactory.decodeStream(is);
                        filename = url.split("/")[5];
                        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                            File dir = new File(wallpaper_url);
                            if (!dir.exists())
                                dir.mkdirs();
                            File file = new File(wallpaper_url + filename);
                            file.createNewFile();


                            FileOutputStream out = new FileOutputStream(file);
                            profile_img.compress(Bitmap.CompressFormat.JPEG, 100, out);
                            out.flush();
                            out.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();



        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        webView = findViewById(R.id.web);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

            }
        });
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                WebView.HitTestResult hitTestResult = webView.getHitTestResult();

                int a = hitTestResult.getType();
                switch (hitTestResult.getType()) {
                    case WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE:
                        new AlertDialog.Builder(Web_Activity.this)
                                .setMessage("선택한 이미지를 저장하시겠습니까?")
                                .setPositiveButton("예", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Message msg = mHandler.obtainMessage();
                                        webView.requestFocusNodeHref(msg);
                                    }
                                }).setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).create().show();
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }


}

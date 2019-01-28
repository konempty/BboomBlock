package com.example.a1117p.bboom;

import android.support.v4.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.net.DatagramPacket;
import java.net.InetAddress;

public class MainActivity extends AppCompatActivity {
    BestBboom bestBboom;
    NewBboom newBboom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // scrollView = findViewById(R.id.sc);
        // frameLayout = findViewById(R.id.frame);
        //  mWebView = findViewById(R.id.wb);
        MySharedPreferences.init(MainActivity.this);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        bestBboom = new BestBboom();
        fragmentTransaction.add(R.id.fragment, bestBboom);
        fragmentTransaction.commit();
        findViewById(R.id.best_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (bestBboom == null)
                    bestBboom = new BestBboom();
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.fragment, bestBboom);
                fragmentTransaction.commit();
            }
        });

        findViewById(R.id.new_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (newBboom == null)
                    newBboom = new NewBboom();
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.fragment, newBboom);
                fragmentTransaction.commit();
            }
        });
        findViewById(R.id.MY_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Intent intent = new Intent(MainActivity.this, Web_Activity.class);
                intent.putExtra("url", "http://m.bboom.naver.com/profile/alarm/list.nhn");
                startActivity(intent);
            }
        });
     /*   mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                view.loadUrl("javascript:window.Android.getHtml(document.getElementsByTagName('html')[0].innerHTML);"); //<html></html> 사이에 있는 모든 html을 넘겨준다.
            }
        });

        mWebView.getSettings().setJavaScriptEnabled(true); //Javascript를 사용하도록 설정
        mWebView.addJavascriptInterface(new MyJavascriptInterface(), "Android");

        mWebView.loadUrl("http://m.bboom.naver.com");*/
        //loadHtml();
       /* findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (b) {
                    frameLayout.bringChildToFront(mWebView);
                } else {

                    frameLayout.bringChildToFront(scrollView);
                }
                b = !b;
            }
        });*/


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.block_list_item:
                Intent intent = new Intent(MainActivity.this, Bloack_List_Activity.class);
                startActivity(intent);
                break;
            case R.id.setting:
                intent = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.find_post:
                intent = new Intent(MainActivity.this, PostParseActivity.class);
                startActivity(intent);
                break;
            case R.id.find_user:
                intent = new Intent(MainActivity.this, UserParseActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}

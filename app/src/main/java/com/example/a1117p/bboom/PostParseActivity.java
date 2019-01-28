package com.example.a1117p.bboom;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class PostParseActivity extends AppCompatActivity {
    EditText editText;
    String myJSON;
    ProgressDialog dialog;
    Post_List_Adapter adapter;
    ListView listView;
    JSONArray data = null;

    private static final String TAG_RESULTS = "result";
    private static final String POST_NO = "POSTNO";
    private static final String TITLE = "TITLE";
    private static final String NICKNAME = "NICK";
    private static final String PF_IMG = "pf_img";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_parse);
        editText = findViewById(R.id.seach_post_et);
        listView = findViewById(R.id.post_list);
        adapter = new Post_List_Adapter();
        findViewById(R.id.find_post_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData("http://konempty.maru.net/post2.php?search=" + editText.getText().toString());
                dialog = ProgressDialog.show(PostParseActivity.this, "기다려주세요", "게시글 리스트를 불러오는 중입니다.", true, false);

            }
        });
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(PostParseActivity.this, Web_Activity.class);

                intent.putExtra("url", "https://m.bboom.naver.com/board/get.nhn?postNo=" + String.valueOf(((Post_List_Item)adapter.getItem(i)).getPost_no()));
                startActivity(intent);
            }
        });
    }

    protected void showList() {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            data = jsonObj.getJSONArray(TAG_RESULTS);

            for (int i = 0; i < data.length(); i++) {
                JSONObject c = data.getJSONObject(i);
                int postno = c.getInt(POST_NO);
                String title = c.getString(TITLE);
                String nickname = c.getString(NICKNAME);
                String pf_img = c.getString(PF_IMG);

                adapter.addItem(postno, title, nickname, pf_img, this);


            }
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                            dialog.cancel();
                        }
                    });
                }
            }).start();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void getData(String url) {
        class GetDataJSON extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {

                String uri = params[0];

                BufferedReader bufferedReader = null;
                try {


                    StringBuilder sb = new StringBuilder();
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }

                    return sb.toString().trim();

                } catch (Exception e) {
                    return null;
                }


            }

            @Override
            protected void onPostExecute(String result) {
                myJSON = result;
                showList();
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(url);
    }
}

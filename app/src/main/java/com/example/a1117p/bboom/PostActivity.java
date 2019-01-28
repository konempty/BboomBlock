package com.example.a1117p.bboom;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class PostActivity extends AppCompatActivity {
    ListView listView;
    ImageView pf;
    TextView nick;
    ArrayAdapter<String> arrayAdapter;
    ArrayList<Integer> postsno;
    String myJSON;

    private static final String TAG_RESULTS = "result";
    private static final String POSTNO = "POSTNO";
    private static final String TITLE = "TITLE";

    JSONArray posts = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        listView = findViewById(R.id.post_list);
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        postsno = new ArrayList<>();
        Intent intent = getIntent();
        getData("http://konempty.maru.net/post.php?userno="+intent.getIntExtra("userno",0));
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(PostActivity.this, Web_Activity.class);

                intent.putExtra("url", "https://m.bboom.naver.com/board/get.nhn?postNo=" + String.valueOf(postsno.get(i)));
                startActivity(intent);
            }
        });
        pf = findViewById(R.id.pf_img_iv);
        pf.setImageBitmap((Bitmap) intent.getParcelableExtra("pf_img"));
        nick = findViewById(R.id.nickname_tv);
        nick.setText(intent.getStringExtra("nick"));

    }

    protected void showList() {
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            posts = jsonObj.getJSONArray(TAG_RESULTS);

            for (int i = 0; i < posts.length(); i++) {
                JSONObject c = posts.getJSONObject(i);
                int postno = c.getInt(POSTNO);
                String title = c.getString(TITLE);

                arrayAdapter.add(title);
                postsno.add(postno);


            }
            arrayAdapter.notifyDataSetChanged();
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
                        sb.append(json).append("\n");
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

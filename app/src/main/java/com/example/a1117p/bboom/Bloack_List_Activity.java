package com.example.a1117p.bboom;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.ListView;

public class Bloack_List_Activity extends AppCompatActivity {
    BaseAdapter adapter = new Block_List_Adapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_bloack__list_);
        ListView listView = findViewById(R.id.block_list);
        listView.setAdapter(adapter);
    }
}

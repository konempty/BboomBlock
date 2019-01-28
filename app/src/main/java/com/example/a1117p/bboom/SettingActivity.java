package com.example.a1117p.bboom;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

public class SettingActivity extends AppCompatActivity {
    Switch aSwitch;
    Boolean block, isChanged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        aSwitch = findViewById(R.id.mov_switch);
        aSwitch.setTextOff("동영상 차단 꺼짐");
        aSwitch.setTextOn("동영상 차단 켜짐");
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                block = b;
                isChanged = true;
            }
        });
        aSwitch.setChecked(MySharedPreferences.getBlock_Mov());
        Button save = findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isChanged = false;
                MySharedPreferences.setBlock_Mov(block);
                finish();
            }
        });
        Button discard = findViewById(R.id.discart);
        discard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isChanged = false;
                finish();
            }
        });

    }

   /* @Override
    public void onBackPressed() {
        if (isChanged) {
            new AlertDialog.Builder(SettingActivity.this)
                    .setMessage("설정을 버리시겠습니까?")
                    .setPositiveButton("예", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    })
                    .setNegativeButton("아니요", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }).create().show();
        } else
            super.onBackPressed();
    }*/
}

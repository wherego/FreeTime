package com.zx.freetime.ui.menu;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.zx.freetime.R;
import com.zx.freetime.widget.ZSwitch.SlideSwitch;

public class NavSettingActivity extends AppCompatActivity {

    SlideSwitch changeSkin;
    SlideSwitch nightMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nav_setting);

        changeSkin = (SlideSwitch) findViewById(R.id.changeskin_switch);
        nightMode = (SlideSwitch) findViewById(R.id.night_mode_switch);

        initListener();
    }


    void initListener() {
        changeSkin.setSlideListener(new SlideSwitch.SlideListener() {
            @Override
            public void open() {
                Toast.makeText(NavSettingActivity.this, "开1", Toast.LENGTH_LONG).show();
            }

            @Override
            public void close() {
                Toast.makeText(NavSettingActivity.this, "关1", Toast.LENGTH_LONG).show();
            }
        });

        nightMode.setSlideListener(new SlideSwitch.SlideListener() {
            @Override
            public void open() {
                Toast.makeText(NavSettingActivity.this, "开2", Toast.LENGTH_LONG).show();

            }

            @Override
            public void close() {
                Toast.makeText(NavSettingActivity.this, "关2", Toast.LENGTH_LONG).show();

            }
        });
    }

    public static void start(Context mContext) {
        Intent intent = new Intent(mContext, NavSettingActivity.class);
        mContext.startActivity(intent);
    }
}

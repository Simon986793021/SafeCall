package com.wind.safecall.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.wind.safecall.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by zhangcong on 2017/4/10.
 */

public class BlackNumActivity extends Activity{
    private TextView activitytitle;
    private TextView back;
    private StringBuffer sb;
    private ListView listview;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_black_num);
        activitytitle= (TextView) findViewById(R.id.tv_activity_toolbar_center);
        activitytitle.setText("黑名单");

        listview= (ListView) findViewById(R.id.lv_black_num);

        back= (TextView) findViewById(R.id.tv_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        showBlackNum();
    }

    private void showBlackNum() {
        ArrayList<String> list = this.getIntent().getStringArrayListExtra("list");
        Log.i(">>>>>", list.toString());
        if (list != null) {
            listview.setAdapter(new ArrayAdapter<>(BlackNumActivity.this, R.layout.item_black_num, R.id.tv_black_num, list));
        }
    }
}

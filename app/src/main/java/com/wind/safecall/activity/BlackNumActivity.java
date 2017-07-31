package com.wind.safecall.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.wind.safecall.R;
import com.wind.safecall.contentprovider.MyContentProvider;
import com.wind.safecall.view.SwipeListLayout;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Created by zhangcong on 2017/4/10.
 */

public class BlackNumActivity extends Activity{
    private TextView activitytitle;
    private TextView back;
    private StringBuffer sb;
    private ListView listview;
    private Set<SwipeListLayout> sets = new HashSet();
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
        listview.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    //当listview开始滑动时，若有item的状态为Open，则Close，然后移除
                    case SCROLL_STATE_TOUCH_SCROLL:
                        if (sets.size() > 0) {
                            for (SwipeListLayout s : sets) {
                                s.setStatus(SwipeListLayout.Status.Close, true);
                                sets.remove(s);
                            }
                        }
                        break;

                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

            }
        });
    }

    private void showBlackNum() {
        ArrayList<String> list = this.getIntent().getStringArrayListExtra("list");
        Log.i(">>>>>", list.toString());
        if (list != null) {
            listview.setAdapter(new BlackNumBaseAdapter(list));
        }


    }
    class MyOnSlipStatusListener implements SwipeListLayout.OnSwipeStatusListener {

        private SwipeListLayout slipListLayout;

        public MyOnSlipStatusListener(SwipeListLayout slipListLayout) {
            this.slipListLayout = slipListLayout;
        }

        @Override
        public void onStatusChanged(SwipeListLayout.Status status) {
            if (status == SwipeListLayout.Status.Open) {
                //若有其他的item的状态为Open，则Close，然后移除
                if (sets.size() > 0) {
                    for (SwipeListLayout s : sets) {
                        s.setStatus(SwipeListLayout.Status.Close, true);
                        sets.remove(s);
                    }
                }
                sets.add(slipListLayout);
            } else {
                if (sets.contains(slipListLayout))
                    sets.remove(slipListLayout);
            }
        }

        @Override
        public void onStartCloseAnimation() {

        }

        @Override
        public void onStartOpenAnimation() {

        }

    }
    class BlackNumBaseAdapter extends BaseAdapter {
        private ArrayList<String> list=new ArrayList<String>();
        public BlackNumBaseAdapter(ArrayList<String> list)
        {
            this.list=list;
        }
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
           ViewHolder viewHolder;
            if (convertView==null)
            {
                viewHolder=new ViewHolder();
                convertView= LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_black_num,null);
                viewHolder.textView= (TextView) convertView.findViewById(R.id.tv_black_num);
                convertView.setTag(viewHolder);
            }
            else
            {
                viewHolder= (ViewHolder) convertView.getTag();
            }
            viewHolder.textView.setText(list.get(position));
            TextView textViewdelete= (TextView) convertView.findViewById(R.id.tv_delete);
            final SwipeListLayout swipeListLayout= (SwipeListLayout) convertView.findViewById(R.id.sll_main);
            swipeListLayout.setOnSwipeStatusListener(new MyOnSlipStatusListener(swipeListLayout));

            /*
            黑名单删除
             */
            textViewdelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    swipeListLayout.setStatus(SwipeListLayout.Status.Close, true);
                    Log.i(">>>",position+"po");
                    Log.i(">>>",list.get(position));
                    String [] args=new String[]{list.get(position).toString()};
                    getContentResolver().delete(MyContentProvider.uri,"blacknum=?",args);
                    list.remove(position);
                    notifyDataSetChanged();


                }
            });


            return convertView;
        }
        private class ViewHolder{
            private TextView textView;
        }
    }


}

package com.wind.safecall.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.wind.safecall.R;
import com.wind.safecall.contentprovider.MyContentProvider;
import com.wind.safecall.service.BlackNumService;
import com.wind.safecall.utils.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button button;
    private Button addblacknumbotton;
    private EditText editText;
    private Button quertblacknumbutton;
    private Button startButton;
    private Button stopButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button= (Button) findViewById(R.id.bt_call);
        button.setOnClickListener(this);
        addblacknumbotton= (Button) findViewById(R.id.bt_addblacknum);
        addblacknumbotton.setOnClickListener(this);
        quertblacknumbutton= (Button) findViewById(R.id.bt_queryblacknum);
        quertblacknumbutton.setOnClickListener(this);
        startButton= (Button) findViewById(R.id.bt_startService);
        startButton.setOnClickListener(this);
        stopButton= (Button) findViewById(R.id.bt_stopservice);

    }
    /*
    启动服务
     */
    private void stService() {
        Intent intent=new Intent(MainActivity.this, BlackNumService.class);
        startService(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.bt_call :
                Intent intent=new Intent(MainActivity.this,PhoneCallActivity.class);
                startActivity(intent);
                break;
            case R.id.bt_addblacknum:
                addBlackNumDialog();
                break;
            case R.id.bt_queryblacknum:
                queryBlackNum();
                Log.i(">>>","<<<");
                break;
            case R.id.bt_startService:
                stService();//开启服务
                Utils.showToast("已经开启黑名单服务",MainActivity.this);
                break;
            case R.id.bt_stopservice:
                spService();
            default:
                break;
        }
    }

    private void spService() {
        Intent intent=new Intent(MainActivity.this,BlackNumService.class);
        stopService(intent);
    }

    private void queryBlackNum() {
        String array[]={"blacknum"};
        Cursor cursor=getContentResolver().query(MyContentProvider.uri,array,null,null,null);
        int blacknumindex=cursor.getColumnIndex("blacknum");
        Log.i(">>>",blacknumindex+"");
        cursor.moveToFirst();
        ArrayList<String> list=new ArrayList<>();
        while (!cursor.isAfterLast())
        {
            String blacknum=cursor.getString(blacknumindex);
            list.add(blacknum);
            cursor.moveToNext();
            Log.i(">>>",">>>>");
            Log.i("Simon",list.toString());
        }
        Intent intent=new Intent(MainActivity.this,BlackNumActivity.class);
        intent.putStringArrayListExtra("list", list);
//        Bundle bundle=new Bundle();
//        bundle.putSerializable("list", (Serializable) list);
//        intent.putExtras(bundle);
        startActivity(intent);


    }

    /*
    添加黑名单dialog
     */
    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    private void addBlackNumDialog() {

        final AlertDialog alertdialog=new AlertDialog.Builder(MainActivity.this).create();
        alertdialog.setCanceledOnTouchOutside(true);
        View view= LayoutInflater.from(MainActivity.this).inflate(R.layout.dialog_add_black_num,null,false);
        alertdialog.show();
        //dialog的edittext默认不能显示软键盘，加上这句唤起软键盘
        alertdialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        alertdialog.setContentView(view);
        alertdialog.getWindow().setGravity(Gravity.CENTER);
        Button surebutton= (Button) view.findViewById(R.id.bt_sure);
        Button cancelbutton= (Button) view.findViewById(R.id.bt_cancel);
        editText= (EditText) view.findViewById(R.id.et_black_phone_num);




        /*
        确认添加黑名单
         */
        surebutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String blacknum=editText.getText().toString().trim();
                if (Utils.checkCellphone(blacknum))
                {
                    alertdialog.cancel();
                    insertData(blacknum);
                }
                else
                {
                    Utils.showToast("请输入正确的手机号码",MainActivity.this);
                }
            }
        });
        /*
        取消添加黑名单
         */
        cancelbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertdialog.cancel();
            }
        });
    }
    private void insertData(String blacknum) {
        ContentValues contentValues=new ContentValues();
        contentValues.put("blacknum",blacknum);
        Uri uri=getContentResolver().insert(MyContentProvider.uri,contentValues);
        Utils.showToast(uri.toString(),MainActivity.this);

    }
}

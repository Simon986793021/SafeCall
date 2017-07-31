package com.wind.safecall.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wind.safecall.R;
import com.wind.safecall.utils.Utils;

/**
 * Created by zhangcong on 2017/4/8.
 */

public class PhoneCallActivity extends Activity implements View.OnClickListener {
    private EditText editText;
    private Button button;
    private String phonenum;
    private TextView back;
    private TextView textView;
    private static final int CALL_PHONE_REQUEST_CODE = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_call);
        editText = (EditText) findViewById(R.id.et_phonenum);
        textView = (TextView) findViewById(R.id.tv_activity_toolbar_center);
        textView.setText("拨打电话");
        back = (TextView) findViewById(R.id.tv_back);
        back.setOnClickListener(this);
        button = (Button) findViewById(R.id.bt_makecall);
        button.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_makecall:
                phonenum = editText.getText().toString().trim();
                if (!Utils.checkTelephone(phonenum) && !Utils.checkCellphone(phonenum)) {
                    Log.i(">>>", phonenum);
                    Utils.showToast("请输入正确的手机号码", PhoneCallActivity.this);
                } else {
                    testCall();

                }


                break;
            case R.id.tv_back:
                finish();
                break;
            default:
                break;
        }
    }
    public void testCall( )
    {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    CALL_PHONE_REQUEST_CODE);
        } else
        {
            callPhone();
        }
    }
    public void callPhone() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + phonenum);
        intent.setData(data);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(intent);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CALL_PHONE_REQUEST_CODE) {
            if (grantResults != null && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phonenum));
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(intent);
            } else {
                // Permission Denied
                Toast.makeText(this,"权限被拒绝了",Toast.LENGTH_SHORT).show();
            }
        }
    }


}

package com.wind.safecall.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.wind.safecall.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhangcong on 2017/4/8.
 */

public class Utils {
    /*
    自定义Toast
     */
    public static void showToast(String string, Context context)
    {
        LayoutInflater layoutInflater=LayoutInflater.from(context);
        View view=layoutInflater.inflate(R.layout.utils_toast,null,false);
        TextView textView= (TextView) view.findViewById(R.id.tv_toast);
        textView.setText(string);
        Toast toast=new Toast(context);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(view);
        toast.show();
    }
    /**
      * 验证手机号码
      *
      * 移动号码段:139、138、137、136、135、134、150、151、152、157、158、159、182、183、187、188、147
      * 联通号码段:130、131、132、136、185、186、145
      * 电信号码段:133、153、180、189
      *
      * @param cellphone
      * @return
      */
    public static boolean checkCellphone(String cellphone) {
        String regex = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8}$";
        Pattern pattern=Pattern.compile(regex);
        Matcher matcher=pattern.matcher(cellphone);
        return matcher.matches();
    }
    /**
      * 验证固话号码
      * @param telephone
      * @return
      */
    public static boolean checkTelephone(String telephone) {
        String regex = "^(0\\d{2}-\\d{8}(-\\d{1,4})?)|(0\\d{3}-\\d{7,8}(-\\d{1,4})?)$";
        Pattern pattern=Pattern.compile(regex);
        Matcher matcher=pattern.matcher(telephone);
        return matcher.matches();
    }

}

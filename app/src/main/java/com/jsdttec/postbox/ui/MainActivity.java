package com.jsdttec.postbox.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import com.alimuzaffar.lib.pin.PinEntryEditText;
import com.jsdttec.postbox.R;
import com.jsdttec.postbox.util.QRCode;
import com.jsdttec.postbox.view.CustomToast;
import com.xnumberkeyboard.android.XNumberKeyboardView;

/**
 * Created by Android Studio.
 * Author : zhangzhongqiang
 * Email  : zhangzhongqiang@jsdttec.com
 * Time   : 2017/05/16 下午 4:35
 * Desc   : MainActivity
 */

public class MainActivity extends AppCompatActivity implements XNumberKeyboardView.IOnKeyboardListener {

    PinEntryEditText editText;
    ImageView imageView;
    XNumberKeyboardView keyboardView;

    String str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = (PinEntryEditText) findViewById(R.id.edit_text);
        imageView = (ImageView) findViewById(R.id.iv_qrcode);
        keyboardView = (XNumberKeyboardView) findViewById(R.id.view_keyboard);
        keyboardView.setIOnKeyboardListener(this);

//        //第一种方式
//        Bitmap bitmap = QRCodeUtil.createQRCodeBitmap("https://www.baidu.com", 500, 500);

//        // 第二种方式
//        Bitmap bitmap = null;
//        try {
//            bitmap = EncodingHandler.createQRCode("http://www.sohu.com", 500);
//        } catch (WriterException e) {
//            e.printStackTrace();
//        }

        // 第三种方式(中间带logo)
        Bitmap bitmap = QRCode.createQRCodeWithLogo("http://www.jsdttec.com", 500,
                BitmapFactory.decodeResource(getResources(), R.mipmap.icon));
        imageView.setImageBitmap(bitmap);
    }

    @Override
    public void onInsertKeyEvent(String text) {
        editText.append(text);
        str = editText.getText().toString();
        if (str.length() == 6) {
            if (str.equals("123456")) {
                editText.setText("");
//                new SweetAlertDialog(MainActivity.this, SweetAlertDialog.SUCCESS_TYPE)
//                        .setTitleText("")
//                        .setContentText("开箱成功")
//                        .show();
//                Toasty.success(MainActivity.this, "开箱成功", Toast.LENGTH_SHORT, true).show();
                CustomToast.INSTANCE.showToast(MainActivity.this, "开箱成功，请取走物品", R.mipmap.ic_box_open, true);
            } else {
                editText.setText("");
//                new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE)
//                        .setTitleText("")
//                        .setContentText("提取码输入错误")
//                        .show();
//                Toasty.error(MainActivity.this, "提取码输入错误", Toast.LENGTH_SHORT, true).show();
                CustomToast.INSTANCE.showToast(MainActivity.this, "提取码输入错误", R.mipmap.ic_box_close, false);
            }
        }
    }

    @Override
    public void onDeleteKeyEvent() {
        int start = editText.length() - 1;
        if (start >= 0) {
            editText.getText().delete(start, start + 1);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (editText != null) {
//            editText.setOnPinEnteredListener(new PinEntryEditText.OnPinEnteredListener() {
//                @Override
//                public void onPinEntered(CharSequence str) {
//                    if (str.length() == 6 && str.toString().equals("123456")) {
//                        Toast.makeText(MainActivity.this, "密码正确", Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(MainActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
//        }
    }
}

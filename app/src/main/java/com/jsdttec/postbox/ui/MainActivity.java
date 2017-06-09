package com.jsdttec.postbox.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import com.alimuzaffar.lib.pin.PinEntryEditText;
import com.jsdttec.postbox.Constants;
import com.jsdttec.postbox.R;
import com.jsdttec.postbox.util.AESCoder;
import com.jsdttec.postbox.util.HexUtil;
import com.jsdttec.postbox.util.QRCode;
import com.jsdttec.postbox.view.CustomToast;
import com.xnumberkeyboard.android.XNumberKeyboardView;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by Android Studio.
 * Author : zhangzhongqiang
 * Email  : zhangzhongqiang@jsdttec.com
 * Time   : 2017/05/16 下午 4:35
 * Desc   : MainActivity
 */

public class MainActivity extends AppCompatActivity implements XNumberKeyboardView.IOnKeyboardListener {

    public static final String TAG = "MainActivity";

    public static final int KEY_SOUND_OPEN = 1;
    public static final int KEY_SOUND_ERROR = 2;
    public static final int KEY_SOUND_CLOSE = 3;

    // 记录密码输入错误的次数
    private int error_count = 0;

    SoundPool mSoundPool;
    private HashMap<Integer, Integer> soundPoolMap;

    PinEntryEditText editText;
    ImageView imageView;
    ImageView iv_state;
    XNumberKeyboardView keyboardView;

    String str;
    String url;
    String randomStr;

    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = (PinEntryEditText) findViewById(R.id.edit_text);
        imageView = (ImageView) findViewById(R.id.iv_qrcode);
        iv_state = (ImageView) findViewById(R.id.iv_state);
        keyboardView = (XNumberKeyboardView) findViewById(R.id.view_keyboard);
        keyboardView.setIOnKeyboardListener(this);

        mSoundPool = new SoundPool(10, AudioManager.STREAM_ALARM, 0);

        soundPoolMap = new HashMap<Integer, Integer>();
        soundPoolMap.put(KEY_SOUND_OPEN, mSoundPool.load(this, R.raw.open, 1));
        soundPoolMap.put(KEY_SOUND_ERROR, mSoundPool.load(this, R.raw.error, 1));
        soundPoolMap.put(KEY_SOUND_CLOSE, mSoundPool.load(this, R.raw.close, 1));

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
//        Bitmap bitmap = QRCode.createQRCodeWithLogo("http://www.jsdttec.com", 500,
//                BitmapFactory.decodeResource(getResources(), R.mipmap.icon));
//        imageView.setImageBitmap(bitmap);
        iv_state.setBackgroundResource(R.mipmap.ic_close);
        createQRCode();
    }

    // 生成二维码
    private void createQRCode() {
        String encodedStr = null;
        byte[] key = HexUtil.hexToBytes(Constants.ENCODE);

        String str = getRandomNumber();

        Log.e(TAG, "生成的16位数字: " + str);

        byte[] value = str.getBytes();
        byte[] encoded = AESCoder.ecbEnc(value, key);
        encodedStr = HexUtil.bytesToHex(encoded);

        Log.e(TAG, "加密后: " + encodedStr);

        url = Constants.URL + "?boxId=" + Constants.BOX_ID + "&mkey=" + encodedStr;

        Log.e(TAG, "组装的URL: " + url);

        byte[] t = AESCoder.ecbDec(HexUtil.hexToBytes(encodedStr), key);

        Log.e(TAG, "解密后: " + new String(t));

        bitmap = QRCode.createQRCodeWithLogo(url, 500,
                BitmapFactory.decodeResource(getResources(), R.mipmap.icon));
        imageView.setImageBitmap(bitmap);
    }

    private String getRandomNumber() {
//        randomStr = String.valueOf((int) ((Math.random() * 9 + 1) * 100000));

        randomStr = String.valueOf(getRandomValue(10000000, 100000000));
        randomStr += String.valueOf(getRandomValue(10000000, 100000000));
        return randomStr;
    }

    private int getRandomValue(int minvalue, int maxvalue) {
        Random random = new Random();
        return random.nextInt(maxvalue - minvalue) + minvalue;
    }

    @Override
    public void onInsertKeyEvent(String text) {
        editText.append(text);
        str = editText.getText().toString();
        if (str.length() == 6) {
            if (str.equals(randomStr.substring(0, 6))) {
                error_count = 0;
                editText.setText("");
                iv_state.setBackgroundResource(R.mipmap.ic_open);
//                new SweetAlertDialog(MainActivity.this, SweetAlertDialog.SUCCESS_TYPE)
//                        .setTitleText("")
//                        .setContentText("开箱成功")
//                        .show();
//                Toasty.success(MainActivity.this, "开箱成功", Toast.LENGTH_SHORT, true).show();
                CustomToast.INSTANCE.showToast(MainActivity.this, "开箱成功，请取走物品", R.mipmap.ic_box_open, CustomToast.SUCCESS_STATE);
                mSoundPool.play(soundPoolMap.get(KEY_SOUND_OPEN), 1, 1, 0, 0, 1);
            } else {
                error_count++;
                editText.setText("");
//                new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE)
//                        .setTitleText("")
//                        .setContentText("提取码输入错误")
//                        .show();
//                Toasty.error(MainActivity.this, "提取码输入错误", Toast.LENGTH_SHORT, true).show();
                if (error_count == 3) {
                    error_count = 0;
                    createQRCode();
                    CustomToast.INSTANCE.showToast(MainActivity.this, "提取码输入错误三次，二维码更新，请重新扫描二维码", R.mipmap.ic_error, CustomToast.ERROR_STATE);
                } else {
                    CustomToast.INSTANCE.showToast(MainActivity.this, "提取码输入错误", R.mipmap.ic_error, CustomToast.ERROR_STATE);
                }
                mSoundPool.play(soundPoolMap.get(KEY_SOUND_ERROR), 1, 1, 0, 0, 1);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.lock_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.lock:
                createQRCode();
                iv_state.setBackgroundResource(R.mipmap.ic_close);
                CustomToast.INSTANCE.showToast(MainActivity.this, "箱子已关闭，二维码更新", R.mipmap.ic_box_close, CustomToast.NORMAL_STATE);
                mSoundPool.play(soundPoolMap.get(KEY_SOUND_CLOSE), 1, 1, 0, 0, 1);
                break;
            default:
                break;
        }
        return false;
    }
}

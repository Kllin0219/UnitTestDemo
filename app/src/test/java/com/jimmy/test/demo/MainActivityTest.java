package com.jimmy.test.demo;

import static org.junit.Assert.assertEquals;
import static org.robolectric.Shadows.shadowOf;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowAlertDialog;
import org.robolectric.shadows.ShadowLog;
import org.robolectric.shadows.ShadowToast;
import org.robolectric.util.FragmentTestUtil;

import java.util.List;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 30)
public class MainActivityTest {

    @Before
    public void setUp() {
        //輸出日誌配置，用System.out代替Android的Log.x
        ShadowLog.stream = System.out;
    }

    // 驗證Activity頁面跳轉
    @Test
    public void testOnClick() {
        //創建Activity
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        Assert.assertNotNull(mainActivity);

        //模擬點擊
        mainActivity.findViewById(R.id.btn_login).performClick();

        Intent expectedIntent = new Intent(mainActivity, LoginActivity.class);
        Intent actual = shadowOf(RuntimeEnvironment.application).getNextStartedActivity();
        //驗證是否啓動了期望的Activity
        Assert.assertEquals(expectedIntent.getComponent(), actual.getComponent());
    }

    // 驗證Toast顯示
    @Test
    public void testToast() {
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);
        mainActivity.findViewById(R.id.btn_toast).performClick();

        // 判斷Toast已經彈出
        Assert.assertNotNull(ShadowToast.getLatestToast());
        //驗證捕獲的最近顯示的Toast
        Assert.assertEquals("測試", ShadowToast.getTextOfLatestToast());

        //捕獲所有已顯示的Toast
        List<Toast> toasts = shadowOf(RuntimeEnvironment.application).getShownToasts();
        Assert.assertEquals(toasts.size(), 1);
        Assert.assertEquals(Toast.LENGTH_SHORT, toasts.get(0).getDuration());
    }

    // 驗證Dialog顯示
    @Test
    public void showDialog() {
        MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);

        // 捕獲最近顯示的Dialog
        AlertDialog dialog = ShadowAlertDialog.getLatestAlertDialog();
        // 判斷Dialog尚未彈出
        Assert.assertNull(dialog);

        //點擊按鈕
        mainActivity.findViewById(R.id.btn_dialog).performClick();

        // 捕獲最近顯示的Dialog
        dialog = ShadowAlertDialog.getLatestAlertDialog();
        // 判斷Dialog已經彈出
        Assert.assertNotNull(dialog);
        // 獲取Shadow類進行驗證
        ShadowAlertDialog shadowDialog = Shadows.shadowOf(dialog);
        Assert.assertEquals("測試showDialog", shadowDialog.getMessage());
    }

    // 使用qualifiers加載對應的資源文件
    @Test
    @Config(qualifiers = "zh-rTW")
    public void testString() throws Exception {
        final Context context = ApplicationProvider.getApplicationContext();
        assertEquals(context.getString(R.string.app_name), "單元測試Demo");
    }
}

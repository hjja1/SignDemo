package demo.hjj.com.newsigndemo;

import android.widget.Toast;

public class ToastUtil {
    private static Toast toast;

    public static void showMsg(CharSequence message) {
        if (null == toast) {
            toast = Toast.makeText(MyApplication.getInstance().getApplicationContext(), message, Toast.LENGTH_SHORT);
        } else {
            toast.setText(message);
        }
        toast.show();
    }
}

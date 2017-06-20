package jp.realglobe.android.util.view;

import android.support.annotation.NonNull;
import android.widget.EditText;

/**
 * EditText 周りの便利関数。
 * Created by fukuchidaisuke on 17/06/20.
 */
public final class EditTextUtils {

    /**
     * @param edit         inputType が number の入力欄
     * @param defaultValue 入力欄が空だったときの値
     * @return 入力されている数値
     */
    public static int parseInt(@NonNull EditText edit, int defaultValue) {
        final String text = edit.getText().toString();
        if (text.isEmpty()) {
            return defaultValue;
        }
        return Integer.parseInt(text);
    }

    /**
     * @param edit         inputType が number の入力欄
     * @param defaultValue 入力欄が空だったときの値
     * @return 入力されている数値
     */
    public static long parseLong(@NonNull EditText edit, long defaultValue) {
        final String text = edit.getText().toString();
        if (text.isEmpty()) {
            return defaultValue;
        }
        return Long.parseLong(text);
    }

    /**
     * @param edit         inputType が numberDecimal の入力欄
     * @param defaultValue 入力欄が空だったときの値
     * @return 入力されている数値
     */
    public static float parseFloat(@NonNull EditText edit, float defaultValue) {
        final String text = edit.getText().toString();
        if (text.isEmpty()) {
            return defaultValue;
        }
        return Float.parseFloat(text);
    }

    /**
     * @param edit         inputType が numberDecimal の入力欄
     * @param defaultValue 入力欄が空だったときの値
     * @return 入力されている数値
     */
    public static double parseDouble(@NonNull EditText edit, double defaultValue) {
        final String text = edit.getText().toString();
        if (text.isEmpty()) {
            return defaultValue;
        }
        return Double.parseDouble(text);
    }

    /**
     * @param edit         入力欄
     * @param defaultValue 入力欄が空だったときの値
     * @return 入力されている文字列
     */
    public static String getString(@NonNull EditText edit, String defaultValue) {
        final String text = edit.getText().toString();
        if (text.isEmpty()) {
            return defaultValue;
        }
        return text;
    }

}

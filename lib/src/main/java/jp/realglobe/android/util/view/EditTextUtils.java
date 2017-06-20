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
    static int parseInt(@NonNull EditText edit, int defaultValue) {
        final String text = edit.getText().toString();
        if (text.isEmpty()) {
            return defaultValue;
        }
        return Integer.parseInt(text);
    }

    /**
     * @param edit         inputType が numberDecimal の入力欄
     * @param defaultValue 入力欄が空だったときの値
     * @return 入力されている数値。未入力の場合は 0
     */
    static float parseFloat(@NonNull EditText edit, float defaultValue) {
        final String text = edit.getText().toString();
        if (text.isEmpty()) {
            return defaultValue;
        }
        return Float.parseFloat(text);
    }

}

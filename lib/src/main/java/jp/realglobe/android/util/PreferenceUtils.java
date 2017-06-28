package jp.realglobe.android.util;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;

/**
 * 設定周りの便利関数。
 * Created by fukuchidaisuke on 17/06/27.
 */
public final class PreferenceUtils {

    private PreferenceUtils() {
    }

    /**
     * @param preferences  設定
     * @param defaultValue 設定が空または空文字列だったときの値
     * @return 入力されている文字列。空文字列が返るのは設定が空かつ defaultValue が空文字列の場合のみ
     */
    @NonNull
    public static String getNonEmptyString(@NonNull SharedPreferences preferences, @NonNull String key, @NonNull String defaultValue) {
        final String text = preferences.getString(key, null);
        if (text == null || text.isEmpty()) {
            return defaultValue;
        }
        return text;
    }

}

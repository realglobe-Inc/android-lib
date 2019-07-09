/*----------------------------------------------------------------------
 * Copyright 2017 realglobe Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *----------------------------------------------------------------------*/

package jp.realglobe.android.util;

import android.content.SharedPreferences;

import androidx.annotation.NonNull;


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

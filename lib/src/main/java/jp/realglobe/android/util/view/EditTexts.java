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

package jp.realglobe.android.util.view;

import android.widget.EditText;

import androidx.annotation.NonNull;

/**
 * EditText 周りの便利関数。
 * Created by fukuchidaisuke on 17/06/20.
 */
public final class EditTexts {

    private EditTexts() {
    }

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
     * @return 入力されている文字列。空文字列が返るのは入力欄が空かつ defaultValue が空文字列の場合のみ
     */
    @NonNull
    public static String getNonEmptyString(@NonNull EditText edit, @NonNull String defaultValue) {
        final String text = edit.getText().toString();
        if (text.isEmpty()) {
            return defaultValue;
        }
        return text;
    }

}

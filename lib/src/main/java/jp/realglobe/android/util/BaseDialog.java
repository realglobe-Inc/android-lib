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

import android.app.DialogFragment;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.widget.Toast;

/**
 * ダイアログの基礎クラス。
 * Created by fukuchidaisuke on 17/09/08.
 */
public abstract class BaseDialog extends DialogFragment {

    /**
     * メッセージを画面に表示する
     *
     * @param message メッセージ
     */
    protected void showToast(@NonNull String message) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
        } else {
            getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show());
        }
    }

    /**
     * メッセージを画面に表示する
     *
     * @param resId メッセージのリソース ID
     */
    protected void showToast(int resId) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            Toast.makeText(getActivity(), resId, Toast.LENGTH_LONG).show();
        } else {
            getActivity().runOnUiThread(() -> Toast.makeText(getActivity(), resId, Toast.LENGTH_LONG).show());
        }
    }

    /**
     * 別のダイアログを表示する
     *
     * @param dialog 表示するダイアログ
     */
    protected void showDialog(@NonNull DialogFragment dialog) {
        if (getActivity().isDestroyed()) {
            return;
        }
        dialog.show(getFragmentManager(), "dialog");
    }

}

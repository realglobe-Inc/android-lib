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

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.realglobe.android.function.Consumer;

/**
 * ちょっとした便利関数を追加したアクティビティ。
 * Created by fukuchidaisuke on 17/06/28.
 */
public abstract class BaseActivity extends AppCompatActivity {

    /**
     * 権限確認用コールバック
     */
    private interface Callback {

        /**
         * 権限が許可されたときに実行される
         */
        void onPermitted();

        /**
         * 権限が拒否されたときに実行される
         *
         * @param denied 拒否された権限
         */
        void onDenied(String[] denied);

    }

    private int requestCode;
    private Map<Integer, Callback> requestCallbacks;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestCode = 0;
        this.requestCallbacks = new HashMap<>();
    }

    /**
     * 権限を確認する。
     * UI スレッドから呼ぶこと
     *
     * @param permissions 確認する権限
     * @param onPermitted 許可されたときに呼ばれる関数
     * @param onDenied    拒否されたときに呼ばれる関数
     */
    protected void checkPermission(@NonNull String[] permissions, @Nullable Runnable onPermitted, @Nullable Consumer<String[]> onDenied) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            if (onPermitted != null) {
                onPermitted.run();
            }
        } else {
            int rc = this.requestCode++;
            this.requestCallbacks.put(rc, new Callback() {
                @Override
                public void onPermitted() {
                    if (onPermitted != null) {
                        onPermitted.run();
                    }
                }

                @Override
                public void onDenied(String[] denied) {
                    if (onDenied != null) {
                        onDenied.accept(denied);
                    }
                }
            });
            requestPermissions(permissions, rc);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        final Callback callback = this.requestCallbacks.remove(requestCode);
        if (callback == null) {
            return;
        }

        final List<String> denied = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++) {
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                continue;
            }
            denied.add(permissions[i]);
        }

        if (denied.isEmpty()) {
            callback.onPermitted();
        } else {
            callback.onDenied(denied.toArray(new String[denied.size()]));
        }
    }

    /**
     * メッセージを画面に表示する。
     *
     * @param message メッセージ
     */
    protected void showToast(@NonNull final String message) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        } else {
            runOnUiThread(() -> Toast.makeText(this, message, Toast.LENGTH_LONG).show());
        }
    }

    /**
     * メッセージを画面に表示する。
     *
     * @param resId メッセージのリソース ID
     */
    protected void showToast(int resId) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            Toast.makeText(this, resId, Toast.LENGTH_LONG).show();
        } else {
            runOnUiThread(() -> Toast.makeText(this, resId, Toast.LENGTH_LONG).show());
        }
    }

    /**
     * ダイアログを表示する
     *
     * @param dialog 表示するダイアログ
     */
    protected void showDialog(@NonNull DialogFragment dialog) {
        if (isDestroyed()) {
            return;
        }
        dialog.show(getSupportFragmentManager(), "dialog");
    }

}

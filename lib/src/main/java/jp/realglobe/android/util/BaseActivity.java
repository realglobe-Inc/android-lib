package jp.realglobe.android.util;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ちょっとした便利関数を追加したアクティビティ。
 * Created by fukuchidaisuke on 17/06/28.
 */
public abstract class BaseActivity extends AppCompatActivity {

    /**
     * 権限確認用コールバック
     */
    protected interface PermissionRequestCallback {

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

    // TODO Android Studio 3.0 からは java.util.function.Consumer が使えるっぽい
    protected interface Consumer<T> {
        void accept(T t);
    }

    /**
     * コード中でクラスをつくらずに権限確認コールバックをつくるための関数
     *
     * @param onPermitted 権限が許可されたときに実行される関数
     * @param onDenied    権限が拒否されたときに実行される関数
     * @return 権限確認コールバック
     */
    protected static PermissionRequestCallback makePermissionRequestCallback(Runnable onPermitted, Consumer<String[]> onDenied) {
        return new PermissionRequestCallback() {
            @Override
            public void onPermitted() {
                onPermitted.run();
            }

            @Override
            public void onDenied(String[] denied) {
                onDenied.accept(denied);
            }
        };
    }

    private int requestCode;
    private Map<Integer, PermissionRequestCallback> requestCallbacks;

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
     * @param callback    確認後に呼ばれる関数
     */
    protected void checkPermission(@NonNull String[] permissions, @Nullable PermissionRequestCallback callback) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            if (callback != null) {
                callback.onPermitted();
            }
            return;
        }

        int rc = this.requestCode++;
        this.requestCallbacks.put(rc, callback);
        ActivityCompat.requestPermissions(this, permissions, rc);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (!this.requestCallbacks.containsKey(requestCode)) {
            return;
        }

        final PermissionRequestCallback callback = this.requestCallbacks.remove(requestCode);

        final List<String> denied = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++) {
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                continue;
            }
            denied.add(permissions[i]);
        }

        if (!denied.isEmpty()) {
            if (callback != null) {
                callback.onDenied(denied.toArray(new String[denied.size()]));
            }
            return;
        }

        if (callback != null) {
            callback.onPermitted();
        }
    }

    /**
     * メッセージを画面に重ねて表示する。
     * UI スレッドから呼ぶこと
     *
     * @param message 表示するメッセージ
     */
    protected void showToast(@NonNull final String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

}

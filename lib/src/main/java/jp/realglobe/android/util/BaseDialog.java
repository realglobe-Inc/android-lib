package jp.realglobe.android.util;

import android.app.DialogFragment;
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
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
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

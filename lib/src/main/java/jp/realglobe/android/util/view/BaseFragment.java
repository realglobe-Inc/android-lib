package jp.realglobe.android.util.view;

import android.app.Activity;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.widget.Toast;

/**
 * フラグメントの基礎クラス。
 * Created by fukuchidaisuke on 18/06/25.
 */
public abstract class BaseFragment extends Fragment {

    /**
     * メッセージを画面に表示する
     *
     * @param message メッセージ
     */
    protected void showToast(@NonNull String message) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show();
        } else {
            requireActivity().runOnUiThread(() -> Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show());
        }
    }

    /**
     * メッセージを画面に表示する
     *
     * @param resId メッセージのリソース ID
     */
    protected void showToast(int resId) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            Toast.makeText(requireContext(), resId, Toast.LENGTH_LONG).show();
        } else {
            requireActivity().runOnUiThread(() -> Toast.makeText(getActivity(), resId, Toast.LENGTH_LONG).show());
        }
    }

    /**
     * ダイアログを表示する
     *
     * @param dialog 表示するダイアログ
     */
    protected void showDialog(@NonNull DialogFragment dialog) {
        final Activity activity = getActivity();
        if (activity == null || activity.isDestroyed()) {
            return;
        }
        dialog.show(requireFragmentManager(), "dialog");
    }

}

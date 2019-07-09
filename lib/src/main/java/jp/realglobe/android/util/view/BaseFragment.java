package jp.realglobe.android.util.view;

import android.os.Looper;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

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
            requireActivity().runOnUiThread(() -> Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show());
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
            requireActivity().runOnUiThread(() -> Toast.makeText(requireContext(), resId, Toast.LENGTH_LONG).show());
        }
    }

    /**
     * ダイアログを表示する
     *
     * @param dialog 表示するダイアログ
     */
    protected void showDialog(@NonNull DialogFragment dialog) {
        dialog.show(requireFragmentManager(), "dialog");
    }

}

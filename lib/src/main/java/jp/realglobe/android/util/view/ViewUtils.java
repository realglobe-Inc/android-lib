package jp.realglobe.android.util.view;

import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

/**
 * View 周りの便利関数。
 * Created by fukuchidaisuke on 17/11/21.
 */
public final class ViewUtils {

    private ViewUtils() {
    }

    /**
     * 再帰的に有効/無効を切り替える
     *
     * @param view    設定する一番上の要素
     * @param enabled true なら有効
     */
    public static void setEnabledRecursively(@NonNull View view, boolean enabled) {
        view.setEnabled(enabled);
        if (!(view instanceof ViewGroup)) {
            return;
        }

        final ViewGroup group = (ViewGroup) view;
        for (int i = 0; i < group.getChildCount(); i++) {
            final View child = group.getChildAt(i);
            setEnabledRecursively(child, enabled);
        }
    }

}

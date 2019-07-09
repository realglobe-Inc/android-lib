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

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

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

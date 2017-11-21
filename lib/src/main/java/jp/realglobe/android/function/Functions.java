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

package jp.realglobe.android.function;

/**
 * 便利な関数。
 * Created by fukuchidaisuke on 17/11/20.
 */
public final class Functions {

    private Functions() {
    }

    /**
     * 何もしない Runnable として使える関数
     */
    public static void nop() {
    }

    /**
     * 何もしない Consumer として使える関数
     *
     * @param t   引数
     * @param <T> 引数の型
     */
    public static <T> void nop(T t) {
    }

}

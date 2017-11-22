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

import android.support.annotation.NonNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * IO 周りの便利関数。
 * Created by fukuchidaisuke on 17/11/21.
 */
public final class IoUtils {

    private static final int BUFFER_SIZE = 8192;

    private IoUtils() {
    }

    /**
     * 全部読む
     *
     * @param input 読み込み元
     * @return 読み込んだデータ
     * @throws IOException 読み込みエラー
     */
    @NonNull
    public static byte[] readAll(@NonNull InputStream input) throws IOException {
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final byte[] buff = new byte[BUFFER_SIZE];
        while (true) {
            final int size = input.read(buff);
            if (size <= 0) {
                break;
            }
            output.write(buff, 0, size);
        }
        return output.toByteArray();
    }

}

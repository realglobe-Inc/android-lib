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

package jp.realglobe.android.logger.util;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.File;
import java.util.Date;

import jp.realglobe.lib.util.Dates;

/**
 * ログファイル周りの便利関数。
 * Created by fukuchidaisuke on 17/11/22.
 */
public final class LogFiles {

    private static final String TAG = LogFiles.class.getName();

    private LogFiles() {
    }

    /**
     * @param context コンテクスト
     * @param parents 親ディレクトリ
     * @return ログファイル
     */
    @NonNull
    public static File getLogFile(@NonNull Context context, @NonNull String... parents) {
        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        if (dir == null) {
            Log.w(TAG, "Permanent directory is not found");
            dir = context.getExternalFilesDir(null);
            if (dir == null) {
                dir = context.getFilesDir();
            }
        }
        for (final String parent : parents) {
            dir = new File(dir, parent);
        }
        if (dir.mkdirs()) {
            Log.i(TAG, "Directory " + dir + " was made");
        }

        final String fileName = Dates.getSimpleDayString(new Date(System.currentTimeMillis())) + ".log";

        return new File(dir, fileName);
    }

}

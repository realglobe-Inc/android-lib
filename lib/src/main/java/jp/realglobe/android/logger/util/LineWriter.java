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

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.Closeable;
import java.io.IOException;
import java.io.Writer;

import jp.realglobe.android.function.Consumer;

/**
 * 文字列を改行区切りで書き出す。
 * Created by fukuchidaisuke on 17/07/05.
 */
public class LineWriter implements Closeable {

    private static final String TAG = LineWriter.class.getName();

    private static final class LineWriteHandler extends Handler implements Closeable {

        static final int MSG_WRITE = 0;
        static final int MSG_FLUSH = 1;
        static final int MSG_CLOSE = 2;

        private final Writer writer;
        @NonNull
        private final Consumer<Exception> onError;

        LineWriteHandler(@NonNull Looper looper, @NonNull Writer writer, @Nullable Consumer<Exception> onError) {
            super(looper);

            this.writer = writer;
            this.onError = (onError != null ? onError : (Exception e) -> Log.e(TAG, "Post failed", e));
        }

        @Override
        public void handleMessage(Message msg) {
            try {
                switch (msg.what) {
                    case MSG_WRITE: {
                        writer.write(msg.obj.toString());
                        writer.write(System.lineSeparator());
                        break;
                    }
                    case MSG_FLUSH: {
                        writer.flush();
                        break;
                    }
                    case MSG_CLOSE: {
                        writer.close();
                        break;
                    }
                }
            } catch (IOException e) {
                this.onError.accept(e);
            }
        }

        void write(@NonNull Object obj) {
            sendMessage(obtainMessage(MSG_WRITE, obj));
        }

        void flush() {
            sendEmptyMessage(MSG_FLUSH);
        }

        @Override
        public void close() {
            sendEmptyMessage(MSG_CLOSE);
        }

    }

    private final LineWriteHandler handler;

    /**
     * @param looper  スレッド
     * @param writer  書き出し先
     * @param onError エラー時実行関数
     */
    public LineWriter(@NonNull Looper looper, @NonNull Writer writer, @Nullable Consumer<Exception> onError) {
        this.handler = new LineWriteHandler(looper, writer, onError);
    }

    /**
     * @param obj 書き出すデータ
     */
    public void write(@NonNull Object obj) {
        this.handler.write(obj);
    }

    /**
     * バッファに溜まってる分を書き出す。
     * 呼び出しが終了した時に書き出しが終わっているわけではない
     */
    public void flush() {
        this.handler.flush();
    }

    /**
     * 書き出し先を閉じる。
     * 呼び出しが終了した時に閉じ終わっているわけではない
     */
    @Override
    public void close() {
        this.handler.close();
    }

}

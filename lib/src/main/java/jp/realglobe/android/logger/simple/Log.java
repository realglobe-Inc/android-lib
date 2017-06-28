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

package jp.realglobe.android.logger.simple;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import jp.realglobe.lib.util.Dates;

/**
 * android.util.Log にファイル出力をつけたラッパー。
 * Created by fukuchidaisuke on 17/05/19.
 */
public final class Log {

    private Log() {
    }

    private static final String TAG = Log.class.getName();

    private static final class Entry {

        private static final String[] levelToLabel;

        static {
            final Map<Integer, String> map = new HashMap<>();
            map.put(android.util.Log.VERBOSE, "VERBOSE");
            map.put(android.util.Log.DEBUG, "DEBUG");
            map.put(android.util.Log.INFO, "INFO");
            map.put(android.util.Log.WARN, "WARN");
            map.put(android.util.Log.ERROR, "ERROR");
            map.put(android.util.Log.ASSERT, "ASSERT");

            int max = 0;
            for (int n : map.keySet()) {
                if (n > max) {
                    max = n;
                }
            }

            levelToLabel = new String[max + 1];
            for (Map.Entry<Integer, String> entry : map.entrySet()) {
                levelToLabel[entry.getKey()] = entry.getValue();
            }
        }

        private final long date;
        private final int level;
        private final String tag;
        private final String msg;
        private final Throwable tr;

        Entry(long date, int level, String tag, String msg, Throwable tr) {
            this.date = date;
            this.level = level;
            this.tag = tag;
            this.msg = msg;
            this.tr = tr;
        }

        @Override
        public String toString() {
            final StringBuilder builder = (new StringBuilder()).append(Dates.getRfc3339(new Date(this.date)))
                    .append(" ").append(levelToLabel[this.level])
                    .append("/").append(this.tag);
            if (this.msg != null && !this.msg.isEmpty()) {
                builder.append(": ").append(msg);
            }
            if (this.tr != null) {
                builder.append(": ").append(android.util.Log.getStackTraceString(this.tr));
            }
            return builder.toString();
        }
    }

    private static final class WriteHandler extends Handler {

        static final int MSG_WRITE = 0;
        static final int MSG_FLUSH = 1;
        static final int MSG_CLOSE = 2;

        private final Writer writer;

        WriteHandler(Looper looper, File file) throws IOException {
            super(looper);

            this.writer = new BufferedWriter(new FileWriter(file, true));
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
                e(TAG, "Writing log entry failed", e);
            }
        }
    }

    private static Handler handler;

    /**
     * ログを出力するファイルを設定する。
     * 複数回呼んだ場合、最後の設定が有効になる
     *
     * @param file ログファイル。
     *             null の場合、ログはファイルに出力されない
     */
    public static synchronized void setLogFile(final File file) throws IOException {
        Looper looper = stopWriting();

        if (file == null) {
            if (looper != null) {
                looper.quitSafely();
            }
            return;
        } else if (file.getParentFile().mkdirs()) {
            i(TAG, "Log directory " + file.getParent() + " was generated");
        }

        if (looper == null) {
            final HandlerThread thread = new HandlerThread(Log.class.getName());
            thread.start();
            looper = thread.getLooper();
        }

        handler = new WriteHandler(looper, file);
    }

    private static Looper stopWriting() {
        if (handler == null) {
            return null;
        }

        handler.sendMessage(handler.obtainMessage(WriteHandler.MSG_CLOSE));
        return handler.getLooper();
    }

    private static synchronized void sendEntry(Entry entry) {
        if (handler == null) {
            return;
        }
        handler.sendMessage(handler.obtainMessage(WriteHandler.MSG_WRITE, entry));
    }

    /**
     * バッファに溜まってる分を書き出す。
     * ただし、呼び出しが終了した時点での書き出し完了は保証しない
     */
    public static synchronized void flushLogFile() {
        if (handler == null) {
            return;
        }
        handler.sendMessage(handler.obtainMessage(WriteHandler.MSG_FLUSH));
    }

    public static int v(String tag, String msg, Throwable tr) {
        sendEntry(new Entry(System.currentTimeMillis(), android.util.Log.VERBOSE, tag, msg, tr));
        return android.util.Log.v(tag, msg, tr);
    }

    public static int v(String tag, String msg) {
        sendEntry(new Entry(System.currentTimeMillis(), android.util.Log.VERBOSE, tag, msg, null));
        return android.util.Log.v(tag, msg, null);
    }

    public static int d(String tag, String msg, Throwable tr) {
        sendEntry(new Entry(System.currentTimeMillis(), android.util.Log.DEBUG, tag, msg, tr));
        return android.util.Log.d(tag, msg, tr);
    }

    public static int d(String tag, String msg) {
        sendEntry(new Entry(System.currentTimeMillis(), android.util.Log.DEBUG, tag, msg, null));
        return android.util.Log.d(tag, msg, null);
    }

    public static int i(String tag, String msg, Throwable tr) {
        sendEntry(new Entry(System.currentTimeMillis(), android.util.Log.INFO, tag, msg, tr));
        return android.util.Log.i(tag, msg, tr);
    }

    public static int i(String tag, String msg) {
        sendEntry(new Entry(System.currentTimeMillis(), android.util.Log.INFO, tag, msg, null));
        return android.util.Log.i(tag, msg, null);
    }

    public static int w(String tag, String msg, Throwable tr) {
        sendEntry(new Entry(System.currentTimeMillis(), android.util.Log.WARN, tag, msg, tr));
        return android.util.Log.w(tag, msg, tr);
    }

    public static int w(String tag, String msg) {
        sendEntry(new Entry(System.currentTimeMillis(), android.util.Log.WARN, tag, msg, null));
        return android.util.Log.w(tag, msg, null);
    }

    public static int w(String tag, Throwable tr) {
        sendEntry(new Entry(System.currentTimeMillis(), android.util.Log.WARN, tag, null, tr));
        return android.util.Log.w(tag, null, tr);
    }

    public static int e(String tag, String msg, Throwable tr) {
        sendEntry(new Entry(System.currentTimeMillis(), android.util.Log.ERROR, tag, msg, tr));
        return android.util.Log.e(tag, msg, tr);
    }

    public static int e(String tag, String msg) {
        sendEntry(new Entry(System.currentTimeMillis(), android.util.Log.ERROR, tag, msg, null));
        return android.util.Log.e(tag, msg, null);
    }

    public static int wtf(String tag, String msg, Throwable tr) {
        sendEntry(new Entry(System.currentTimeMillis(), android.util.Log.ASSERT, tag, msg, tr));
        return android.util.Log.wtf(tag, msg, tr);
    }

    public static int wtf(String tag, String msg) {
        sendEntry(new Entry(System.currentTimeMillis(), android.util.Log.ASSERT, tag, msg, null));
        return android.util.Log.wtf(tag, msg, null);
    }

    public static int wtf(String tag, Throwable tr) {
        sendEntry(new Entry(System.currentTimeMillis(), android.util.Log.ASSERT, tag, null, tr));
        return android.util.Log.wtf(tag, null, tr);
    }

}

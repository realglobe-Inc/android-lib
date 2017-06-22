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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import jp.realglobe.lib.util.Dates;

/**
 * android.util.Log にファイル出力をつけたラッパー。
 * Created by fukuchidaisuke on 17/05/19.
 */
public final class Log {

    private Log() {
    }

    private static final String TAG = Log.class.getName();

    private static final long TIMEOUT = 3;
    private static final TimeUnit TIMEOUT_UNIT = TimeUnit.SECONDS;
    private static final int QUEUE_SIZE = 1_000;

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

        static final Entry SKIP = new Entry(0, 0, null, null, null);

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

    private static final BlockingQueue<Entry> queue = new ArrayBlockingQueue<>(QUEUE_SIZE);
    private static boolean stop;
    private static boolean flush;

    private static final ExecutorService executor = Executors.newSingleThreadExecutor();
    private static Future<?> fileWriter;


    /**
     * ログを出力するファイルを設定する。
     * 複数回呼んだ場合、最後の設定が有効になる
     *
     * @param file ログファイル。
     *             null の場合、ログはファイルに出力されない
     */
    public static synchronized void setLogFile(final File file) {
        stopLogFile();

        if (file == null) {
            return;
        } else if (file.getParentFile().mkdirs()) {
            i(TAG, "Log directory " + file.getParent() + " was generated");
        }

        fileWriter = executor.submit(() -> {
            try (final Writer writer = new BufferedWriter(new FileWriter(file, true))) {
                while (true) {
                    final Entry entry = queue.take();
                    if (stop) {
                        return;
                    } else if (flush) {
                        flush = false;
                        writer.flush();
                        v(TAG, "Flushed");
                    }
                    if (entry == Entry.SKIP) {
                        continue;
                    }
                    writer.write(entry.toString());
                    writer.write(System.lineSeparator());
                }
            } catch (IOException e) {
                e(TAG, "Writing log to " + file + " failed", e);
            } catch (InterruptedException e) {
                // 終了
            }
        });
    }

    private static synchronized void stopLogFile() {
        if (fileWriter == null) {
            return;
        } else if (fileWriter.isDone()) {
            fileWriter = null;
            return;
        }
        stop = true;
        try {
            queue.offer(Entry.SKIP);
            try {
                fileWriter.get(TIMEOUT, TIMEOUT_UNIT);
            } catch (InterruptedException e) {
                // 終了
                Thread.currentThread().interrupt();
                return;
            } catch (ExecutionException e) {
                e(TAG, "Logging failed", e);
            } catch (TimeoutException e) {
                fileWriter.cancel(true);
            }
            fileWriter = null;
        } finally {
            stop = false;
        }
    }

    /**
     * バッファに溜まってる分を書き出す。
     * ただし、呼び出しが終了した時点での書き出し完了は保証しない
     */
    public static void flushLogFile() {
        // queue で同期してるので synchronized の必要は無い
        flush = true;
        // キューが空だと実行されないのでダミーを入れる
        queue.offer(Entry.SKIP);
    }

    public static int v(String tag, String msg, Throwable tr) {
        queue.offer(new Entry(System.currentTimeMillis(), android.util.Log.VERBOSE, tag, msg, tr));
        return android.util.Log.v(tag, msg, tr);
    }

    public static int v(String tag, String msg) {
        queue.offer(new Entry(System.currentTimeMillis(), android.util.Log.VERBOSE, tag, msg, null));
        return android.util.Log.v(tag, msg, null);
    }

    public static int d(String tag, String msg, Throwable tr) {
        queue.offer(new Entry(System.currentTimeMillis(), android.util.Log.DEBUG, tag, msg, tr));
        return android.util.Log.d(tag, msg, tr);
    }

    public static int d(String tag, String msg) {
        queue.offer(new Entry(System.currentTimeMillis(), android.util.Log.DEBUG, tag, msg, null));
        return android.util.Log.d(tag, msg, null);
    }

    public static int i(String tag, String msg, Throwable tr) {
        queue.offer(new Entry(System.currentTimeMillis(), android.util.Log.INFO, tag, msg, tr));
        return android.util.Log.i(tag, msg, tr);
    }

    public static int i(String tag, String msg) {
        queue.offer(new Entry(System.currentTimeMillis(), android.util.Log.INFO, tag, msg, null));
        return android.util.Log.i(tag, msg, null);
    }

    public static int w(String tag, String msg, Throwable tr) {
        queue.offer(new Entry(System.currentTimeMillis(), android.util.Log.WARN, tag, msg, tr));
        return android.util.Log.w(tag, msg, tr);
    }

    public static int w(String tag, String msg) {
        queue.offer(new Entry(System.currentTimeMillis(), android.util.Log.WARN, tag, msg, null));
        return android.util.Log.w(tag, msg, null);
    }

    public static int w(String tag, Throwable tr) {
        queue.offer(new Entry(System.currentTimeMillis(), android.util.Log.WARN, tag, null, tr));
        return android.util.Log.w(tag, null, tr);
    }

    public static int e(String tag, String msg, Throwable tr) {
        queue.offer(new Entry(System.currentTimeMillis(), android.util.Log.ERROR, tag, msg, tr));
        return android.util.Log.e(tag, msg, tr);
    }

    public static int e(String tag, String msg) {
        queue.offer(new Entry(System.currentTimeMillis(), android.util.Log.ERROR, tag, msg, null));
        return android.util.Log.e(tag, msg, null);
    }

    public static int wtf(String tag, String msg, Throwable tr) {
        queue.offer(new Entry(System.currentTimeMillis(), android.util.Log.ASSERT, tag, msg, tr));
        return android.util.Log.wtf(tag, msg, tr);
    }

    public static int wtf(String tag, String msg) {
        queue.offer(new Entry(System.currentTimeMillis(), android.util.Log.ASSERT, tag, msg, null));
        return android.util.Log.wtf(tag, msg, null);
    }

    public static int wtf(String tag, Throwable tr) {
        queue.offer(new Entry(System.currentTimeMillis(), android.util.Log.ASSERT, tag, null, tr));
        return android.util.Log.wtf(tag, null, tr);
    }

}

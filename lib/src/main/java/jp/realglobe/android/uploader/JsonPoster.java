package jp.realglobe.android.uploader;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import jp.realglobe.android.function.Consumer;

/**
 * JSON を HTTP POST する。
 * Created by fukuchidaisuke on 17/07/05.
 */
public class JsonPoster {

    private static final String TAG = JsonPoster.class.getName();

    private static final String CONTENT_TYPE = "Content-Type";
    private static final String CONTENT_TYPE_JSON = "application/json";

    private static class JsonPostHandler extends Handler {

        private static final int MSG_POST = 0;

        private final URL url;
        private final int timeout;
        @NonNull
        private final Consumer<Exception> onError;

        JsonPostHandler(@NonNull Looper looper, @NonNull URL url, @Nullable Consumer<Exception> onError, int timeout) {
            super(looper);
            this.url = url;
            this.timeout = timeout;
            this.onError = (onError != null ? onError : (Exception e) -> Log.e(TAG, "Post failed", e));
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_POST: {
                    try {
                        post(this.url, (Map) msg.obj, this.timeout);
                    } catch (Exception e) {
                        this.onError.accept(e);
                    }
                }
            }
        }

        private static void post(URL url, Map data, int timeout) throws IOException {
            final String json = (new JSONObject(data)).toString();
            final byte[] bytes = json.getBytes();
            final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            try {
                connection.setConnectTimeout(timeout);
                connection.setDoOutput(true);
                connection.setDoInput(false);
                connection.setRequestProperty(CONTENT_TYPE, CONTENT_TYPE_JSON);
                connection.connect();

                try (final OutputStream reqBody = new BufferedOutputStream(connection.getOutputStream())) {
                    reqBody.write(bytes);
                }
                Log.v(TAG, "Posting " + json + " to " + url + " resulted in " + connection.getResponseCode());
            } finally {
                connection.disconnect();
            }
        }

        /**
         * @param data  POST させるデータ
         * @param clear true なら溜まってる分は捨てる
         */
        void post(Map data, boolean clear) {
            if (clear && hasMessages(MSG_POST)) {
                removeMessages(MSG_POST);
            }
            sendMessage(obtainMessage(MSG_POST, data));
        }

    }

    private final JsonPostHandler handler;

    /**
     * @param looper  スレッド
     * @param url     POST 先 URL
     * @param onError エラー時実行関数
     * @param timeout 接続タイムアウト（ミリ秒）
     */
    public JsonPoster(@NonNull Looper looper, @NonNull URL url, @Nullable Consumer<Exception> onError, int timeout) {
        this.handler = new JsonPostHandler(looper, url, onError, timeout);
    }

    /**
     * @param data  POST させるデータ
     * @param clear true なら溜まってる分は捨てる
     */
    public void post(Map data, boolean clear) {
        this.handler.post(data, clear);
    }

    /**
     * post(data, false) と同じ
     */
    public void post(Map data) {
        this.handler.post(data, false);
    }

}

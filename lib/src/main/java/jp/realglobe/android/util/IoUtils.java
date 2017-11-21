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

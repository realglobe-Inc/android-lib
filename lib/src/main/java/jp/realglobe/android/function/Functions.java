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

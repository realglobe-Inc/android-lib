package jp.realglobe.android.function;

/**
 * java.util.function.Function 対応が API 24 からなので。
 * Created by fukuchidaisuke on 18/04/19.
 */
public interface Function<T, R> {

    R apply(T t);

}

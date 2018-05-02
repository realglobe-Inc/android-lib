package jp.realglobe.android.function;

/**
 * java.util.function.Supplier 対応が API 24 からなので。
 * Created by fukuchidaisuke on 18/04/19.
 */
public interface Supplier<T> {

    T get();

}

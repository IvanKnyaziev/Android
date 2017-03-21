package com.example.user.mvvmregistration.utils;


import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.util.Log;
import java.util.HashMap;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;

public class ReadOnlyField<T> extends ObservableField<T> {
    final Observable<T> source;
    final HashMap<OnPropertyChangedCallback, Subscription> subscriptions = new HashMap<>();

    public static <U> ReadOnlyField<U> create(@NonNull Observable<U> source) {
        return new ReadOnlyField<>(source);
    }

    protected ReadOnlyField(@NonNull Observable<T> source) {
        super();
        this.source = source
                .doOnNext(new Action1<T>() {
                    @Override
                    public void call(T t) {
                        ReadOnlyField.super.set(t);
                    }
                })
                .doOnError(new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.e("ReadOnlyField", "onError in source observable", throwable);
                    }
                })
                .onErrorResumeNext(Observable.<T>empty())
                .share();
    }

    /**
     * @deprecated Setter of ReadOnlyField does nothing. Merge with the source Observable instead.
     * See <a href="https://github.com/manas-chaudhari/android-mvvm/tree/master/Documentation/ObservablesAndSetters.md">Documentation/ObservablesAndSetters.md</a>
     */
    @Deprecated
    @Override
    public void set(T value) {}

    @Override
    public synchronized void addOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        super.addOnPropertyChangedCallback(callback);
        subscriptions.put(callback, source.subscribe());
    }

    @Override
    public synchronized void removeOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        super.removeOnPropertyChangedCallback(callback);
        Subscription subscription = subscriptions.remove(callback);
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
    }
}
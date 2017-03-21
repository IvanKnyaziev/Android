package com.example.user.mvvmregistration.utils.PaginationUtils;

import android.databinding.Observable;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import com.example.user.mvvmregistration.utils.ReadOnlyField;

import rx.Subscriber;
import rx.functions.Action0;
import rx.subscriptions.Subscriptions;

public class FieldUtils {
    @NonNull
    public static <T> rx.Observable<T> toObservable(@NonNull final ObservableField<T> field) {
        return rx.Observable.create(new rx.Observable.OnSubscribe<T>() {
            @Override
            public void call(final Subscriber<? super T> subscriber) {
                subscriber.onNext(field.get());
                final Observable.OnPropertyChangedCallback callback = new Observable.OnPropertyChangedCallback() {
                    @Override
                    public void onPropertyChanged(android.databinding.Observable observable, int i) {
                        subscriber.onNext(field.get());
                    }
                };
                field.addOnPropertyChangedCallback(callback);
                subscriber.add(Subscriptions.create(new Action0() {
                    @Override
                    public void call() {
                        field.removeOnPropertyChangedCallback(callback);
                    }
                }));
            }
        });
    }
    @NonNull
    public static <T> ReadOnlyField<T> toField(@NonNull final rx.Observable<T> observable) {
        return ReadOnlyField.create(observable);
    }
}
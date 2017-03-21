package com.example.user.mvvmregistration.ui.UserDetailsActivity;

import android.content.Context;

import com.example.user.mvvmregistration.model.UserDetails.SingleDataObject;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class UserDetailsActivityPresenter implements UserDetailsActivityMVP.Presenter {

    private UserDetailsActivityMVP.View view;
    private UserDetailsActivityMVP.Model model;
    private Subscription subscription = null;
    private int userId;

    public UserDetailsActivityPresenter(UserDetailsActivityMVP.View view, int userId, Context context) {
        this.view = view;
        this.userId = userId;
        model = new UserDetailsActivityModel(context);
        loadData(userId);
    }

    @Override
    public void loadData(int userId) {
        subscription = model.getSingleDataObject(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SingleDataObject>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(SingleDataObject singleDataObject) {
                        view.updateView(singleDataObject);
                    }
                });
    }

    @Override
    public void setView(UserDetailsActivityMVP.View view) {
        this.view = view;

    }

    @Override
    public void rxUnsubscribe() {
        if(subscription!=null && !subscription.isUnsubscribed()){
            subscription.unsubscribe();
        }
    }
}

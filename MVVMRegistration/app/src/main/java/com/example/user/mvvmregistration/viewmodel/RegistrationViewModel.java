package com.example.user.mvvmregistration.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.view.View;
import android.widget.Toast;
import com.example.user.mvvmregistration.model.RegistrationResponseModel;
import com.example.user.mvvmregistration.model.User;
import com.example.user.mvvmregistration.repository.DataManager;
import com.example.user.mvvmregistration.ui.RegistrationActivity;
import com.example.user.mvvmregistration.ui.UsersActivity;

import rx.Observable;
import rx.SingleSubscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func3;
import rx.schedulers.Schedulers;

import static com.example.user.mvvmregistration.utils.PaginationUtils.FieldUtils.toObservable;

public class RegistrationViewModel {

    public final ObservableField<String> email = new ObservableField<>("");
    public final ObservableField<String> password = new ObservableField<>("");
    public final ObservableField<String> confirmPassword = new ObservableField<>("");

    public ObservableBoolean registrationEnabled = new ObservableBoolean(false);

    DataManager dataManager;
    Subscription subscription = null;
    User user = new User("", "");

    public RegistrationViewModel(Context context) {
        dataManager = DataManager.INSTANCE(context);

        Observable<Boolean> isRegistrationEnabled = Observable.combineLatest(
                toObservable(email),
                toObservable(password),
                toObservable(confirmPassword),
                new Func3<String, String, String, Boolean>() {
                    @Override
                    public Boolean call(String email, String password, String confirmPassword) {
                        user.setEmail(email);
                        user.setPassword(password);
                        return !password.equals("") && password.equals(confirmPassword)
                                && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
                    }
                }
        );
        isRegistrationEnabled.subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean aBoolean) {
                registrationEnabled.set(aBoolean);
            }
        });
    }

    public void onRegistrationClick(final View view){
        subscription = dataManager.
                registrateUser(user)
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleSubscriber<RegistrationResponseModel>() {
                    @Override
                    public void onSuccess(RegistrationResponseModel value) {
                        loginUser(view);
                    }

                    @Override
                    public void onError(Throwable error) {
                        error.printStackTrace();
                        //Show ErrorDialog
                    }
                });
    }

    public void onDestroy(){
        if(subscription!=null && !subscription.isUnsubscribed()){
            subscription.unsubscribe();
        }
    }

    public void loginUser(final View view){
        subscription = dataManager.loginUser(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<RegistrationResponseModel>() {
                    @Override
                    public void onSuccess(RegistrationResponseModel value) {
                        Toast.makeText(view.getContext(), "Login successful", Toast.LENGTH_SHORT).show();
                        onLoginSuccess(view.getContext());
                    }

                    @Override
                    public void onError(Throwable error) {
                        error.printStackTrace();
                        //Show ErrorDialog
                    }
                });
    }

    private void onLoginSuccess(Context context){
        Intent usersActivityintent = new Intent(context, UsersActivity.class);
        context.startActivity(usersActivityintent);
        ((RegistrationActivity)context).finish();
    }

}

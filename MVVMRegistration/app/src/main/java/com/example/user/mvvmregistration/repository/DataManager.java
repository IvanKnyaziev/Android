package com.example.user.mvvmregistration.repository;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.util.DiffUtil;
import android.util.Log;

import com.example.user.mvvmregistration.api.ApiService;
import com.example.user.mvvmregistration.api.IApi;
import com.example.user.mvvmregistration.db.DataBaseHelper;
import com.example.user.mvvmregistration.model.RegistrationResponseModel;
import com.example.user.mvvmregistration.model.UserDetails.ResponseResource;
import com.example.user.mvvmregistration.model.UserDetails.ResponseUser;
import com.example.user.mvvmregistration.model.UserDetails.SingleDataObject;
import com.example.user.mvvmregistration.model.UserDetails.SingleResources;
import com.example.user.mvvmregistration.model.UserDetails.SingleUser;
import com.example.user.mvvmregistration.model.User;
import com.example.user.mvvmregistration.model.UsersListDiffCallback;
import com.example.user.mvvmregistration.model.UsersResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import rx.Observable;
import rx.Single;
import rx.Subscriber;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.functions.Func4;
import rx.subjects.PublishSubject;

public class DataManager {

    ApiService apiService;
    DataBaseHelper mDbHelper;
    private PublishSubject<List<SingleUser>> mSubject = PublishSubject.create();

    private static volatile DataManager sInstance;

    private DataManager(Context context) {
        apiService = new ApiService();
        mDbHelper = new DataBaseHelper(context);
    }

    public static DataManager INSTANCE(Context context) {
        if  (sInstance == null) {
            synchronized (DataManager.class) {
                if (sInstance == null) {
                    sInstance = new DataManager(context);
                }
            }
        }
        return sInstance;
    }

    public Single<RegistrationResponseModel> registrateUser(User user){
        return apiService.provideApiService().registration(user);
    }

    public Single<RegistrationResponseModel> loginUser(User user){
        return apiService.provideApiService().login(user);
    }

    public Observable<DiffUtil.DiffResult> getUsers(final List<SingleUser> oldData){
        return Observable.zip(apiService.provideApiService().getUsers(1),
                apiService.provideApiService().getUsers(2),
                apiService.provideApiService().getUsers(3),
                apiService.provideApiService().getUsers(4),
                new Func4<UsersResponse, UsersResponse, UsersResponse, UsersResponse, List<SingleUser>>() {
                    @Override
                    public List<SingleUser> call(UsersResponse usersResponse, UsersResponse usersResponse2, UsersResponse usersResponse3, UsersResponse usersResponse4) {
                        List<SingleUser> users = new ArrayList<>();
                        users.addAll(usersResponse.getData());
                        users.addAll(usersResponse2.getData());
                        users.addAll(usersResponse3.getData());
                        users.addAll(usersResponse4.getData());
                        return users;
                    }
                }).flatMap(new Func1<List<SingleUser>, Observable<DiffUtil.DiffResult>>() {
            @Override
            public Observable<DiffUtil.DiffResult> call(List<SingleUser> singleUsers) {
                updateUsersInDataBase(singleUsers);
                return Observable.just(DiffUtil.calculateDiff(new UsersListDiffCallback(oldData, singleUsers)));
            }
        });
    }

    public Observable<List<SingleUser>> getUsers() {
        Observable<List<SingleUser>> res = Observable.zip(apiService.provideApiService().getUsers(1),
                apiService.provideApiService().getUsers(2),
                apiService.provideApiService().getUsers(3),
                apiService.provideApiService().getUsers(4), new Func4<UsersResponse, UsersResponse, UsersResponse, UsersResponse, List<SingleUser>>() {
                    @Override
                    public List<SingleUser> call(UsersResponse usersResponse, UsersResponse usersResponse2, UsersResponse usersResponse3, UsersResponse usersResponse4) {
                        List<SingleUser> users = new ArrayList<>();
                        users.addAll(usersResponse.getData());
                        users.addAll(usersResponse2.getData());
                        users.addAll(usersResponse3.getData());
                        users.addAll(usersResponse4.getData());
                        return users;
                    }
                });
        return res;
    }

    public Observable<SingleDataObject> getSingleDataObject(int userId){
        return Observable.zip(getSingleUser(userId), getSingleResources(userId), new Func2<SingleUser, SingleResources, SingleDataObject>() {
            @Override
            public SingleDataObject call(SingleUser singleUser, SingleResources singleResources) {
                return new SingleDataObject(
                        singleUser.getId(),
                        singleUser.getFirstName(),
                        singleUser.getLastName(),
                        singleUser.getAvatar(),
                        singleResources.getName(),
                        singleResources.getYear(),
                        singleResources.getPantoneValue());
            }
        });
    }

    private Observable<SingleUser> getSingleUser(int userId){
        Observable<ResponseUser> responseUserSingle = apiService.provideApiService().getResponseUser(userId);
        return responseUserSingle.flatMap(new Func1<ResponseUser, Observable<? extends SingleUser>>() {
            @Override
            public Observable<? extends SingleUser> call(ResponseUser responseUser) {
                return Observable.just(responseUser.getData());
            }
        });
    }

    private Observable<SingleResources> getSingleResources(int userId){
        Observable<ResponseResource> responseResource = apiService.provideApiService().getResponseResource(userId);
        return responseResource.flatMap(new Func1<ResponseResource, Observable<SingleResources>>() {
            @Override
            public Observable<SingleResources> call(ResponseResource responseResource) {
                return Observable.just(responseResource.getData());
            }
        });
    }

    public void insertUsersToDataBase(List<SingleUser> users){
        mDbHelper.open();
        for(SingleUser user : users){
            mDbHelper.addUser(user.getId(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getAvatar());
            //Log.d("SAVE TO DATABASE", user.getFirstName() + " SAVED");
        }
        mDbHelper.close();
        mSubject.onNext(users);
    }

    public void updateUsersInDataBase(List<SingleUser> users){
        mDbHelper.open();
        for(SingleUser user : users){
            mDbHelper.updateOrInsertUsers(user.getId(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getAvatar());
            //Log.d("Update DATABASE", user.getFirstName() + " UPDATED");
        }
        mDbHelper.close();
    }

    private List<SingleUser> getUsersFromDatabase() {
        List<SingleUser> results = new ArrayList<SingleUser>();
        mDbHelper.openForRead();
        Cursor c = mDbHelper.getAllUsers();
        if(!c.moveToFirst()){
            return results;
        }
        do{
            results.add(new SingleUser(c.getInt(c.getColumnIndex(DataBaseHelper.KEY_ID)),
                    c.getString(c.getColumnIndex(DataBaseHelper.KEY_FIRST_NAME)),
                    c.getString(c.getColumnIndex(DataBaseHelper.KEY_LAST_NAME)),
                    c.getString(c.getColumnIndex(DataBaseHelper.KEY_AVATAR))));
        }while(c.moveToNext());
        c.close();
        mDbHelper.close();
        return results;
    }

    public Observable<List<SingleUser>> getDataObservable() {
        return makeObservable(getData());
    }

    private Callable<List<SingleUser>> getData() {
        return new Callable() {
            public List<SingleUser> call() {
                return getUsersFromDatabase();
            }};
        }

    private <T> Observable<T> makeObservable(final Callable<T> func) {
        return Observable.create(
                   new Observable.OnSubscribe<T>() {
                       @Override
                       public void call(Subscriber<? super T> subscriber) {
                           try {
                               T observed = func.call();
                               if (observed != null) { // to make defaultIfEmpty work
                                   subscriber.onNext(observed);
                               }
                               subscriber.onCompleted();
                           } catch (Exception ex) {
                               subscriber.onError(ex);
                           }
                       }
                   });
    }
}

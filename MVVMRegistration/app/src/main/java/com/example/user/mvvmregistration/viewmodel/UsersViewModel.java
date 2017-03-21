package com.example.user.mvvmregistration.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.databinding.BindingAdapter;
import android.databinding.ObservableField;
import android.os.Bundle;
import android.support.v7.util.DiffUtil;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.user.mvvmregistration.adapters.UsersAdapter;
import com.example.user.mvvmregistration.databinding.ActivityUsersBinding;
import com.example.user.mvvmregistration.model.UserDetails.SingleUser;
import com.example.user.mvvmregistration.repository.DataManager;
import com.example.user.mvvmregistration.ui.UserDetailsActivity.UserDetailsActivity;
import com.example.user.mvvmregistration.ui.UsersActivity;
import com.example.user.mvvmregistration.utils.PaginationUtils.PaginationTool;
import com.example.user.mvvmregistration.utils.PaginationUtils.PagingListener;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class UsersViewModel {

    public static final String USER_ID = "userID";
    public static final String USER_INFO_BUNDLE = "userInfoBundle";

    public final ObservableField<String> firstname = new ObservableField<>();
    public final ObservableField<String> lastname = new ObservableField<>();

    private DataManager dataManager;
    private SingleUser datum;
    private Context context;
    public List<SingleUser> users;
    private UsersAdapter adapter;
    private ActivityUsersBinding binding;
    private Subscription subscription;
    private int counter = 0;

    public UsersViewModel(Context context){
        dataManager = DataManager.INSTANCE(context);
        users = new ArrayList<>();
    }

    public UsersViewModel(SingleUser datum, Context context) {
        this.datum = datum;
        this.context = context;
    }

    public void setAdapter(Context context, ActivityUsersBinding binding){
        this.adapter = new UsersAdapter(users, context);
        this.binding = binding;
        binding.usersList.setAdapter(adapter);
        getUsersFromDB();
    }

    private void getUsersFromDB(){
        subscription = dataManager.getDataObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<SingleUser>>() {
                    @Override
                    public void onCompleted() {
                    }
                    @Override
                    public void onError(Throwable e) {
                    }
                    @Override
                    public void onNext(List<SingleUser> singleUsers) {
                        users.addAll(singleUsers);
                        adapter.notifyItemInserted(adapter.getItemCount() - singleUsers.size());
                        Log.d("USERSACTIVITY", "GET DATA CALL");
                        getData(singleUsers);
                    }
                });
    }

    private void getData(List<SingleUser> singleUsers){
        subscription = dataManager.getUsers(singleUsers)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<DiffUtil.DiffResult>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(DiffUtil.DiffResult diffResult) {
                        diffResult.dispatchUpdatesTo(adapter);
                    }
                });
    }

//    private void getData(){
//        /*PaginationTool.Builder<List<SingleUser>> paginationTool = PaginationTool.
//                buildPagingObservable(
//                        binding.usersList,
//                        new PagingListener<List<SingleUser>>(){
//                            @Override
//                            public Observable<List<SingleUser>> onNextPage(int offset) {
//                                counter++;
//                                return dataManager.getUsers(counter);
//                            }
//                        });*/
//        subscription = /*paginationTool.build()
//                .getPagingObservable()*/
//        dataManager.getUsers()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<List<SingleUser>>() {
//                    @Override
//                    public void onCompleted() {
//                    }
//                    @Override
//                    public void onError(Throwable e) {
//                        e.printStackTrace();
//                    }
//                    @Override
//                    public void onNext(List<SingleUser> usersList){
//                        users = usersList;
//                        adapter.notifyDataSetChanged();
//                    }
//                });
//    }

    private void updateAdapter(List<SingleUser> usersList){

            for(int i = 0; i<usersList.size();i++){
                users.set(i, usersList.get(i));
                adapter.notifyItemChanged(i);
            }
    }

    @BindingAdapter({"image"})
    public static void loadImage(ImageView imageView, String url){
        Glide.with(imageView.getContext()).load(url).centerCrop().into(imageView);
    }

    public String getFirstname() {
        return datum.getFirstName();
    }

    public String getLastname() {
        return datum.getLastName();
    }

    public String getAvatar(){
        return datum.getAvatar();
    }

    public void onItemClick(View v){
        Intent detailsIntent = new Intent(v.getContext(), UserDetailsActivity.class);
        detailsIntent.putExtra(USER_ID, datum.getId());
        ((UsersActivity)v.getContext()).startActivity(detailsIntent);
    }

    public void onDestroy(){
        if(subscription!=null && !subscription.isUnsubscribed()){
            subscription.unsubscribe();
        }
    }

}

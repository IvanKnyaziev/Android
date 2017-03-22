package com.example.user.mvvmregistration.viewmodel;

import android.content.Context;
import android.content.Intent;
import android.databinding.BindingAdapter;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.util.DiffUtil;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.user.mvvmregistration.adapters.UsersAdapter;
import com.example.user.mvvmregistration.databinding.ActivityUsersBinding;
import com.example.user.mvvmregistration.model.UserDetails.SingleUser;
import com.example.user.mvvmregistration.model.UsersListDiffCallback;
import com.example.user.mvvmregistration.repository.DataManager;
import com.example.user.mvvmregistration.ui.UserDetailsActivity.UserDetailsActivity;
import com.example.user.mvvmregistration.ui.UsersActivity;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class UsersViewModel implements Parcelable{

    public static final String USER_ID = "userID";
    public static final String USER_INFO_BUNDLE = "userInfoBundle";

    public final ObservableField<String> firstname = new ObservableField<>();
    public final ObservableField<String> lastname = new ObservableField<>();
    public final ObservableBoolean isRefreshing = new ObservableBoolean(false);

    private DataManager dataManager;
    private SingleUser datum;
    private Context context;
    public List<SingleUser> users;
    private UsersAdapter adapter;
    private ActivityUsersBinding binding;
    private Subscription subscription;
    private int counter = 0;

    public UsersViewModel getInstance(){
        return this;
    }

    public UsersViewModel(Context context){
        dataManager = DataManager.getInstance(context);
        users = new ArrayList<>();
    }

    public UsersViewModel(SingleUser datum, Context context) {
        this.datum = datum;
        this.context = context;
    }

    protected UsersViewModel(Parcel in) {
        counter = in.readInt();
        users = in.createTypedArrayList(SingleUser.CREATOR);
    }

    public static final Creator<UsersViewModel> CREATOR = new Creator<UsersViewModel>() {
        @Override
        public UsersViewModel createFromParcel(Parcel in) {
            return new UsersViewModel(in);
        }

        @Override
        public UsersViewModel[] newArray(int size) {
            return new UsersViewModel[size];
        }
    };

    public void setAdapter(Context context, ActivityUsersBinding binding){
        this.adapter = new UsersAdapter(users, context);
        this.binding = binding;
        binding.usersList.setAdapter(adapter);
        if(users.isEmpty()){
            getUsersFromDB();
        }
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
                        e.printStackTrace();
                    }
                    @Override
                    public void onNext(List<SingleUser> singleUsers) {
                        //Hardcoded to check that animations working fine without redraw
                        //You can uncomment to check animations use 'l' instead of 'singleUsers'
                        //ArrayList<SingleUser> l = new ArrayList<>();
                        //l.add(new SingleUser(4, "eve", "holt", "https://s3.amazonaws.com/uifaces/faces/twitter/marcoramires/128.jpg"));
                        //l.add(new SingleUser(7, "michael", "bluth", "https://s3.amazonaws.com/uifaces/faces/twitter/follettkyle/128.jpg"));
                        users.addAll(singleUsers);
                        adapter.notifyItemInserted(adapter.getItemCount() - singleUsers.size());
                        getData();
                    }
                });
    }

    private void getData(){
        subscription = dataManager.getUsers()
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
                    public void onNext(List<SingleUser> usersList) {
                        UsersListDiffCallback callback = new UsersListDiffCallback(users, usersList);
                        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(callback);
                        users.clear();
                        users.addAll(usersList);
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
//                        users.addAll(usersList);
//                        adapter.notifyItemInserted(usersList.size());
//                    }
//                });
//    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(counter);
        dest.writeTypedList(users);
    }
}

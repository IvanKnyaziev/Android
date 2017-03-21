package com.example.user.mvvmregistration.ui.UserDetailsActivity;

import android.content.Context;

import com.example.user.mvvmregistration.model.UserDetails.SingleDataObject;
import com.example.user.mvvmregistration.repository.DataManager;
import rx.Observable;

public class UserDetailsActivityModel implements UserDetailsActivityMVP.Model {

    DataManager dataManager;

    public UserDetailsActivityModel(Context context){
        this.dataManager = DataManager.INSTANCE(context);
    }

    @Override
    public Observable<SingleDataObject> getSingleDataObject(int userId) {
        return dataManager.getSingleDataObject(userId);
    }
}

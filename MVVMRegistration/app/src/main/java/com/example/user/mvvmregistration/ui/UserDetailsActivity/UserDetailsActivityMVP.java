package com.example.user.mvvmregistration.ui.UserDetailsActivity;

import com.example.user.mvvmregistration.model.UserDetails.SingleDataObject;

import rx.Observable;

public interface UserDetailsActivityMVP {
    interface View{
        void updateView(SingleDataObject singleDataObject);
    }

    interface Presenter{
        void loadData(int userId);
        void setView(UserDetailsActivityMVP.View view);
        void rxUnsubscribe();
    }

    interface Model{
        Observable<SingleDataObject> getSingleDataObject(int userId);
    }

}

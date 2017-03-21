package com.example.user.mvvmregistration.model;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import com.example.user.mvvmregistration.model.UserDetails.SingleUser;

import java.util.List;

public class UsersListDiffCallback extends DiffUtil.Callback {

    private List<SingleUser> mOldList;

    private List<SingleUser> mNewList;

    public UsersListDiffCallback(List<SingleUser> mOldList, List<SingleUser> mNewList) {
        this.mOldList = mOldList;
        this.mNewList = mNewList;
    }

    @Override
    public int getOldListSize() {
        return mOldList != null ? mOldList.size() : 0;
    }

    @Override
    public int getNewListSize() {
        return mNewList != null ? mNewList.size() : 0;
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return mNewList.get(newItemPosition).getId() == mOldList.get(oldItemPosition).getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return mNewList.get(newItemPosition).equals(mOldList.get(oldItemPosition));
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        SingleUser newUser = mNewList.get(newItemPosition);
        SingleUser oldUser = mOldList.get(oldItemPosition);
        Bundle diffBundle = new Bundle();
        if (!newUser.getFirstName().equals(oldUser.getFirstName())) {
            diffBundle.putString("firstName", newUser.getFirstName());
        }
        if (!newUser.getLastName().equals(oldUser.getLastName())) {
            diffBundle.putString("lastName", newUser.getLastName());
        }
        if (!newUser.getAvatar().equals(oldUser.getAvatar())) {
            diffBundle.putString("avatar", newUser.getAvatar());
        }
        if (diffBundle.size() == 0) return null;
        return diffBundle;

    }

}

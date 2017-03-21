package com.example.user.mvvmregistration.model.UserDetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseUser {

    @SerializedName("data")
    @Expose
    private SingleUser data;

    public SingleUser getData() {
        return data;
    }

    public void setData(SingleUser data) {
        this.data = data;
    }

}

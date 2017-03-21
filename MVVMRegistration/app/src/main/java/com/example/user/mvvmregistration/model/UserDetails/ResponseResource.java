package com.example.user.mvvmregistration.model.UserDetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ResponseResource {

    @SerializedName("data")
    @Expose
    private SingleResources data;

    public SingleResources getData() {
        return data;
    }

    public void setData(SingleResources data) {
        this.data = data;
    }

}
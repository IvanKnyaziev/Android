
package com.example.user.mvvmregistration.model.UserDetails;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SingleUser implements Parcelable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("first_name")
    @Expose
    private String firstName;
    @SerializedName("last_name")
    @Expose
    private String lastName;
    @SerializedName("avatar")
    @Expose
    private String avatar;

    public SingleUser(Integer id, String firstName, String lastName, String avatar) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.avatar = avatar;
    }

    protected SingleUser(Parcel in) {
        firstName = in.readString();
        lastName = in.readString();
        avatar = in.readString();
    }

    public static final Creator<SingleUser> CREATOR = new Creator<SingleUser>() {
        @Override
        public SingleUser createFromParcel(Parcel in) {
            return new SingleUser(in);
        }

        @Override
        public SingleUser[] newArray(int size) {
            return new SingleUser[size];
        }
    };

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(avatar);
    }
}


package com.example.user.mvvmregistration.model;

import java.util.List;

import com.example.user.mvvmregistration.model.UserDetails.SingleUser;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UsersResponse {

    @SerializedName("page")
    @Expose
    private String page;
    @SerializedName("per_page")
    @Expose
    private Integer perPage;
    @SerializedName("total")
    @Expose
    private Integer total;
    @SerializedName("total_pages")
    @Expose
    private Integer totalPages;
    @SerializedName("data")
    @Expose
    private List<SingleUser> data = null;

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public Integer getPerPage() {
        return perPage;
    }

    public void setPerPage(Integer perPage) {
        this.perPage = perPage;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public List<SingleUser> getData() {
        return data;
    }

    public void setData(List<SingleUser> data) {
        this.data = data;
    }

}

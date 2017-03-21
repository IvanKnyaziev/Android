package com.example.user.mvvmregistration.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.user.mvvmregistration.R;
import com.example.user.mvvmregistration.databinding.UserUtemBinding;
import com.example.user.mvvmregistration.model.UserDetails.SingleUser;
import com.example.user.mvvmregistration.viewmodel.UsersViewModel;

import java.util.List;


public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.BindingHolder> {

    private List<SingleUser> users;
    private Context context;

    public UsersAdapter(List<SingleUser> users, Context context) {
        this.users = users;
        this.context = context;
    }

    @Override
    public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        UserUtemBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.user_utem,
                parent, false);

        return new BindingHolder(binding);
    }

    @Override
    public void onBindViewHolder(BindingHolder holder, int position) {
        UserUtemBinding binding = holder.binding;
        binding.setUsv(new UsersViewModel(users.get(position), context));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public class BindingHolder extends RecyclerView.ViewHolder {
        private UserUtemBinding binding;

        public BindingHolder(UserUtemBinding binding) {
            super(binding.userCard);
            this.binding = binding;
        }
    }
}

package com.example.user.mvvmregistration.ui;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.example.user.mvvmregistration.R;
import com.example.user.mvvmregistration.databinding.ActivityUsersBinding;
import com.example.user.mvvmregistration.viewmodel.UsersViewModel;

public class UsersActivity extends AppCompatActivity {

    //
    ActivityUsersBinding binding;
    UsersViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        setBinding();
        viewModel = new UsersViewModel(this);
        viewModel.setAdapter(this, binding);
    }

    public void setBinding(){
        binding = DataBindingUtil.setContentView(this, R.layout.activity_users);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        binding.usersList.setLayoutManager(layoutManager);
    }

    @Override
    protected void onStop() {
        super.onStop();
        viewModel.onDestroy();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

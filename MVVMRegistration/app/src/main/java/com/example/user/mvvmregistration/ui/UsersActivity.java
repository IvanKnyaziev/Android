package com.example.user.mvvmregistration.ui;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import com.example.user.mvvmregistration.R;
import com.example.user.mvvmregistration.app.MyApplication;
import com.example.user.mvvmregistration.databinding.ActivityUsersBinding;
import com.example.user.mvvmregistration.viewmodel.UsersViewModel;
import com.squareup.leakcanary.RefWatcher;

public class UsersActivity extends AppCompatActivity {

    public static String USERS_VIEW_MODEL_KEY = "uvm";
    //
    private ActivityUsersBinding binding;
    private UsersViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState!=null){
            viewModel = savedInstanceState.getParcelable(USERS_VIEW_MODEL_KEY);
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        setBinding();
        if(viewModel==null) {
            viewModel = new UsersViewModel(this);
        }
        viewModel.setAdapter(this, binding);
    }

    public void setBinding(){
        binding = DataBindingUtil.setContentView(this, R.layout.activity_users);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        binding.usersList.setLayoutManager(layoutManager);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(USERS_VIEW_MODEL_KEY, viewModel.getInstance());
        super.onSaveInstanceState(outState);
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

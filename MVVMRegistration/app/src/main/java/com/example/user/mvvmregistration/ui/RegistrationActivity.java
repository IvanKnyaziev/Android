package com.example.user.mvvmregistration.ui;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.user.mvvmregistration.R;
import com.example.user.mvvmregistration.databinding.RegistrationActivityBinding;
import com.example.user.mvvmregistration.model.User;
import com.example.user.mvvmregistration.viewmodel.RegistrationViewModel;

public class RegistrationActivity extends AppCompatActivity {

    private RegistrationViewModel registrationViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        registrationViewModel = new RegistrationViewModel(this);
        RegistrationActivityBinding binding = DataBindingUtil.setContentView(this, R.layout.registration_activity);
        binding.setVm(registrationViewModel);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onStop(){
        super.onStop();
        registrationViewModel.onDestroy();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }
}

package com.example.user.mvvmregistration.ui;

import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.example.user.mvvmregistration.R;
import com.example.user.mvvmregistration.databinding.RegistrationActivityBinding;
import com.example.user.mvvmregistration.viewmodel.RegistrationViewModel;

public class RegistrationActivity extends  AppCompatActivity {

    private RegistrationViewModel registrationViewModel;
    public static String REGISTRATION_VIEW_MODEL_KEY = "rvm";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if(savedInstanceState!=null) {
            registrationViewModel = savedInstanceState.getParcelable(REGISTRATION_VIEW_MODEL_KEY);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(registrationViewModel==null) {
            registrationViewModel = new RegistrationViewModel(this);
        }
        RegistrationActivityBinding binding = DataBindingUtil.setContentView(this, R.layout.registration_activity);
        binding.setVm(registrationViewModel);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        outState.putParcelable(REGISTRATION_VIEW_MODEL_KEY, registrationViewModel.getInstance());
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

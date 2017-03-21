package com.example.user.mvvmregistration.ui.UserDetailsActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.user.mvvmregistration.R;
import com.example.user.mvvmregistration.model.UserDetails.SingleDataObject;
import com.example.user.mvvmregistration.viewmodel.UsersViewModel;

public class UserDetailsActivity extends AppCompatActivity implements UserDetailsActivityMVP.View{

    UserDetailsActivityMVP.Presenter presenter;
    ImageView imageView;
    TextView tv1, tv2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_activity);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        imageView = (ImageView)findViewById(R.id.imgAvatar);
        tv1 = (TextView)findViewById(R.id.textView);
        tv2 = (TextView)findViewById(R.id.textView2);
        presenter = new UserDetailsActivityPresenter(this, getId(getIntent()), this);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
        }
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.rxUnsubscribe();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void updateView(SingleDataObject singleDataObject) {
        Glide.with(imageView.getContext()).load(singleDataObject.getAvatar()).centerCrop().into(imageView);
        tv1.setText(singleDataObject.getFirstName());
        tv2.setText(singleDataObject.getYear());
    }

    private int getId(Intent extras){
        return extras.getIntExtra(UsersViewModel.USER_ID, 1);
    }

}

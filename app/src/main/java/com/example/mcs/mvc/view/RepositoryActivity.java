package com.example.mcs.mvc.view;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mcs.mvc.ArchiApplication;
import com.example.mcs.mvc.R;
import com.example.mcs.mvc.databinding.ActivityRepositoryBinding;
import com.example.mcs.mvc.models.GithubService;
import com.example.mcs.mvc.models.Repository;
import com.example.mcs.mvc.models.User;
import com.example.mcs.mvc.viewmodel.RepositoryViewModel;
import com.squareup.picasso.Picasso;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class RepositoryActivity extends AppCompatActivity  {

    private static final String EXTRA_REPOSITORY = "EXTRA_REPOSITORY";
    ActivityRepositoryBinding activityRepositoryBinding;
    RepositoryViewModel repositoryViewModel;


    public static Intent newIntent(Context context, Repository repository) {
        Intent intent = new Intent(context, RepositoryActivity.class);
        intent.putExtra(EXTRA_REPOSITORY, repository);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityRepositoryBinding = DataBindingUtil.setContentView(this, R.layout.activity_repository);

        setSupportActionBar(activityRepositoryBinding.toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        Repository repository = getIntent().getParcelableExtra(EXTRA_REPOSITORY);
        repositoryViewModel = new RepositoryViewModel(this, repository);

        activityRepositoryBinding.setViewModelArmando(repositoryViewModel);


        setTitle(repository.name);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        repositoryViewModel.destroy();
    }


}

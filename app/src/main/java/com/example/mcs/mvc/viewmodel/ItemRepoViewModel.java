package com.example.mcs.mvc.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.view.View;

import com.example.mcs.mvc.R;
import com.example.mcs.mvc.view.RepositoryActivity;
import com.example.mcs.mvc.models.Repository;

public class ItemRepoViewModel extends BaseObservable implements ViewModel {


    private Repository repository;
    private Context context;

    public ItemRepoViewModel(Context context, Repository repository)
    {
        this.repository = repository;
        this.context = context;
    }

    public String getName()
    {

        return  repository.name;
    }

    public String getDescription()
    {
        return repository.description;
    }

    public String getStarts()
    {
        return context.getString(R.string.text_stars, repository.stars);
    }

    public String getWatchers()
    {
        return context.getString(R.string.text_watchers, repository.watchers);
    }

    public String getForks()
    {
        return context.getString(R.string.text_forks, repository.forks);
    }

    public void onItemClick(View view)
    {
        context.startActivity(RepositoryActivity.newIntent(context,repository));
    }

    public void setRepository(Repository repository)
    {
        this.repository = repository;
        notifyChange();
    }

    @Override
    public void destroy() {

    }
}

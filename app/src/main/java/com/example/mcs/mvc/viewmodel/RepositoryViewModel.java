package com.example.mcs.mvc.viewmodel;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.view.View;
import android.widget.ImageView;

import com.example.mcs.mvc.ArchiApplication;
import com.example.mcs.mvc.R;
import com.example.mcs.mvc.models.GithubService;
import com.example.mcs.mvc.models.Repository;
import com.example.mcs.mvc.models.User;
import com.squareup.picasso.Picasso;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class RepositoryViewModel implements ViewModel {

    private Subscription subscription;
    private Repository repository;
    private Context context;

    public ObservableField<String> ownerName;
    public ObservableField<String> ownerEmail;
    public ObservableField<String> ownerLocation;
    public ObservableInt ownerEmailVisibility;
    public ObservableInt ownerLocationVisibility;
    public ObservableInt ownerLayoutVisibility;


    public RepositoryViewModel(Context context, final Repository repository)
    {
        this.context = context;
        this.repository = repository;
        this.ownerName = new ObservableField<>();
        this.ownerEmail = new ObservableField<>();
        this.ownerLocation = new ObservableField<>();
        this.ownerEmailVisibility = new ObservableInt(View.INVISIBLE);
        this.ownerLocationVisibility = new ObservableInt(View.VISIBLE);
        this.ownerLayoutVisibility = new ObservableInt(View.VISIBLE);

        loadFullUser(repository.owner.url);
    }

    public String getDescription()
    {
        return repository.description;
    }

    public String getHomepage()
    {
        return repository.homepage;

    }

    public int getHomepageVisibility()
    {
        return repository.hasHomepage()? View.VISIBLE:View.GONE;
    }

    public String getLanguage()
    {
        return context.getString(R.string.text_language, repository.language);
    }

    public int getLanguageVisibility()
    {
        return repository.hasLanguage()? View.VISIBLE: View.GONE;
    }

    public int getForkVisibility()
    {
        return repository.isFork()? View.VISIBLE:View.GONE;
    }

    public String getOwnerAvatarUrl()
    {
        return repository.owner.avatarUrl;
    }

    @BindingAdapter({"imageUrl"})
    public static void loadImage(ImageView view , String imageUrl)
    {
        Picasso.with(view.getContext())
                .load(imageUrl)
                .placeholder(R.drawable.placeholder)
                .into(view);
    }

    private void loadFullUser(String url)
    {
        ArchiApplication application = ArchiApplication.get(context);
        GithubService githubService = application.getGithubService();
        subscription = githubService.userFromUrl(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(application.defaultSubscribeScheduler())
                .subscribe(new Action1<User>() {
                    @Override
                    public void call(User user) {

                        ownerName.set(user.name);
                        ownerEmail.set(user.email);
                        ownerLocation.set(user.location);

                        ownerEmailVisibility.set(user.hasLocation()? View.VISIBLE: View.INVISIBLE);
                        ownerLocationVisibility.set(user.hasLocation()? View.VISIBLE:View.INVISIBLE);
                        ownerLayoutVisibility.set(View.VISIBLE);

                    }

                });
    }


    @Override
    public void destroy() {

        this.context = null;
        if (subscription != null && !subscription.isUnsubscribed())
            subscription.unsubscribe();

    }
}

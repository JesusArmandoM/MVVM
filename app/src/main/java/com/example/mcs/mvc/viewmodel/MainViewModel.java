package com.example.mcs.mvc.viewmodel;

import android.content.Context;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.example.mcs.mvc.ArchiApplication;
import com.example.mcs.mvc.R;
import com.example.mcs.mvc.models.GithubService;
import com.example.mcs.mvc.models.Repository;

import java.util.List;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

public class MainViewModel implements ViewModel {

    private static final String TAG = "MainViewModel";


    public ObservableInt infoMessageVisibility;
    public ObservableInt progressVisibility;
    public ObservableInt recyclerViewVisibility;
    public ObservableInt searchButtonVisibility;
    public ObservableField<String> infoMessage;

    private Context context;
    private Subscription subscription;
    private List<Repository> repositories;
    private DataListener dataListener;
    private String editTextUsernameValue;


    public MainViewModel(Context context, DataListener dataListener)
    {
        this.context = context;
        this.dataListener = dataListener;
        this.infoMessageVisibility = new ObservableInt(View.VISIBLE);
        this.progressVisibility = new ObservableInt(View.INVISIBLE);
        this.recyclerViewVisibility = new ObservableInt(View.INVISIBLE);
        this.searchButtonVisibility = new ObservableInt(View.GONE);
        this.infoMessage = new ObservableField<>("Enter a Github username above to see its repositories");

    }

    public void setDataListener(DataListener dataListener) {
        this.dataListener = dataListener;
    }


    @Override
    public void destroy() {

       if (subscription != null && !subscription.isUnsubscribed()) subscription.unsubscribe();

       subscription = null;
       context = null;
       dataListener = null;

    }

    public boolean onSearchAction(TextView view, int actionId, KeyEvent event)
    {

        if (actionId == EditorInfo.IME_ACTION_SEARCH)
        {
            String username = view.getText().toString();
            if(username.length() > 0) loadGithubRepos (username);
        }

        return false;


    }

    public void onClickSearch(View view)
    {
        loadGithubRepos(editTextUsernameValue);
    }

    public TextWatcher getUsernameEditTextWatcher()
    {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {

                editTextUsernameValue = charSequence.toString();
                searchButtonVisibility.set(charSequence.length() > 0? View.VISIBLE: View.GONE);

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
    }

    private void loadGithubRepos(String username) {
        this.progressVisibility.set(View.VISIBLE);
        this.recyclerViewVisibility.set(View.INVISIBLE);
        this.infoMessageVisibility.set(View.INVISIBLE);

        if(subscription != null && !subscription.isUnsubscribed()) subscription.unsubscribe();
        ArchiApplication application = ArchiApplication.get(context);
        GithubService githubService = application.getGithubService();
        subscription = githubService.publicRepositories(username)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(application.defaultSubscribeScheduler())
                .subscribe(new Subscriber<List<Repository>>() {
                    @Override
                    public void onCompleted() {

                        if (dataListener != null ) dataListener.onRepositoriesChanged(repositories);
                        progressVisibility.set(View.INVISIBLE);
                        if (!repositories.isEmpty())
                        {
                            recyclerViewVisibility.set(View.VISIBLE);
                        }else
                        {
                            infoMessage.set(context.getString(R.string.text_empty_repos));
                            infoMessageVisibility.set(View.VISIBLE);
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        progressVisibility.set(View.INVISIBLE);

                        if (isHttp404(e))
                        {
                            infoMessage.set(context.getString(R.string.error_username_not_found));
                        }else
                        {
                            infoMessage.set(context.getString(R.string.error_loading_repos));
                        }

                        infoMessageVisibility.set(View.VISIBLE);

                    }

                    @Override
                    public void onNext(List<Repository> repositories) {

                        MainViewModel.this.repositories = repositories;
                    }
                });
    }

    private static boolean isHttp404(Throwable error)
    {
        return error instanceof HttpException && ((HttpException) error).code() == 404;
    }


    public interface DataListener
   {
       void onRepositoriesChanged(List<Repository> repositories);
   }

}

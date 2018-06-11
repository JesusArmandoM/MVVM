package com.example.mcs.mvc;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mcs.mvc.databinding.ItemRepoBinding;
import com.example.mcs.mvc.models.Repository;
import com.example.mcs.mvc.viewmodel.ItemRepoViewModel;

import java.util.Collections;
import java.util.List;

public class RepositoryAdapter extends RecyclerView.Adapter<RepositoryAdapter.RepositoryViewHolder> {

    private List<Repository> repositories;


    public RepositoryAdapter() {
        this.repositories = Collections.emptyList();
    }

    public RepositoryAdapter(List<Repository> repositories) {
        this.repositories = repositories;
    }

    public void setRepositories(List<Repository> repositories) {
        this.repositories = repositories;
    }


    @NonNull
    @Override
    public RepositoryAdapter.RepositoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRepoBinding itemRepoBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.item_repo,
                parent,
                false);

        return new RepositoryViewHolder(itemRepoBinding);

    }

    @Override
    public void onBindViewHolder(@NonNull RepositoryAdapter.RepositoryViewHolder holder, int position) {

       holder.bindRepository(repositories.get(position));

    }

    @Override
    public int getItemCount() {
        return repositories.size();
    }



    public static class RepositoryViewHolder extends RecyclerView.ViewHolder {

        final ItemRepoBinding itemRepoBinding;

        public RepositoryViewHolder(ItemRepoBinding binding) {
            super(binding.cardView);

            itemRepoBinding = binding;

        }

        void bindRepository (Repository repository)
        {
            if (itemRepoBinding.getViewModelArmando() == null)
            {
                itemRepoBinding.setViewModelArmando(new ItemRepoViewModel(itemView.getContext(),repository));
            }else
            {
                itemRepoBinding.getViewModelArmando().setRepository(repository);
            }

        }

    }
    public interface Callback {
        void onItemClick(Repository repository);
    }
}

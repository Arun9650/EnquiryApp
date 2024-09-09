package com.example.enquiryapp.presentation.base;


import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.subjects.PublishSubject;

public abstract class BaseAdapter<T extends ViewBinding, E> extends RecyclerView.Adapter<BaseAdapter<T, E>.BaseViewHolder> {
    private final PublishSubject<E> itemClickSubject = PublishSubject.create();

    private final DiffUtil.ItemCallback<E> diffUtilCallback = new DiffUtil.ItemCallback<E>() {
        @Override
        public boolean areItemsTheSame(@NonNull E oldItem, @NonNull E newItem) {
            return areItemsSame(oldItem, newItem);
        }

        @Override
        public boolean areContentsTheSame(@NonNull E oldItem, @NonNull E newItem) {
            return areContentsSame(oldItem, newItem);
        }
    };

    private AsyncListDiffer<E> differ;

    public void initDiffer(BaseAdapter<T, E> adapter) {
        differ = new AsyncListDiffer<>(adapter, diffUtilCallback);
    }

    public abstract T getLayoutBinding(LayoutInflater inflater, ViewGroup container);

    public abstract void bindView(T viewBinding, int pos, E item, PublishSubject<E> itemClickSubject);

    public boolean areItemsSame(E oldItem, E newItem) {
        return oldItem.toString().equals(newItem.toString());
    }

    public boolean areContentsSame(E oldItem, E newItem) {
        return oldItem.equals(newItem);
    }

    public List<E> getCurrentList() {
        return differ.getCurrentList();
    }

    public void updateList(List<E> updated) {
        if (updated.isEmpty()) {
            differ.submitList(new ArrayList<>());
            return;
        }
        differ.submitList(updated);
    }

    public void addItem(E item) {
        ArrayList<E> list = new ArrayList<>(differ.getCurrentList());
        list.add(item);
        updateList(list);
    }

    public void updateItem(int pos, E updatedValue) {
        ArrayList<E> list = new ArrayList<>(differ.getCurrentList());
        list.set(pos, updatedValue);
        updateList(list);
    }

    public void removeItem(int pos) {
        ArrayList<E> list = new ArrayList<>(differ.getCurrentList());
        list.remove(pos);
        updateList(list);
    }

    public Observable<E> getItemClickObservable() {
        return itemClickSubject;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        T viewBinding = getLayoutBinding(LayoutInflater.from(parent.getContext()), parent);
        return new BaseViewHolder(viewBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        int pos = holder.getBindingAdapterPosition();
        E item = differ.getCurrentList().get(pos);
        bindView(holder.viewBinding, pos, item, itemClickSubject);
    }

    @Override
    public int getItemCount() {
        return differ.getCurrentList().size();
    }

    public class BaseViewHolder extends RecyclerView.ViewHolder {
        T viewBinding;

        public BaseViewHolder(T viewBinding) {
            super(viewBinding.getRoot());
            this.viewBinding = viewBinding;
        }
    }
}


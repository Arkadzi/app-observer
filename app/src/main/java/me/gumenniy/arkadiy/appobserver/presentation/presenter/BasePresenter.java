package me.gumenniy.arkadiy.appobserver.presentation.presenter;

import android.support.annotation.Nullable;

import me.gumenniy.arkadiy.appobserver.presentation.view.BaseView;

/**
 * Created by Arkadiy on 15.04.2016.
 */
public abstract class BasePresenter<T extends BaseView> {

    @Nullable
    private T view;

    public void bindView(T view) {
        this.view = view;
    }

    @Nullable
    public T getView() {
        return view;
    }
}

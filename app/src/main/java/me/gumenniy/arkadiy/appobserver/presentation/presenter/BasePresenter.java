package me.gumenniy.arkadiy.appobserver.presentation.presenter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import me.gumenniy.arkadiy.appobserver.presentation.view.BaseView;

/**
 * Created by Arkadiy on 15.04.2016.
 */
public abstract class BasePresenter<V> {

    @Nullable
    private V view;

    public void bindView(@NonNull V view) {
        this.view = view;
    }

    public void unbindView(boolean isViewDestroyed) {
        this.view = null;
    }

    @Nullable
    public V getView() {
        return view;
    }
}

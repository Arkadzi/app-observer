package me.gumenniy.arkadiy.appobserver.presentation.presenter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Basic class for MVP presenter with lifecycle methods
 */
public abstract class BasePresenter<V> {

    /**
     * MVP view
     */
    @Nullable
    private V view;

    /**
     * binds view
     *
     * @param view bind view
     */
    public void bindView(@NonNull V view) {
        this.view = view;
    }

    /**
     * unbinds view
     *
     * @param isViewDestroyed returns true if view is destroyed for ever, false otherwise
     */
    public void unbindView(boolean isViewDestroyed) {
        this.view = null;
    }

    @Nullable
    public V getView() {
        return view;
    }
}

package me.gumenniy.arkadiy.appobserver.presentation.model;

import android.support.annotation.Nullable;

import java.util.List;

import me.gumenniy.arkadiy.appobserver.dao.model.App;

/**
 * Model with abilities of caching and asynchronous loading
 */
public interface LoadModel<T> {

    /**
     * asynchronously load data and sends it to callback
     *
     * @param callback listener for loaded data
     */
    void loadData(AppCallback<T> callback);

    /**
     * removes listener fof loading data
     */
    void abandonCallback();

    /**
     * cancels data loading
     */
    void cancelLoad();

    /**
     * defines loading progress
     *
     * @return true if loads data, false if data is already loaded or loading haven't begun
     */
    boolean isLoading();

    /**
     * sets listener for loading data
     *
     * @param callback listener
     */
    void setCallback(@Nullable AppCallback<App> callback);

    /**
     * clears loaded resources
     */
    void clearCache();

    /**
     * Represents listener for {@link LoadModel}
     *
     * @param <T>
     */
    interface AppCallback<T> {
        /**
         * called when data is loaded
         *
         * @param oldData data, deleted since the previous time
         * @param newData data which is not presented in previous load
         * @param allApps all data, including {@param oldData}
         */
        void onDataLoaded(List<T> oldData, List<T> newData, List<App> allApps);
    }
}

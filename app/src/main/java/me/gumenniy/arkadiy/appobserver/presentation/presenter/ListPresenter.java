package me.gumenniy.arkadiy.appobserver.presentation.presenter;

import android.support.annotation.NonNull;

import java.util.List;

import me.gumenniy.arkadiy.appobserver.dao.model.App;
import me.gumenniy.arkadiy.appobserver.presentation.model.AppModel;
import me.gumenniy.arkadiy.appobserver.presentation.model.LoadModel;
import me.gumenniy.arkadiy.appobserver.presentation.view.ListView;

/**
 * Presenter for {@link ListView<App>} view, retrieves data from {@link LoadModel<App>} instance
 * inside special callback method
 *
 * @see me.gumenniy.arkadiy.appobserver.presentation.model.LoadModel.AppCallback
 */
public class ListPresenter extends BasePresenter<ListView<App>> implements AppModel.AppCallback<App> {

    /**
     * source of loaded data
     */
    private LoadModel<App> model;

    public ListPresenter(LoadModel<App> model) {
        this.model = model;
    }

    /**
     * @param view bind view
     * @see BasePresenter
     * additionally sets view in appropriate state, depending on model loading state.
     * Starts data loading if model is not currently loading data
     */
    @Override
    public void bindView(@NonNull ListView<App> view) {
        super.bindView(view);
        if (model.isLoading()) {
            model.setCallback(this);
            view.showProgress();
        } else {
            startLoad();
        }
    }

    /**
     * starts loading and notifies view
     *
     * @see ListView
     */
    private void startLoad() {
        ListView<App> view = getView();
        if (view != null) {
            view.showProgress();
        }

        model.loadData(this);
    }

    /**
     * notifies view about data loading finish
     *
     * @param oldData data that was deleted since the previous loading
     * @param newData data that is new since the previous loading
     * @param allData all presented data
     */
    private void renderViewData(List<App> oldData, List<App> newData, List<App> allData) {
        ListView<App> view = getView();
        if (view != null) {
            view.renderData(oldData, newData, allData);
            view.hideProgress();
        }
    }

    /**
     * @param isViewDestroyed returns true if view is destroyed for ever, false otherwise
     * @see BasePresenter
     * additionally cancels load and clears cache in order to
     * reload data in the same state the next time
     */
    @Override
    public void unbindView(boolean isViewDestroyed) {
        super.unbindView(isViewDestroyed);
        model.abandonCallback();
        if (isViewDestroyed) {
            model.cancelLoad();
            model.clearCache();
        }
    }

    /**
     * @param oldData data, deleted since the previous load
     * @param newData data, new since the previous time
     * @param allData all presented data including {@param oldData}
     * @see me.gumenniy.arkadiy.appobserver.presentation.model.LoadModel.AppCallback
     */
    @Override
    public void onDataLoaded(List<App> oldData, List<App> newData, List<App> allData) {
        renderViewData(oldData, newData, allData);
    }
}

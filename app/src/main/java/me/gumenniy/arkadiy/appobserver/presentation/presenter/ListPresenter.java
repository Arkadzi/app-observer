package me.gumenniy.arkadiy.appobserver.presentation.presenter;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import me.gumenniy.arkadiy.appobserver.dao.model.App;
import me.gumenniy.arkadiy.appobserver.presentation.model.AppModel;
import me.gumenniy.arkadiy.appobserver.presentation.view.ListView;

/**
 * Created by Arkadiy on 16.04.2016.
 */
public class ListPresenter extends BasePresenter<ListView<App>> implements AppModel.AppCallback {

    private AppModel model;

    public ListPresenter(AppModel model) {
        this.model = model;
    }

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

    private void startLoad() {
        ListView<App> view = getView();
        if (view != null) {
            view.showProgress();
        }

        model.loadData(this);
    }

    private void renderViewData(List<App> oldData, List<App> newData, List<App> allData) {
        ListView<App> view = getView();
        if (view != null) {
            view.renderData(oldData, newData, allData);
            view.hideProgress();
        }
    }

    @Override
    public void unbindView(boolean isViewDestroyed) {
        super.unbindView(isViewDestroyed);
        model.abandonCallback();
        if (isViewDestroyed) {
            model.cancelLoad();
            model.clearCache();
        }
    }

    @Override
    public void onDataLoaded(List<App> oldData, List<App> newData) {
        List<App> deleted = new ArrayList<>(oldData);
        List<App> installed = new ArrayList<>(newData);
        List<App> allApps = new ArrayList<>(newData);

        deleted.removeAll(newData);
        installed.removeAll(oldData);
        allApps.addAll(deleted);
        Collections.sort(allApps, new Comparator<App>() {
            @Override
            public int compare(App lhs, App rhs) {
                return lhs.getAppLabel().compareTo(rhs.getAppLabel());
            }
        });

        renderViewData(deleted, installed, allApps);
    }
}

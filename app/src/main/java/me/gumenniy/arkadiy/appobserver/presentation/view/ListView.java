package me.gumenniy.arkadiy.appobserver.presentation.view;

import java.util.List;

import me.gumenniy.arkadiy.appobserver.dao.model.App;

/**
 * Created by Arkadiy on 16.04.2016.
 */
public interface ListView<T> extends BaseView {
    void showProgress();
    void hideProgress();
    void renderData(List<App> oldData, List<App> newData, List<App> allData);
}

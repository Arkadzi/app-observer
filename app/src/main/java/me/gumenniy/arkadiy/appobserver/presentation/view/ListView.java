package me.gumenniy.arkadiy.appobserver.presentation.view;

import java.util.List;

import me.gumenniy.arkadiy.appobserver.dao.model.App;

/**
 * MVP view, which displays a list of specific data
 */
public interface ListView<T> {
    /**
     * displays loading progress
     */
    void showProgress();

    /**
     * displays finish of loading process
     */
    void hideProgress();

    /**
     * displays loaded data
     *
     * @param oldData
     * @param newData
     * @param allData
     */
    void renderData(List<App> oldData, List<App> newData, List<App> allData);
}

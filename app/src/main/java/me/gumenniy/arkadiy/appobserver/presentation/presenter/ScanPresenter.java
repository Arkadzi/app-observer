package me.gumenniy.arkadiy.appobserver.presentation.presenter;

import me.gumenniy.arkadiy.appobserver.presentation.view.ScanView;

/**
 * Created by Arkadiy on 15.04.2016.
 */
public class ScanPresenter extends BasePresenter<ScanView> {

    public void handleClick() {
        ScanView view = getView();
        if (view != null) {
            view.navigate();
        }
    }
}

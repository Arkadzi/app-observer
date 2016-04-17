package me.gumenniy.arkadiy.appobserver.presentation.presenter;

import me.gumenniy.arkadiy.appobserver.presentation.view.NavigateView;

/**
 * presenter for MVP {@link NavigateView} view
 */
public class NavigatePresenter extends BasePresenter<NavigateView> {

    /**
     * handles click on view's button
     */
    public void handleClick() {
        NavigateView view = getView();
        if (view != null) {
            view.navigate();
        }
    }
}

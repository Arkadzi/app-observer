package me.gumenniy.arkadiy.appobserver.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import me.gumenniy.arkadiy.appobserver.R;
import me.gumenniy.arkadiy.appobserver.presentation.presenter.NavigatePresenter;
import me.gumenniy.arkadiy.appobserver.presentation.view.NavigateView;

/**
 * fragment which contains single button to launch another fragment
 */
public class NavigateFragment extends Fragment implements NavigateView {
    /**
     * MVP presenter for this fragment
     */
    private NavigatePresenter presenter;

    public NavigateFragment() {
        // Required empty public constructor
    }

    /**
     * instantiates new {@link NavigateFragment} instance with non-null arguments
     *
     * @return instantiated fragment
     */
    public static Fragment newInstance() {
        Fragment fragment = new NavigateFragment();

        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    /**
     * creates presenter for this fragment
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new NavigatePresenter();
    }

    /**
     * inflates view to display, binds vies to fields,
     * sets {@link android.view.View.OnClickListener} for button
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_scan, container, false);

        Button scanButton = (Button) view.findViewById(R.id.scan_button);
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.handleClick();
            }
        });
        return view;
    }

    /**
     * binds fragment to presenter
     *
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.bindView(this);
    }

    /**
     * unbinds fragment from presenter
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.unbindView(isRemoving());
    }

    /**
     * replaces current fragment with instantiated {@link AppListFragment}
     */
    @Override
    public void navigate() {
        getFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_in, R.anim.pop_out)
                .replace(R.id.fragment_container, AppListFragment.newInstance())
                .addToBackStack(null)
                .commit();
    }
}

package me.gumenniy.arkadiy.appobserver.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

import me.gumenniy.arkadiy.appobserver.R;
import me.gumenniy.arkadiy.appobserver.adapters.AppAdapter;
import me.gumenniy.arkadiy.appobserver.dao.model.App;
import me.gumenniy.arkadiy.appobserver.presentation.model.AppModel;
import me.gumenniy.arkadiy.appobserver.presentation.presenter.ListPresenter;

/**
 * Displays deleted and presented apps inside {@link ListView}
 */
public class AppListFragment extends Fragment implements me.gumenniy.arkadiy.appobserver.presentation.view.ListView<App> {

    /**
     * {@link ListView} adapter
     */
    private AppAdapter adapter;
    /**
     * MVP presenter for this view.
     */
    private ListPresenter presenter;
    private ListView listView;
    /**
     * view, which displayed during data loading
     */
    private View progressView;

    public AppListFragment() {
        // Required empty public constructor
    }

    /**
     * instantiates new {@link AppListFragment} instance with non-null arguments
     *
     * @return instantiated fragment
     */
    public static Fragment newInstance() {
        Fragment fragment = new AppListFragment();

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
        presenter = new ListPresenter(AppModel.getInstance(getActivity()));
    }

    /**
     * inflates view to display, binds vies to fields, sets empty adapter into listView
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_app_list, container, false);
        listView = (ListView) view.findViewById(R.id.list_view);
        progressView = view.findViewById(R.id.progress_bar);

        adapter = new AppAdapter(getContext());
        listView.setAdapter(adapter);
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

    @Override
    public void showProgress() {
        listView.setVisibility(View.INVISIBLE);
        progressView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        listView.setVisibility(View.VISIBLE);
        progressView.setVisibility(View.INVISIBLE);
    }

    /**
     * updates data inside {@link AppAdapter}, which automatically
     * notifies changes inside {@link ListView}
     *
     * @param oldData deleted apps
     * @param newData new installed apps
     * @param allData all apps including deleted apps
     */
    @Override
    public void renderData(List<App> oldData, List<App> newData, List<App> allData) {
        adapter.setData(oldData, newData, allData);
    }
}

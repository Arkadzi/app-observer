package me.gumenniy.arkadiy.appobserver.fragments;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

import me.gumenniy.arkadiy.appobserver.R;
import me.gumenniy.arkadiy.appobserver.adapters.AppAdapter;
import me.gumenniy.arkadiy.appobserver.dao.model.App;
import me.gumenniy.arkadiy.appobserver.presentation.model.AppModel;
import me.gumenniy.arkadiy.appobserver.presentation.presenter.ListPresenter;

/**
 * A simple {@link Fragment} subclass.
 */
public class AppListFragment extends Fragment implements me.gumenniy.arkadiy.appobserver.presentation.view.ListView<App> {


    private AppAdapter adapter;
    private ListPresenter presenter;
    private ListView listView;
    private View progressView;

    public static Fragment newInstance() {
        Fragment fragment = new AppListFragment();

        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }


    public AppListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new ListPresenter(AppModel.getInstance(getActivity()));
    }

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

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.bindView(this);
    }

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

    @Override
    public void renderData(List<App> oldData, List<App> newData, List<App> allData) {
        adapter.setData(oldData, newData, allData);
    }
}

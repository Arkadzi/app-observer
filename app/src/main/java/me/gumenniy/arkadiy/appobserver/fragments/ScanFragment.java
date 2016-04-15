package me.gumenniy.arkadiy.appobserver.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import me.gumenniy.arkadiy.appobserver.R;
import me.gumenniy.arkadiy.appobserver.presentation.presenter.ScanPresenter;
import me.gumenniy.arkadiy.appobserver.presentation.view.BaseView;
import me.gumenniy.arkadiy.appobserver.presentation.view.ScanView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ScanFragment extends Fragment implements ScanView {

    private ScanPresenter presenter;

    public static Fragment newInstance() {
        Fragment fragment = new ScanFragment();

        Bundle args = new Bundle();
        fragment.setArguments(args);

        return fragment;
    }

    public ScanFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new ScanPresenter();
    }

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
        presenter.bindView(this);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.bindView(null);
    }

    @Override
    public void navigate() {
        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, AppListFragment.newInstance())
                .addToBackStack(null)
                .commit();
    }
}

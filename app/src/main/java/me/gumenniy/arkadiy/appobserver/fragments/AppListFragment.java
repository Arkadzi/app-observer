package me.gumenniy.arkadiy.appobserver.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.BufferedReader;

import me.gumenniy.arkadiy.appobserver.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AppListFragment extends Fragment {


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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_app_list, container, false);
    }
}

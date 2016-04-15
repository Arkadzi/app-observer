package me.gumenniy.arkadiy.appobserver.fragments;


import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.provider.SyncStateContract;
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
        View view = inflater.inflate(R.layout.fragment_app_list, container, false);
        ListView listView = (ListView) view.findViewById(R.id.list_view);

        listView.setAdapter(new ArrayAdapter<>(container.getContext(), R.layout.list_item, R.id.title,  getInstalledComponentList()));

        return view;
    }

    private List<String> getInstalledComponentList() {
        Intent componentSearchIntent = new Intent(Intent.ACTION_MAIN, null);
        componentSearchIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> ril = getActivity().getPackageManager()
                .queryIntentActivities(componentSearchIntent, 0);
        List<String> componentList = new ArrayList<>();
        for (ResolveInfo ri : ril) {
            if (ri.activityInfo != null) {
                componentList.add(ri.activityInfo.packageName);
            }
        }
        return componentList;
    }
}

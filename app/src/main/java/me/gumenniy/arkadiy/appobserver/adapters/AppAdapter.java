package me.gumenniy.arkadiy.appobserver.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import me.gumenniy.arkadiy.appobserver.R;
import me.gumenniy.arkadiy.appobserver.dao.model.App;

/**
 * Created by Arkadiy on 16.04.2016.
 */
public class AppAdapter extends BaseAdapter {

    @NonNull
    private final LayoutInflater inflater;
    private final String deleted;
    private final String installed;
    private final String noChanges;
    @NonNull
    private List<App> apps;
    @NonNull
    private List<App> deletedApps;
    @NonNull
    private List<App> newApps;

    public AppAdapter(Context context) {
        this.apps = new ArrayList<>();
        this.deletedApps = new ArrayList<>();
        this.newApps = new ArrayList<>();

        inflater = LayoutInflater.from(context);

        deleted = context.getString(R.string.deleted);
        installed = context.getString(R.string.newApp);
        noChanges = context.getString(R.string.noChanges);
    }

    @Override
    public int getCount() {
        return apps.size();
    }

    @Override
    public App getItem(int position) {
        return apps.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = getView(convertView, parent);
        ViewHolder holder = (ViewHolder) view.getTag();

        App item = getItem(position);
        holder.packageTextView.setText(item.getAppPackage());
        holder.labelTextView.setText(item.getAppLabel());
        holder.icon.setImageDrawable(item.getIcon());
        if (deletedApps.contains(item)) {
            holder.subTextView.setText(deleted);
        } else if (newApps.contains(item)) {
            holder.subTextView.setText(installed);
        } else {
            holder.subTextView.setText(noChanges);
        }

        return view;
    }

    private View getView(View convertView, ViewGroup parent) {
        View result = convertView;

        if (result == null) {
            result = inflater.inflate(R.layout.list_item, parent, false);
            ViewHolder holder = new ViewHolder(result);
            result.setTag(holder);
        }

        return result;
    }

    public void setData(@NonNull List<App> deletedApps,
                        @NonNull List<App> newApps, @NonNull List<App> apps) {
        updateList(this.apps, apps);
        updateList(this.deletedApps, deletedApps);
        updateList(this.newApps, newApps);

        notifyDataSetChanged();

    }

    private void updateList(List<App> oldList, List<App> newList) {
        oldList.clear();
        oldList.addAll(newList);
    }

    static class ViewHolder {
        private final View view;
        private final TextView labelTextView;
        private final TextView subTextView;
        private final TextView packageTextView;
        private final ImageView icon;

        public ViewHolder(View view) {
            this.view = view;
            labelTextView = (TextView) view.findViewById(R.id.label_text);
            subTextView = (TextView) view.findViewById(R.id.sub_text);
            packageTextView = (TextView) view.findViewById(R.id.package_name_text);
            icon = (ImageView) view.findViewById(R.id.app_icon_view);
        }
    }
}

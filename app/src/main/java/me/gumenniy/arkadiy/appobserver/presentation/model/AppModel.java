package me.gumenniy.arkadiy.appobserver.presentation.model;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.greenrobot.dao.async.AsyncOperationListener;
import me.gumenniy.arkadiy.appobserver.dao.model.App;
import me.gumenniy.arkadiy.appobserver.dao.model.AppDao;
import me.gumenniy.arkadiy.appobserver.dao.model.DaoMaster;
import me.gumenniy.arkadiy.appobserver.dao.model.DaoSession;

/**
 * Created by Arkadiy on 16.04.2016.
 */
public class AppModel {
    public static final String DB_NAME = "app-db";
    private static AppModel instance;

    private final Context context;
    private DaoMaster.DevOpenHelper helper;
    private DaoSession daoSession;
    private SQLiteDatabase database;
    @Nullable
    private AppCallback callback;
    @Nullable
    private List<App> oldData;
    @Nullable
    private List<App> newData;
    @Nullable
    private AppAsyncLoader loader;

    public static AppModel getInstance(Context context) {
        if (instance == null) {
            instance = new AppModel(context.getApplicationContext());
        }
        return instance;
    }

    public AppModel(Context context) {
        this.context = context;
        helper = new DaoMaster.DevOpenHelper(context, DB_NAME, null);
    }

    public void loadData(AppCallback callback) {
        if (oldData == null || newData == null) {
            this.callback = callback;
            loadData();
        } else {
            callback.onDataLoaded(oldData, newData);
        }
    }

    public void abandonCallback() {
        callback = null;
    }

    private void loadData() {
        loader = new AppAsyncLoader();
        loader.execute();
    }

    private List<App> getInstalledComponentList() {
        Intent componentSearchIntent = new Intent(Intent.ACTION_MAIN, null);
        componentSearchIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> infoList = context.getPackageManager()
                .queryIntentActivities(componentSearchIntent, 0);

        List<App> componentList = new ArrayList<>();
        Date date = new Date();
        for (ResolveInfo info : infoList) {
            if (info.activityInfo != null) {
                App app = new App(info.activityInfo.packageName, date);
                if (!componentList.contains(app)) {
                    componentList.add(app);
                }
            }
        }
        return componentList;
    }

    public void cancelLoad() {
        if (loader != null && !loader.isCancelled()) {
            loader.cancel(true);
            loader = null;
            Log.e("Model"," cancelLoad");
        }
    }

    public boolean isLoading() {
        return loader != null;
    }

    public void setCallback(@Nullable AppCallback callback) {
        this.callback = callback;
    }

    public interface AppCallback {
        void onDataLoaded(List<App> oldData, List<App> newData);
    }

    private void openDbConnection() {
        database = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(database);
        daoSession = daoMaster.newSession();
    }

    private void closeDbConnection() {
        if (daoSession != null) {
            daoSession.clear();
            daoSession = null;
        }
        if (database != null && database.isOpen()) {
            database.close();
            database = null;
        }
    }

    public void clearCache() {
        oldData = null;
        newData = null;
    }

    private class AppAsyncLoader extends AsyncTask<Void, Void, List<List<App>>> {

        @Override
        protected List<List<App>> doInBackground(Void... params) {
            List<List<App>> lists = new ArrayList<>(2);

            List<App> newApps = getInstalledComponentList();

            openDbConnection();

            AppDao appDao = daoSession.getAppDao();
            List<App> oldApps = appDao.loadAll();
            lists.add(oldApps);
            lists.add(newApps);

            appDao.insertOrReplaceInTx(newApps);

            if (!isCancelled()) {
                closeDbConnection();
            }
            return lists;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            closeDbConnection();
            clearCache();
        }

        @Override
        protected void onPostExecute(List<List<App>> lists) {
            Log.e("Model", "onPostExecute()");
            oldData = lists.get(0);
            newData = lists.get(1);

            if (callback != null) {
                callback.onDataLoaded(oldData, newData);
            }

            loader = null;
        }

    }
}

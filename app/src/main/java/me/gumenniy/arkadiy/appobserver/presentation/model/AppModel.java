package me.gumenniy.arkadiy.appobserver.presentation.model;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import me.gumenniy.arkadiy.appobserver.dao.model.App;
import me.gumenniy.arkadiy.appobserver.dao.model.AppDao;
import me.gumenniy.arkadiy.appobserver.dao.model.DaoMaster;
import me.gumenniy.arkadiy.appobserver.dao.model.DaoSession;

/**
 * Scans device and retrieves all installed apps and deleted apps,
 * returns them into {@link me.gumenniy.arkadiy.appobserver.presentation.model.LoadModel.AppCallback} instance
 * and caches in memory
 */
public class AppModel implements LoadModel<App> {
    /**
     * local database name
     */
    public static final String DB_NAME = "app-db";

    /**
     * singleton instance of {@link AppModel}
     */
    private static AppModel instance;

    /**
     * used for accessing local database and other device-specific data
     */
    private final Context context;
    /**
     * local database helper
     */
    private DaoMaster.DevOpenHelper helper;
    /**
     * local database session
     */
    private DaoSession daoSession;
    /**
     * local database
     */
    private SQLiteDatabase database;
    /**
     * listener for loaded data
     */
    @Nullable
    private LoadModel.AppCallback<App> callback;
    /**
     * asynchronous loader of all data. Caches all data
     * and notifies callback
     *
     * @see me.gumenniy.arkadiy.appobserver.presentation.model.LoadModel.AppCallback
     */
    @Nullable
    private AppAsyncLoader loader;

    private ArrayList<App> deletedApps;
    private ArrayList<App> installedApps;
    private ArrayList<App> allApps;

    public AppModel(Context context) {
        this.context = context;
        helper = new DaoMaster.DevOpenHelper(context, DB_NAME, null);
    }

    /**
     * creates if necessary and returns {@link AppModel} singleton instance
     *
     * @param context used for creating {@link AppModel} instance
     * @return singleton instance of {@link AppModel}
     */
    public static AppModel getInstance(Context context) {
        if (instance == null) {
            instance = new AppModel(context.getApplicationContext());
        }
        return instance;
    }

    @Override
    public void loadData(LoadModel.AppCallback<App> callback) {
        if (deletedApps == null || installedApps == null || allApps == null) {
            this.callback = callback;
            loadData();
        } else {
            callback.onDataLoaded(deletedApps, installedApps, allApps);
        }
    }

    /**
     * starts asynchronous data loading
     */
    private void loadData() {
        loader = new AppAsyncLoader();
        loader.execute();
    }

    @Override
    public void abandonCallback() {
        callback = null;
    }

    @Override
    public void cancelLoad() {
        if (loader != null && !loader.isCancelled()) {
            loader.cancel(true);
            loader = null;
            Log.e("Model", " cancelLoad");
        }
    }

    @Override
    public boolean isLoading() {
        return loader != null;
    }

    @Override
    public void setCallback(@Nullable AppCallback<App> callback) {
        this.callback = callback;
    }

    @Override
    public void clearCache() {
        deletedApps = null;
        installedApps = null;
        allApps = null;
    }

    /**
     * loads list of {@link App} installed on device
     *
     * @return list of installed {@link App}
     */
    private List<App> loadInstalledApps() {
        PackageManager packageManager = context.getPackageManager();
        List<ApplicationInfo> infoList = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);
        List<App> apps = new ArrayList<>();
        Date date = new Date();
        for (ApplicationInfo info : infoList) {
            try {
                String packageName = info.packageName;
                if (packageManager.getLaunchIntentForPackage(packageName) != null) {
                    Drawable icon = info.loadIcon(packageManager);
                    String label = info.loadLabel(packageManager).toString();

                    App app = new App(packageName, label, date);
                    app.setIcon(icon);

                    if (!apps.contains(app)) {
                        apps.add(app);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return apps;
    }

    /**
     * opens local database connection and begins new session
     */
    private void openDbConnection() {
        database = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(database);
        daoSession = daoMaster.newSession();
    }

    /**
     * clears current database session and closes local database
     */
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

    /**
     * asynchronously loads data from local database and data about apps currently installed on the device
     */
    private class AppAsyncLoader extends AsyncTask<Void, Void, List<List<App>>> {

        @Override
        protected List<List<App>> doInBackground(Void... params) {
            List<List<App>> lists = new ArrayList<>(2);

            List<App> newApps = loadInstalledApps();

            openDbConnection();

            AppDao appDao = daoSession.getAppDao();
            List<App> oldApps = appDao.loadAll();
            lists.add(oldApps);
            lists.add(newApps);

            appDao.deleteAll();
            appDao.insertInTx(newApps);

            if (!isCancelled()) {
                closeDbConnection();
            }
            return lists;
        }

        /**
         * closes database connection if it is opened and clears cache
         */
        @Override
        protected void onCancelled() {
            super.onCancelled();
            closeDbConnection();
            clearCache();
        }

        /**
         * saves cache and notifies callback if such presented
         *
         * @param lists asynchronously loaded data
         */
        @Override
        protected void onPostExecute(List<List<App>> lists) {
            Log.e("Model", "onPostExecute()");
            if (lists.size() == 2) {
                List<App> dbData = lists.get(0);
                List<App> deviceData = lists.get(1);

                deletedApps = new ArrayList<>(dbData);
                installedApps = new ArrayList<>(deviceData);
                allApps = new ArrayList<>(deviceData);

                deletedApps.removeAll(deviceData);
                installedApps.removeAll(dbData);
                allApps.addAll(deletedApps);
                Collections.sort(allApps, new Comparator<App>() {
                    @Override
                    public int compare(App lhs, App rhs) {
                        return lhs.getAppLabel().compareTo(rhs.getAppLabel());
                    }
                });

                if (callback != null) {
                    callback.onDataLoaded(deletedApps, installedApps, allApps);
                }
            }
            loader = null;
        }
    }
}

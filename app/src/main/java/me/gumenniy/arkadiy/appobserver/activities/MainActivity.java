package me.gumenniy.arkadiy.appobserver.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import me.gumenniy.arkadiy.appobserver.R;
import me.gumenniy.arkadiy.appobserver.fragments.NavigateFragment;

/**
 * Main activity. Holds fragments
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prepareToolbar();
        showFragmentIfNeed();
    }

    /**
     * sets {@link Toolbar} as action bar
     */
    private void prepareToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
    }

    /**
     * launches {@link NavigateFragment} for the first entrance
     */
    private void showFragmentIfNeed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_container, NavigateFragment.newInstance())
                    .commit();
        }
    }
}

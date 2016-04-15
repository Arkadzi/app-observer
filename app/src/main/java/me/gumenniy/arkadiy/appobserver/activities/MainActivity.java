package me.gumenniy.arkadiy.appobserver.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import me.gumenniy.arkadiy.appobserver.R;
import me.gumenniy.arkadiy.appobserver.fragments.ScanFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prepareToolbar();

        showFragmentIfNeed();
    }

    private void showFragmentIfNeed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_container, ScanFragment.newInstance())
                    .commit();
        }
    }

    private void prepareToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
    }
}

package com.cj186.booktracker;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.Toolbar;

import com.cj186.booktracker.sharedfragments.AboutFragment;
import com.cj186.booktracker.sharedfragments.SettingsFragment;

/**
 * Collin J. Johnson
 * 5/6/2025
 * 2376 Mobile Applications Development
 *
 * This activity is inherited by all activities, it contains the toolbar and theme methods.
 */

public class BaseActivity extends AppCompatActivity {
    //private CheckBox useDarkmode;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        // Get the userDarkmode button and set its listener.
        /*useDarkmode = findViewById(R.id.use_darkmode);
        useDarkmode.setOnClickListener(this::changeTheme);*/
        // Initialize our theme and toolbar.
        initTheme();
        setupToolbar(R.id.toolbar);
    }

    protected void setupToolbar(int toolbarId) {
        // Set the toolbar and make it the support action bar.
        Toolbar toolbar = findViewById(toolbarId);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayShowTitleEnabled(false);
            }
        }
    }

    private void checkBoxChecked(boolean checked, CheckBox checkBox){
        // Change the icon for the checkbox.
        Drawable day = AppCompatResources.getDrawable(this, R.drawable.day);
        Drawable night = AppCompatResources.getDrawable(this, R.drawable.night);

        checkBox.setChecked(checked);
        checkBox.setBackground(checked ? night : day);
    }

    protected void initTheme(){
        // Get the app preference for darkmode
        SharedPreferences pref = this.getSharedPreferences("APP_PREFERENCES", MODE_PRIVATE);
        boolean isDarkmode = pref.getBoolean("USE_DARKMODE", (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES);

        // Set darkmode on or off.
        //checkBoxChecked(isDarkmode, useDarkmode);
        if(isDarkmode)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

    protected void changeTheme(View view){
//        // Put USE_DARKMODE into preferences.
//        SharedPreferences pref = this.getSharedPreferences("APP_PREFERENCES", MODE_PRIVATE);
//        pref.edit().putBoolean("USE_DARKMODE", useDarkmode.isChecked()).apply();
//        // Set the theme.
//        if(useDarkmode.isChecked())
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//        else
//            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the overflow menu.
        getMenuInflater().inflate(R.menu.about, menu);
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.about) {
            // Open the about dialog.
            AboutFragment dialog = new AboutFragment();
            dialog.show(getSupportFragmentManager(), "aboutFragment");
            return true;
        }
        if (item.getItemId() == R.id.settings){
            // Open the settings dialog.
            SettingsFragment dialog = new SettingsFragment();
            dialog.show(getSupportFragmentManager(), "settingsFragment");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

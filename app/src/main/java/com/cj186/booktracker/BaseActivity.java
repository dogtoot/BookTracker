package com.cj186.booktracker;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.Toolbar;

import com.cj186.booktracker.sharedfragments.AboutFragment;

public class BaseActivity extends AppCompatActivity {
    private CheckBox useDarkmode;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        useDarkmode = findViewById(R.id.use_darkmode);
        useDarkmode.setOnClickListener(this::changeTheme);
        initTheme();
        setupToolbar(R.id.toolbar);
    }

    protected void setupToolbar(int toolbarId) {
        Toolbar toolbar = findViewById(toolbarId);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    private void checkBoxChecked(boolean checked, CheckBox checkBox){
        Drawable day = AppCompatResources.getDrawable(this, R.drawable.day);
        Drawable night = AppCompatResources.getDrawable(this, R.drawable.night);

        checkBox.setChecked(checked);
        checkBox.setBackground(checked ? night : day);
    }

    protected void initTheme(){
        SharedPreferences pref = this.getSharedPreferences("APP_PREFERENCES", MODE_PRIVATE);
        boolean isDarkmode = pref.getBoolean("USE_DARKMODE", (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES);

        checkBoxChecked(isDarkmode, useDarkmode);
        if(isDarkmode)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

    protected void changeTheme(View view){
        SharedPreferences pref = this.getSharedPreferences("APP_PREFERENCES", MODE_PRIVATE);
        pref.edit().putBoolean("USE_DARKMODE", useDarkmode.isChecked()).apply();
        if(useDarkmode.isChecked())
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.about, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.about) {
            AboutFragment dialog = new AboutFragment();
            dialog.show(getSupportFragmentManager(), "aboutFragment");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

package com.cj186.booktracker;

import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.cj186.booktracker.sharedfragments.AboutFragment;

public class BaseActivity extends AppCompatActivity {
    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        setupToolbar(R.id.toolbar);
    }

    private void setupToolbar(int toolbarId){
        Toolbar toolbar = findViewById(toolbarId);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
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

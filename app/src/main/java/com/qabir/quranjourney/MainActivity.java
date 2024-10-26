package com.qabir.quranjourney;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.qabir.utility.CommonUtil;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, NavigationBarView.OnItemSelectedListener {

    private static final int PERMISSION_ID_PHONESTATE = 109;
    DrawerLayout drawer;
    NavigationView navigationView;
    BottomNavigationView bottomNavigationView;
    Toolbar toolbar = null;
    public boolean isRunningActivity = false;
    private int resumeCount = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);
        bottomNavigationView.setOnItemSelectedListener(this);

        // show dashboard fragment
        this.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new FragmentDashboard()).commit();

        initilizeRequiredVersionSetUp();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            //close Dwawer if open on back press
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //Go back to dashboard if not showing dashboard on back press or close app if showing
            Fragment f = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            if (f instanceof FragmentDashboard) {
                super.onBackPressed();
            } else {
                this.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new FragmentDashboard()).commit();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent idd = new Intent(this, SettingsActivity.class);
            startActivity(idd);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        CommonUtil.onNavigationItemSelected(this, item);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isRunningActivity = true;
        resumeCount++;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isRunningActivity = false;
    }

    public boolean isRunningActivity() {
        return isRunningActivity;
    }


    private void initilizeRequiredVersionSetUp() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.READ_PHONE_STATE}, PERMISSION_ID_PHONESTATE);
        }
    }

    public int getResumeCount() {
        return resumeCount;
    }

    public BottomNavigationView getBottomNevigationView() {
        return bottomNavigationView;
    }

    public NavigationView getSideNevigationView() {
        return navigationView;
    }
}

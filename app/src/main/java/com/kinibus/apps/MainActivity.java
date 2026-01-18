package com.kinibus.apps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.kinibus.apps.views.activities.DashboardActivity;
import com.kinibus.apps.views.activities.ProfileActivity;
import com.kinibus.apps.views.fragments.HistoryFragment;
import com.kinibus.apps.views.fragments.SettingsFragment;
import com.kinibus.apps.views.fragments.HomeFragment;

public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    private BottomNavigationView bottomNavigationView;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        fragmentManager = getSupportFragmentManager();

        // Setup bottom navigation
        bottomNavigationView.setOnItemSelectedListener(this);

        // Load default fragment (Home/Dashboard)
        if (savedInstanceState == null) {
            loadFragment(new HomeFragment(), false);
            bottomNavigationView.setSelectedItemId(R.id.nav_home);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.nav_home) {
            loadFragment(new HomeFragment(), false);
            return true;
        } else if (itemId == R.id.nav_booking) {
            // Navigate to DashboardActivity for booking
            Intent intent = new Intent(this, DashboardActivity.class);
            startActivity(intent);
            return false; // Don't change fragment
        } else if (itemId == R.id.nav_profile) {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
            return false; // Don't change fragment
        } else if (itemId == R.id.nav_history) {
            loadFragment(new HistoryFragment(), false);
            return true;
        } else if (itemId == R.id.nav_settings) {
            loadFragment(new SettingsFragment(), false);
            return true;
        }

        return false;
    }

    private void loadFragment(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);

        if (addToBackStack) {
            transaction.addToBackStack(null);
        }

        transaction.commit();
    }
}

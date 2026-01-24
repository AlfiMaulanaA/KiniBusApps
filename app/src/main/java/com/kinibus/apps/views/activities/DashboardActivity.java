package com.kinibus.apps.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.kinibus.apps.R;
import com.kinibus.apps.views.fragments.EditTripBottomSheetFragment;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DashboardActivity extends AppCompatActivity implements
        EditTripBottomSheetFragment.OnTripDetailsChangedListener {

    private String origin = null; // No default route - user must select
    private String destination = null;
    private Date tripDate = null;
    private String passengers = "1 Orang";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        initializeViews();
        setupBottomNavigation();
    }

    private Date getInitialDate() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        cal.set(2026, Calendar.JANUARY, 13);
        return cal.getTime();
    }

    private void initializeViews() {
        // Add click handler for search button
        findViewById(R.id.search_button).setOnClickListener(v -> {
            // Use current values or defaults for the edit dialog
            String currentOrigin = origin != null ? origin : "Gambir";
            String currentDestination = destination != null ? destination : "Pasteur";
            long currentDateTime = tripDate != null ? tripDate.getTime() : getInitialDate().getTime();

            EditTripBottomSheetFragment bottomSheet = EditTripBottomSheetFragment.newInstance(
                    currentOrigin, currentDestination, currentDateTime, passengers);
            bottomSheet.show(getSupportFragmentManager(), "EditTripBottomSheetFragment");
        });
    }

    private void setupBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Set Home as selected by default
        bottomNavigationView.setSelectedItemId(R.id.nav_home);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_home) {
                // Already on home, no action needed
                return true;
            } else if (itemId == R.id.nav_booking) {
                // Navigate to booking/history
                Toast.makeText(this, "Booking clicked", Toast.LENGTH_SHORT).show();
                return true;
            } else if (itemId == R.id.nav_history) {
                // Navigate to history
                Toast.makeText(this, "History clicked", Toast.LENGTH_SHORT).show();
                return true;
            } else if (itemId == R.id.nav_profile) {
                // Navigate to profile
                Intent profileIntent = new Intent(this, ProfileActivity.class);
                startActivity(profileIntent);
                return true;
            } else if (itemId == R.id.nav_settings) {
                // Navigate to settings
                Toast.makeText(this, "Settings clicked", Toast.LENGTH_SHORT).show();
                return true;
            }

            return false;
        });
    }

    @Override
    public void onTripDetailsChanged(String origin, String destination, Date date, String passengers) {
        this.origin = origin;
        this.destination = destination;
        this.tripDate = date;
        this.passengers = passengers;

        // For search layout, we would navigate to results activity
        // This is a simplified version - in full implementation you would:
        // 1. Navigate to BusResultsActivity
        // 2. Pass the search parameters
        // 3. Let BusResultsActivity handle the bus listings

        Toast.makeText(this, "Searching for: " + origin + " → " + destination, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

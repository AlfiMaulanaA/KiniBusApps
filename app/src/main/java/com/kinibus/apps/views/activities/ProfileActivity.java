package com.kinibus.apps.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.kinibus.apps.R;
import com.kinibus.apps.repositories.AuthRepository;
import com.kinibus.apps.viewmodels.AuthViewModel;

/**
 * ProfileActivity - Halaman profil user GoBus style
 * Menampilkan info user, upcoming trip, booking history, settings
 */
public class ProfileActivity extends AppCompatActivity {
    private static final String TAG = "ProfileActivity";

    // ViewModel & Repository
    private AuthViewModel authViewModel;
    private AuthRepository authRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize components
        initializeViewModel();
        setupClickListeners();

        Log.d(TAG, "ProfileActivity created - GoBus profile screen");
    }

    /**
     * Initialize ViewModel
     */
    private void initializeViewModel() {
        authRepository = AuthRepository.getInstance();
        authViewModel = new ViewModelProvider(this).get(AuthViewModel.class);
    }

    /**
     * Setup click listeners
     */
    private void setupClickListeners() {
        // Back button
        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish());

        // Edit button
        MaterialButton editButton = findViewById(R.id.edit_button);
        editButton.setOnClickListener(v -> {
            Toast.makeText(this, "Edit profile - Akan diimplementasikan", Toast.LENGTH_SHORT).show();
        });

        // Profile image click
        findViewById(R.id.profile_image).setOnClickListener(v -> {
            Toast.makeText(this, "Change profile picture - Akan diimplementasikan", Toast.LENGTH_SHORT).show();
        });

        // Upcoming trip click
        findViewById(R.id.upcoming_trip_card).setOnClickListener(v -> {
            Toast.makeText(this, "View upcoming trip details - Akan diimplementasikan", Toast.LENGTH_SHORT).show();
        });

        // "Lihat Tiket" click
        findViewById(R.id.view_tickets_link).setOnClickListener(v -> {
            Toast.makeText(this, "View all tickets - Akan diimplementasikan", Toast.LENGTH_SHORT).show();
        });

        // "Lihat Semua" history click
        findViewById(R.id.view_all_history_link).setOnClickListener(v -> {
            Toast.makeText(this, "View all booking history - Akan diimplementasikan", Toast.LENGTH_SHORT).show();
        });

        // Settings menu items
        setupSettingsMenu();

        // Bottom navigation
        setupBottomNavigation();
    }

    /**
     * Setup settings menu click listeners
     */
    private void setupSettingsMenu() {
        findViewById(R.id.account_settings).setOnClickListener(v -> {
            Toast.makeText(this, "Account settings - Akan diimplementasikan", Toast.LENGTH_SHORT).show();
        });

        findViewById(R.id.payment_methods).setOnClickListener(v -> {
            Toast.makeText(this, "Payment methods - Akan diimplementasikan", Toast.LENGTH_SHORT).show();
        });

        findViewById(R.id.language_settings).setOnClickListener(v -> {
            Toast.makeText(this, "Language settings - Akan diimplementasikan", Toast.LENGTH_SHORT).show();
        });

        findViewById(R.id.help_support).setOnClickListener(v -> {
            Toast.makeText(this, "Help & Support - Akan diimplementasikan", Toast.LENGTH_SHORT).show();
        });
    }

    /**
     * Setup bottom navigation click listeners
     */
    private void setupBottomNavigation() {
        // Home tab
        findViewById(R.id.nav_home).setOnClickListener(v -> {
            Intent intent = new Intent(this, DashboardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        });

        // Schedule tab
        findViewById(R.id.nav_schedule).setOnClickListener(v -> {
            Toast.makeText(this, "Jadwal - Akan diimplementasikan", Toast.LENGTH_SHORT).show();
        });

        // Tickets tab
        findViewById(R.id.nav_tickets).setOnClickListener(v -> {
            Toast.makeText(this, "Tiket - Akan diimplementasikan", Toast.LENGTH_SHORT).show();
        });

        // Profile tab - already selected (do nothing)
    }

    /**
     * Handle logout
     */
    private void handleLogout() {
        Log.d(TAG, "User logout initiated");

        // Clear authentication state
        authViewModel.logout();

        // Show logout message
        Toast.makeText(this, "Logout berhasil", Toast.LENGTH_SHORT).show();

        // Navigate back to welcome screen
        Intent intent = new Intent(this, WelcomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();

        // Check authentication status
        boolean isAuthenticated = authRepository.isAuthenticated.getValue() != null &&
                                authRepository.isAuthenticated.getValue();
        if (!isAuthenticated) {
            Log.d(TAG, "User not authenticated, redirecting to login");
            Intent intent = new Intent(this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        } else {
            // Load user data if authenticated
            loadUserData();
        }
    }

    /**
     * Load user data and display
     */
    private void loadUserData() {
        // Get user data from repository or viewmodel
        String userEmail = authRepository.getCurrentUserEmail();
        String userName = "Budi Santoso"; // Mock data for demo

        if (userEmail != null && !userEmail.isEmpty()) {
            TextView emailText = findViewById(R.id.user_email);
            emailText.setText(userEmail);

            TextView nameText = findViewById(R.id.user_name);
            nameText.setText(userName);

            Log.d(TAG, "Loaded user data: " + userName + " (" + userEmail + ")");
        } else {
            Log.w(TAG, "No user data available");
        }

        // Setup logout button
        MaterialButton logoutButton = findViewById(R.id.logout_button);
        logoutButton.setOnClickListener(v -> handleLogout());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "ProfileActivity destroyed");
    }
}

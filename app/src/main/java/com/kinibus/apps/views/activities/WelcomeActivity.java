package com.kinibus.apps.views.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.button.MaterialButton;
import com.kinibus.apps.R;

/**
 * WelcomeActivity - Halaman welcome/onboarding sebelum login
 * Menampilkan hero section dengan branding dan call-to-action buttons
 */
public class WelcomeActivity extends AppCompatActivity {
    private static final String TAG = "WelcomeActivity";

    // UI Components
    private MaterialButton loginButton, registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // Initialize views
        initializeViews();

        // Setup click listeners
        setupClickListeners();

        Log.d(TAG, "WelcomeActivity created - showing onboarding screen");
    }

    /**
     * Initialize UI components
     */
    private void initializeViews() {
        loginButton = findViewById(R.id.login_button);
        registerButton = findViewById(R.id.register_button);
    }

    /**
     * Setup click listeners for buttons
     */
    private void setupClickListeners() {
        // Login button - navigate to LoginActivity
        loginButton.setOnClickListener(v -> {
            Log.d(TAG, "Login button clicked - navigating to LoginActivity");
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        });

        // Register button - navigate to RegisterActivity
        registerButton.setOnClickListener(v -> {
            Log.d(TAG, "Register button clicked - navigating to RegisterActivity");
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        });

        // Skip button - navigate directly to Dashboard (guest mode)
        findViewById(R.id.skip_text).setOnClickListener(v -> {
            Log.d(TAG, "Skip button clicked - navigating to Dashboard (guest mode)");
            navigateToDashboard();
        });
    }

    /**
     * Navigate to Dashboard for guest users
     */
    private void navigateToDashboard() {
        Intent intent = new Intent(this, DashboardActivity.class);
        intent.putExtra("GUEST_MODE", true); // Flag untuk guest mode
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "WelcomeActivity destroyed");
    }
}
